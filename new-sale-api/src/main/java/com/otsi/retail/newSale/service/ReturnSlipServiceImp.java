package com.otsi.retail.newSale.service;

import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.common.ReturnSlipStatus;
import com.otsi.retail.newSale.config.Config;
import com.otsi.retail.newSale.mapper.ReturnSlipMapper;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.repository.ReturnSlipRepo;
import com.otsi.retail.newSale.utils.DateConverters;
import com.otsi.retail.newSale.vo.InventoryUpdateVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;
import com.otsi.retail.newSale.vo.ReturnSlipVo;
import com.otsi.retail.newSale.vo.UserDetailsVo;

@Service
public class ReturnSlipServiceImp implements ReturnslipService {

	private Logger log = LogManager.getLogger(ReturnSlipServiceImp.class);

	@Autowired
	private NewSaleRepository newsaleRepo;
    @Autowired
	private ReturnSlipRepo returnSlipRepo;

	@Autowired
	private ReturnSlipMapper returnSlipMapper;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RestTemplate template;

	@Autowired
	private PaymentAmountTypeRepository orderTransactionRepo;
	
	@Autowired
	private NewSaleServiceImpl newsaleserviceImp;

	@Autowired
	Config config;

	@Override
	public ReturnSlipRequestVo getReturnSlip(String returnReferenceNumber, Long storeId) {
		ReturnSlipRequestVo returnslipVo = new ReturnSlipRequestVo();
		ReturnSlip returnSlip = returnSlipRepo.findByRtNoAndStoreId(returnReferenceNumber,
				storeId);
		if (returnSlip != null) {

			ReturnSlipRequestVo returnSlipVo = returnSlipMapper.convertReturnSlipEntityToVo(returnSlip);
			return returnSlipVo;
		}
		else {
		return returnslipVo;
		}
	}

	private String generateRtNumber() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		LocalDate currentdate = LocalDate.now();
		int month = currentdate.getMonth().getValue();
		int year = currentdate.getYear();
		return "RT" + timestamp.getTime();
	}

	/*@Override
	public List<ReturnSlipRequestVo> generateReturnSlip(ReturnSlipRequestVo returnSlipRequestVo) {

		List<PaymentAmountType> paymentAmountType = new ArrayList<>();

		returnSlipRequestVo.getBarcodes().stream().forEach(barcode -> {
			PaymentAmountType orderTransaction = returnSlipMapper.convertVoToEntity(returnSlipRequestVo);

			NewSaleEntity newSaleEntity = newsaleRepo.findByOrderNumber(returnSlipRequestVo.getInvoiceNumber());

			orderTransaction.setReturnReference(generateRtNumber());
			orderTransaction.setOrderId(newSaleEntity);
			orderTransaction.setBarcode(barcode.getBarCode());
			orderTransaction.setPaymentAmount(barcode.getAmount());
			PaymentAmountType orderTransactEntity = orderTransactionRepo.save(orderTransaction);
			paymentAmountType.add(orderTransactEntity);

		});

		List<ReturnSlipRequestVo> returnslipVo = returnSlipMapper.EntityToVo(paymentAmountType);

		return returnslipVo;
	}
*/
	@Override
	public ReturnSlipRequestVo createReturnSlip(ReturnSlipRequestVo returnSlipRequestVo)
			throws JsonProcessingException, DuplicateRecordException {
List<String> barcodesIn = returnSlipRequestVo.getBarcodes().stream().map(barcode->barcode.getBarCode()).collect(Collectors.toList());

ReturnSlip returnslip =	returnSlipRepo.findByInvoiceNumberAndTaggedItems_BarCodeIn(returnSlipRequestVo.getInvoiceNumber(),barcodesIn);
//ReturnSlip returnslip = null;
if(returnslip==null) {
			ReturnSlip returnSlipDto = new ReturnSlip();
			returnSlipDto.setRtNo(generateRtNumber());

			returnSlipDto.setIsReviewed(Boolean.FALSE);
			returnSlipDto.setCreatedBy(returnSlipRequestVo.getCreatedBy());
			List<TaggedItems> barcodes = new ArrayList<>();
			List<Long> returnTotalAmount = new ArrayList<>();
			returnSlipRequestVo.getBarcodes().stream().forEach(b -> {
				TaggedItems tg = new TaggedItems();
				tg.setBarCode(b.getBarCode());
				tg.setQty(b.getQty());
				tg.setAmount(b.getAmount());
				//Long amount = b.getAmount();
			//	returnTotalAmount.add(amount);
				barcodes.add(tg);
			});
	Long totalreturnAmount =barcodes.stream().mapToLong(a->a.getAmount())	.sum();
	if(totalreturnAmount.equals(returnSlipRequestVo.getTotalAmount())) {
	returnSlipDto.setAmount(returnSlipRequestVo.getTotalAmount());
	}else {
		
		throw new InvalidInputException("please provide valid data");
	}

			returnSlipDto.setTaggedItems(barcodes);
			returnSlipDto.setRtStatus(ReturnSlipStatus.PENDING);
			returnSlipDto.setMobileNumber(returnSlipRequestVo.getMobileNumber());
			returnSlipDto.setStoreId(returnSlipRequestVo.getStoreId());
			returnSlipDto.setReason(returnSlipRequestVo.getReason());
			returnSlipDto.setCustomerId(returnSlipRequestVo.getCustomerId());
			returnSlipDto.setSettelmentInfo(returnSlipRequestVo.getComments());
			returnSlipDto.setInvoiceNumber(returnSlipRequestVo.getInvoiceNumber());
			ReturnSlip returnSlip = returnSlipRepo.save(returnSlipDto);

			updateReturnItemsInInventory(returnSlip);

			ReturnSlipRequestVo returnSlipVo = returnSlipMapper.convertReturnSlipEntityToVo(returnSlip);

			log.warn("we are checking if return slip is saved...");
			log.info("Successfully saved " + returnSlipDto.getRtNo());
			return returnSlipVo;
		
	}else {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Returnslip already Generated");
	}
	}

	private void updateReturnItemsInInventory(ReturnSlip returnSlipDto) throws JsonProcessingException {
		// TODO Auto-generated method stub
		List<InventoryUpdateVo> updateVo = new ArrayList<>();
		returnSlipDto.getTaggedItems().stream().forEach(x -> {
			InventoryUpdateVo vo = new InventoryUpdateVo();
			vo.setBarCode(x.getBarCode());
			vo.setQuantity(x.getQty());
			vo.setStoreId(returnSlipDto.getStoreId());
			updateVo.add(vo);
		});
		/*
		 * ObjectMapper objectMapper = new ObjectMapper(); String result =
		 * objectMapper.writeValueAsString(updateVo);
		 */
		rabbitTemplate.convertAndSend(config.getReturnSlipupdateInventoryExchange(), config.getReturnSlipupdateInventoryRK(), updateVo);

	}

	public void updateReturnSlip(Long storeId, String returnSlipNumber) {
		
		ReturnSlip returnSlip = returnSlipRepo.findByRtNoAndStoreId(returnSlipNumber,storeId);
		if(returnSlip!=null) {
			
			returnSlip.setRtStatus(ReturnSlipStatus.COMPLETED);
			
			returnSlipRepo.save(returnSlip);
			
		}

		
	}
	@Override
	public Page<ListOfReturnSlipsVo> getListOfReturnSlips(ListOfReturnSlipsVo vo,Pageable pageable) {
		log.debug("debugging getListOfReturnSlips():" + vo);
		Page<ReturnSlip> retunSlipdetails = null;
		/**
		 * getting the record using dates combination
		 *	
		 */
		LocalDateTime createdDateTo;
		LocalDateTime createdDatefrom = DateConverters.convertLocalDateToLocalDateTime(vo.getDateFrom());
		if(vo.getDateTo()!=null) {
		 createdDateTo = DateConverters.convertToLocalDateTimeMax(vo.getDateTo());
		}else {
			createdDateTo = DateConverters.convertToLocalDateTimeMax(vo.getDateFrom());
	}
				
		if (createdDatefrom != null && createdDateTo != null && vo.getStoreId() != 0L ) {
			/**
			 * getting the record using dates and RtNumber
			 *
			 */
			
			/*
			 * if (vo.getRtStatus() == ReturnSlipStatus.ALL) { retunSlipdetails =
			 * returnSlipRepo.findByCreatedDateBetweenAndStoreIdOrderByCreatedDateAsc(
			 * createdDatefrom,createdDateTo, vo.getStoreId(),pageable); }
			 */

			if (vo.getRtNumber() != null && vo.getBarcode() == null && vo.getCreatedBy() == null
					&& vo.getRtStatus() == null) {
				retunSlipdetails = returnSlipRepo
						.findByCreatedDateBetweenAndRtNoAndStoreIdOrderByCreatedDateAsc(createdDateTo, createdDateTo,
								vo.getRtNumber(), vo.getStoreId(),pageable);
			}

			/**
			 * getting the record using dates and barcode
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreatedBy() == null && vo.getBarcode() != null
					&& vo.getRtStatus() == null) {

				retunSlipdetails = returnSlipRepo
						.findByCreatedDateBetweenAndTaggedItems_barCodeAndStoreIdOrderByCreatedDateAsc(
								createdDatefrom,createdDateTo, vo.getBarcode(), vo.getStoreId(),pageable);

			}
			/**
			 * getting the record using dates and CreatedBy
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreatedBy() != null && vo.getBarcode() == null
					&& vo.getRtStatus() == null) {

				retunSlipdetails = returnSlipRepo
						.findByCreatedDateBetweenAndCreatedByAndStoreIdOrderByCreatedDateAsc(
								createdDatefrom,createdDateTo, vo.getCreatedBy(), vo.getStoreId(),pageable);

			}
			/**
			 * getting the record using dates and status
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreatedBy() == null && vo.getBarcode() == null
					&& vo.getRtStatus() != null) {

				retunSlipdetails = returnSlipRepo
						.findByCreatedDateBetweenAndRtStatusAndStoreIdOrderByCreatedDateAsc(
								createdDatefrom,createdDateTo,vo.getRtStatus() ,vo.getStoreId(),pageable);

			}

			/**
			 * getting the record using dates only
			 *
			 */
			else
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndStoreIdOrderByCreatedDateAsc(
						createdDatefrom,createdDateTo, vo.getStoreId(),pageable);
			/**
			 * getting the records without dates
			 *
			 */
		} else if (vo.getDateFrom() == null && vo.getDateTo() == null && vo.getStoreId() != 0L) {
			/**
			 * getting the record using RtNumber
			 *
			 */
			if (vo.getRtNumber() != null && vo.getCreatedBy() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByRtNoAndStoreIdOrderByCreatedDateAsc(vo.getRtNumber(),
						vo.getStoreId(),pageable);
			} /*
				 * else if (vo.getRtStatus() == ReturnSlipStatus.ALL) {
				 * 
				 * retunSlipdetails =
				 * returnSlipRepo.findByStoreIdOrderByCreatedDateAsc(vo.getStoreId(),pageable);
				 * 
				 * }
				 */

			/**
			 * getting the record using barcode
			 *
			 */

			else if (vo.getRtNumber() == null && vo.getCreatedBy() == null && vo.getBarcode() != null) {

				retunSlipdetails = returnSlipRepo.findByTaggedItems_barCodeAndStoreIdOrderByCreatedDateAsc(
						vo.getBarcode(), vo.getStoreId(),pageable);

			}
			/**
			 * getting the record using RtStatus
			 *
			 */

			else if (vo.getRtNumber() == null && vo.getCreatedBy() == null && vo.getBarcode() == null
					&& vo.getRtStatus() != null) {

				retunSlipdetails = returnSlipRepo.findByRtStatusAndStoreIdOrderByCreatedDateAsc(
						vo.getRtStatus(), vo.getStoreId(),pageable);

			}

			/**
			 * getting the record using RtReviewStatus
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreatedBy() != null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedByAndStoreIdOrderByCreatedDateAsc(
						vo.getCreatedBy(), vo.getStoreId(),pageable);
			}

		}

		Page<ListOfReturnSlipsVo> rvo = returnSlipMapper.mapReturnEntityToVo(retunSlipdetails);

		if (rvo.hasContent()) {
			log.warn("we are checking if list of return slips is fetching...");
			log.info("fetching list of return slips successfully:" + rvo);
			return rvo;
		} else
			log.error("No return slips are found");
		// throw new RuntimeException("no record found with the giveninformation");
		throw new DataNotFoundException("No return slips are found");
	}

	@Override
	public ReturnSlipVo ReturnSlipsDeatils(String rtNumber) throws URISyntaxException {
		ReturnSlip rts = returnSlipRepo.findByRtNo(rtNumber);
		if (rts == null) {
			log.error("given RT number is not exists");
			throw new RecordNotFoundException("given RT number is not exists", 0);
		}
		List<TaggedItems> tgItems = rts.getTaggedItems();
		
	
		
			ReturnSlipVo returnSlipVo =  new ReturnSlipVo();
			returnSlipVo.setRtNo(rts.getRtNo());
			returnSlipVo.setCreatedDate(rts.getCreatedDate());
			returnSlipVo.setTaggedItems(tgItems);
			
			returnSlipVo.setMobileNumber(rts.getMobileNumber());
		if(	rts.getCustomerId()!=null) {
			Long customerId = rts.getCustomerId();
			List<Long> customerIds = new ArrayList<>();
			customerIds.add(customerId);
			List<UserDetailsVo> userDetailsVo = newsaleserviceImp.getUsersForGivenId(customerIds);
			userDetailsVo.stream().forEach(customer->{
				
				returnSlipVo.setCustomerName(customer.getUserName());
			});
	
		}

		/*List<String> barcodes = tgItems.stream().map(x -> x.getBarCode()).collect(Collectors.toList());
		
		List<LineItemVo> bvo = newsaleserviceImp.getBarcodes(barcodes);
		bvo.stream().forEach(x -> {

		
				
				LineItemVo iVo = new LineItemVo();
				iVo.setBarCode(x.getBarCode());
				iVo.setCreatedDate(x.getCreatedDate());
				iVo.setDiscount(x.getDiscount());
				iVo.setDomainId(x.getDomainId());
				iVo.setGrossValue(x.getGrossValue());
				iVo.setItemPrice(x.getItemPrice());
				iVo.setNetValue(x.getNetValue());
				
				iVo.setQuantity(x.getQuantity());
				iVo.setSection(x.getSection());
			
				
				liVo.add(iVo);

			
		});*/
		return returnSlipVo;
	}

}
