package com.otsi.retail.newSale.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.common.ReturnSlipStatus;
import com.otsi.retail.newSale.config.Config;
import com.otsi.retail.newSale.mapper.ReturnSlipMapper;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.repository.ReturnSlipRepo;
import com.otsi.retail.newSale.vo.InventoryUpdateVo;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;

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
			throws JsonProcessingException {

		if (returnSlipRequestVo.getMobileNumber() != null || returnSlipRequestVo.getMobileNumber() != "") {
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
				Long amount = b.getAmount()*b.getQty();
				returnTotalAmount.add(amount);
				barcodes.add(tg);
			});
	Long totalreturnAmount = returnTotalAmount.stream().mapToLong(a->a)	.sum();
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
			ReturnSlip returnSlip = returnSlipRepo.save(returnSlipDto);

			updateReturnItemsInInventory(returnSlip);

			ReturnSlipRequestVo returnSlipVo = returnSlipMapper.convertReturnSlipEntityToVo(returnSlip);

			log.warn("we are checking if return slip is saved...");
			log.info("Successfully saved " + returnSlipDto.getRtNo());
			return returnSlipVo;
		} else {

			throw new InvalidInputException("please provide the mobileNumber");
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
		ObjectMapper objectMapper = new ObjectMapper();
		String result = objectMapper.writeValueAsString(updateVo);
		rabbitTemplate.convertAndSend(config.getUpdateInventoryExchange(), config.getUpdateInventoryRK(), result);

	}

	public void updateReturnSlip(Long storeId, String returnSlipNumber) {
		
		ReturnSlip returnSlip = returnSlipRepo.findByRtNoAndStoreId(returnSlipNumber,storeId);
		if(returnSlip!=null) {
			
			returnSlip.setRtStatus(ReturnSlipStatus.SETTELED);
			
			returnSlipRepo.save(returnSlip);
			
		}

		
	}

}
