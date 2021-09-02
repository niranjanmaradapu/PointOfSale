package com.otsi.retail.promoexchange.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.promoexchange.Entity.BarcodeEntity;
import com.otsi.retail.promoexchange.Entity.CustomerDetailsEntity;
import com.otsi.retail.promoexchange.Entity.DeliverySlipEntity;
import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;
import com.otsi.retail.promoexchange.common.DSStatus;
import com.otsi.retail.promoexchange.gateway.GateWayResponse;
import com.otsi.retail.promoexchange.mapper.DeliverySlipMapper;
import com.otsi.retail.promoexchange.mapper.PromoExchangeMapper;
import com.otsi.retail.promoexchange.repository.BarcodeRepository;
import com.otsi.retail.promoexchange.repository.CustomerDetailsRepo;
import com.otsi.retail.promoexchange.repository.DeliverySlipRepository;
import com.otsi.retail.promoexchange.repository.PromoExchangeRepository;
import com.otsi.retail.promoexchange.service.CustomerService;
import com.otsi.retail.promoexchange.service.PromoExchangeService;
import com.otsi.retail.promoexchange.vo.BarcodeVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfDeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfReturnSlipsVo;
import com.otsi.retail.promoexchange.vo.ListOfSaleBillsVo;
import com.otsi.retail.promoexchange.vo.MessageVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;

/**
 * Service class contains all bussiness logics related to new sale , create
 * barcode , getting barcode details and create delivery slip
 * 
 * @author Manikanta Guptha
 *
 */
@Component
public class PromoExchangeServiceeImpl implements PromoExchangeService {

	@Autowired
	private RestTemplate template;

	// Rest call
	@Value("${getdeliveryslip.url}")
	private String getDeliveryslipWithDsnumber;

	@Value("${getreturnslip.url}")
	private String getListOfReturnSlipsUrl;

	@Autowired
	private PromoExchangeMapper promoExchangeMapper;

	@Autowired
	private BarcodeRepository barcodeRepository;

	@Autowired
	private DeliverySlipRepository dsRepo;

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private PromoExchangeRepository promoExchangeRepository;

	@Autowired
	private DeliverySlipMapper dsMapper;

	@Value("${savecustomer.url}")
	private String url;

	@Override
	public ResponseEntity<?> savePromoItemExchangeRequest(PromoExchangeVo vo) {

		Random ran = new Random();

		PromoExchangeEntity entity = new PromoExchangeEntity();

		if (vo.getCustomerDetails() != null) {

			try {
				customerService.saveCustomerDetails(vo.getCustomerDetails());
			} catch (Exception e) {

				e.printStackTrace();
			}

			List<DeliverySlipVo> dlSlips = vo.getDlSlip();
            List<ListOfReturnSlipsVo> returnSlips=vo.getReturnSlips();
			entity.setPayType(vo.getPayType());
			entity.setGrossAmount(dlSlips.stream().mapToLong(i -> i.getMrp()).sum());
			entity.setTotalPromoDisc(dlSlips.stream().mapToLong(i -> i.getPromoDisc()).sum());
			entity.setTotalManualDisc(vo.getTotalManualDisc());
			entity.setCreatedDate(LocalDate.now());
            entity.setNetPayableAmount(dlSlips.stream().mapToLong(i->i.getNetAmount()).sum());
            entity.setRecievedAmount(returnSlips.stream().mapToLong(r->r.getAmount()).sum());
		    Long balanceAmount=entity.getNetPayableAmount()-entity.getRecievedAmount();              
			Long net = dlSlips.stream().mapToLong(i -> i.getNetAmount()).sum() - vo.getTotalManualDisc();

			entity.setNetPayableAmount(net);
            entity.setBalanceAmount(balanceAmount);
			entity.setBillNumber(
					"KLM/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());

			List<String> dlsList = dlSlips.stream().map(x -> x.getDsNumber()).collect(Collectors.toList());

			List<DeliverySlipEntity> dsList = dsRepo.findByDsNumberIn(dlsList);

			if (dsList.size() == vo.getDlSlip().size()) {

				PromoExchangeEntity saveEntity = promoExchangeRepository.save(entity);

				dsList.stream().forEach(a -> {

					a.setPromoexchange(saveEntity);
					a.setLastModified(LocalDateTime.now());

					dsRepo.save(a);
				});

			} else {
				return new ResponseEntity<>("Please enter Valid delivery slips..", HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>("Bill generated with number :" + entity.getBillNumber(), HttpStatus.OK);

	}

	@Override
	public DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws Exception {

		DeliverySlipEntity dsEntity = dsRepo.findByDsNumber(dsNumber);

		if (dsEntity != null) {
			if (!dsEntity.getBarcodes().isEmpty()) {
				DeliverySlipVo vo = dsMapper.convertDsEntityToVo(dsEntity);

				return vo;
			} else {
				throw new Exception("Barcode details not exists with given DS Number");
			}

		} else {
			throw new Exception("No record with DsNumber :" + dsNumber);
		}

	}

	@Override
	public ResponseEntity<?> saveBarcode(BarcodeVo vo) {

		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(vo.getBarcode());
		if (barcodeDetails == null) {

			BarcodeEntity barcode = promoExchangeMapper.convertBarcodeVoToEntity(vo);
			barcodeRepository.save(barcode);

			return new ResponseEntity<>("Barcode details saved successfully..", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Barcode with " + vo.getBarcode() + " is already exists",
					HttpStatus.BAD_GATEWAY);
		}
	}

	@Override
	public ResponseEntity<?> getBarcodeDetails(String barCode) {

		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(barCode);

		if (barcodeDetails == null) {
			return new ResponseEntity<>("Barcode with number " + barCode + " is not exists", HttpStatus.BAD_GATEWAY);
		} else {
			BarcodeVo vo = promoExchangeMapper.convertBarcodeEntityToVo(barcodeDetails);
			return new ResponseEntity<>(vo, HttpStatus.OK);
		}
	}

	// Method for saving delivery slip
	@Override
	public ResponseEntity<?> saveDeliverySlip(DeliverySlipVo vo) {
		try {

			Random ran = new Random();
			DeliverySlipEntity entity = dsMapper.convertDsVoToEntity(vo);

			entity.setDsNumber(
					"DS/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());

			DeliverySlipEntity savedEntity = dsRepo.save(entity);

			List<BarcodeVo> barVo = vo.getBarcode();
			List<String> barcodeList = barVo.stream().map(x -> x.getBarcode()).collect(Collectors.toList());

			List<BarcodeEntity> barcodeDetails = barcodeRepository.findByBarcodeIn(barcodeList);

			barcodeDetails.stream().forEach(a -> {

				a.setDeliverySlip(savedEntity);
				a.setLastModified(LocalDateTime.now());

				barcodeRepository.save(a);
			});
			MessageVo message = new MessageVo();
			message.setMessage("Successfully created deliverySlip with DS Number " + entity.getDsNumber());
			message.setNumber(entity.getDsNumber());

			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("error occurs while saving Delivery slip", HttpStatus.BAD_REQUEST);
		}
	}

//Rest call for get deliveryslips

	/*
	 * @Override public DeliverySlipVo getDeliverySlipDetails1(String dsNumber) {
	 * //log.debug("debugging getNewsaleByCustomerId:" + dsNumber);
	 * 
	 * ResponseEntity<DeliverySlipVo> deliveryResponse =
	 * template.exchange(getDeliveryslipWithDsnumber + "?dsNumber="+dsNumber ,
	 * HttpMethod.GET, null, DeliverySlipVo.class);
	 * 
	 * return deliveryResponse.getBody(); }
	 */

	// Rest call for get list of return slips

//	@Override
//	public List<ReturnSlipsVo> getListOfReturnSlips(ReturnSlipsVo vo)
//	{
//
//		// log.debug("debugging getNewsaleByCustomerId:" + dsNumber);
//
//		
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		ObjectMapper mapper = new ObjectMapper();
//		HttpEntity<ReturnSlipsVo> requestEntity = new HttpEntity<>(vo, headers);
//
//		
////		ResponseEntity<?>  returnSlipResponse= template.exchange(getListOfReturnSlips,
////				HttpMethod.POST, requestEntity, GateWayResponse.class);
////		ListOfReturnSlipsVo
//		
//		
//		ResponseEntity<List<ReturnSlipsVo>> responce= template.exchange(getListOfReturnSlips,HttpMethod.POST,
//				 requestEntity,new ParameterizedTypeReference <List<ReturnSlipsVo>> (){
//				} );
//		 
//			/*
//			 * List<ReturnSlipsVo> finalList= Arrays.stream(responce.getBody()). map(vos
//			 * ->mapper.convertValue(vos, ReturnSlipsVo.class))
//			 * .collect(Collectors.toList());
//			 */
//		//ListOfReturnSlipsVo vo1=	mapper.convertValue(returnSlipResponse.getResult(), ListOfReturnSlipsVo.class);
//		//List<ReturnSlipsVo>vos=(List<ReturnSlipsVo>) returnSlipResponse.getResult();
//		/*
//		 * // List<ListOfReturnSlipsVo> result = returnSlipsResponse.getBody();
//		 * ObjectMapper mapper = new ObjectMapper(); //
//		 * mapper.readValue(getDeliveryslipWithDsnumber, null)
//		 * 
//		 * GateWayResponse<?> gatewayResponse =
//		 * mapper.convertValue(returnSlipResponse.getBody(), GateWayResponse.class);
//		 * 
//		 * List<ReturnSlipsVo> lvo = mapper.convertValue(gatewayResponse.getResult(),
//		 * new TypeReference<List<ReturnSlipsVo>>() { });
//		 * 
//		 * 
//		 * // ObjectMapper mapper = new ObjectMapper(); // ListOfReturnSlipsVo
//		 * gatewayResponse = // mapper.convertValue(returnSlipsResponse.getBody(),
//		 * ListOfReturnSlipsVo.class); // // ListOfReturnSlipsVo vo1 = null;
//		 * 
//		 * try { vo = mapper.readValue(deliveryResponse.getBody().toString(), new
//		 * TypeReference<List<DeliverySlipVo>>() { });
//		 * 
//		 * } catch (JsonMappingException e) { // TODO Auto-generated catch block
//		 * e.printStackTrace(); } catch (JsonProcessingException e) { // TODO
//		 * Auto-generated catch block e.printStackTrace(); }
//		 */
//
//		return responce.getBody();
//	}

	@Override
	public List<ListOfReturnSlipsVo> getListOfRetunSlips() throws JsonMappingException, JsonProcessingException {

		ResponseEntity<?> returnSlipListResponse = template.exchange(getListOfReturnSlipsUrl, HttpMethod.GET, null,
				GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		List<ListOfReturnSlipsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<ListOfReturnSlipsVo>>() {
				});

		return vo;
	}

	@Override
	public ResponseEntity<?> getListOfSaleBills(ListOfSaleBillsVo svo) {

		List<PromoExchangeEntity> saleDetails = new ArrayList<>();

		/*
		 * getting the data using between dates and bill status or custMobileNumber or
		 * barCode or billNumber or invoiceNumber or dsNumber
		 */
		if (svo.getDateFrom() != null && svo.getDateTo() != null) {
			if (svo.getBillStatus() != null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				saleDetails = promoExchangeRepository.findByCreatedDateBetweenAndBillStatus(svo.getDateFrom(),
						svo.getDateTo(), svo.getBillStatus());
			}
			/*
			 * getting the record using custmobilenumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() != null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				Optional<CustomerDetailsEntity> customer = customerRepo.findByMobileNumber(svo.getCustMobileNumber());
				if (customer.isPresent()) {
					saleDetails = promoExchangeRepository
							.findByPromoExchangeId(customer.get().getPromoexchange().get(0).getPromoExchangeId());

				} else {
					return new ResponseEntity<>("No record found with given mobilenumber", HttpStatus.BAD_REQUEST);
				}

			}
			/*
			 * getting the record using barcode
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() != null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				BarcodeEntity bar = barcodeRepository.findByBarcode(svo.getBarcode());
				if (bar != null) {

					saleDetails = promoExchangeRepository
							.findByPromoExchangeId(bar.getDeliverySlip().getPromoexchange().getPromoExchangeId());

				} else {
					return new ResponseEntity<>("No record found with given barcode", HttpStatus.BAD_REQUEST);
				}

			}
			/*
			 * getting the record using billNumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() != null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {
				saleDetails = promoExchangeRepository.findByBillNumber(svo.getBillNumber());
			}
			/*
			 * getting the record using invoice number
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() != null && svo.getDsNumber() == null) {
				saleDetails = promoExchangeRepository.findByInvoiceNumber(svo.getInvoiceNumber());
			}
			/*
			 * getting the record using dsNumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() != null) {
				DeliverySlipEntity ds = dsRepo.findByDsNumber(svo.getDsNumber());

				if (ds != null) {
					saleDetails = promoExchangeRepository
							.findByPromoExchangeId(ds.getPromoexchange().getPromoExchangeId());
				}
			} else
				saleDetails = promoExchangeRepository.findByCreatedDateBetween(svo.getDateFrom(), svo.getDateTo());

			if (saleDetails != null) {

				ListOfSaleBillsVo lsvo = promoExchangeMapper.convertlistSalesEntityToVo(saleDetails);
				return new ResponseEntity<>(lsvo, HttpStatus.OK);

			}

			else {

				return new ResponseEntity<>("No record found with given information", HttpStatus.BAD_REQUEST);
			}

		}
		return new ResponseEntity<>("sucessfully getting the records", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo) {

		List<DeliverySlipEntity> dsDetails = new ArrayList<DeliverySlipEntity>();
		/*
		 * getting the record using barcode
		 */

		if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() != null) {

			BarcodeEntity bar = barcodeRepository.findByBarcode(listOfDeliverySlipVo.getBarcode());

			if (bar != null) {
				dsDetails = dsRepo.findByDsId(bar.getDeliverySlip().getDsId());

			} else {
				return new ResponseEntity<>("No record found with given barcode", HttpStatus.BAD_REQUEST);
			}
		}
		/*
		 * getting the record using barcode and dates
		 */

		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() != null) {

			BarcodeEntity bar = barcodeRepository.findByBarcode(listOfDeliverySlipVo.getBarcode());

			if (bar != null) {
				dsDetails = dsRepo.findByCreatedDateBetweenAndDsId(listOfDeliverySlipVo.getDateFrom(),
						listOfDeliverySlipVo.getDateTo(), bar.getDeliverySlip().getDsId());

			} else {
				return new ResponseEntity<>("No record found with given barcode", HttpStatus.BAD_REQUEST);
			}
		}
		/*
		 * getting the record using dsNumber and dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() != null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreatedDateBetweenAndDsNumber(listOfDeliverySlipVo.getDateFrom(),
					listOfDeliverySlipVo.getDateTo(), listOfDeliverySlipVo.getDsNumber());

			if (dsDetails == null) {

				return new ResponseEntity<>("No record found with given information", HttpStatus.BAD_REQUEST);
			}

		}
		/*
		 * getting the record using status and dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreatedDateBetweenAndStatus(listOfDeliverySlipVo.getDateFrom(),
					listOfDeliverySlipVo.getDateTo(), listOfDeliverySlipVo.getStatus());

			if (dsDetails == null) {

				return new ResponseEntity<>("No record found with given information", HttpStatus.BAD_REQUEST);
			}

		}

		/*
		 * getting the record using dsNumber
		 */

		if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
				&& listOfDeliverySlipVo.getDsNumber() != null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			// List<String> dlsList = listOfDeliverySlipVo.stream().map(x ->
			// x.getDsNumber()).collect(Collectors.toList());
			List<String> dsList = new ArrayList<>();
			dsList.add(listOfDeliverySlipVo.getDsNumber());
			dsDetails = dsRepo.findByDsNumberIn(dsList);

			if (dsDetails.isEmpty()) {

				/*
				 * throw new RuntimeException( "No record found with giver DS Number :" +
				 * listOfDeliverySlipVo.getDsNumber());
				 */
				return new ResponseEntity<>("No record found with giver DS Number", HttpStatus.BAD_REQUEST);
			}

		}

		/*
		 * getting the record using status
		 */

		if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByStatus(listOfDeliverySlipVo.getStatus());

			if (dsDetails == null) {

				return new ResponseEntity<>("No record found with giver DS Number", HttpStatus.BAD_REQUEST);
			}

		}
		/*
		 * getting the record using dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreatedDateBetween(listOfDeliverySlipVo.getDateFrom(),
					listOfDeliverySlipVo.getDateTo());

			if (dsDetails == null) {

				return new ResponseEntity<>("No record found with given information", HttpStatus.BAD_REQUEST);
			}

		}

		ListOfDeliverySlipVo mapper = promoExchangeMapper.convertListDSToVo(dsDetails);
		return new ResponseEntity<>(mapper, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> posDayClose() {

		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreatedDate(DSStatus.Pending, LocalDate.now());

		if (DsList.isEmpty()) {
			return new ResponseEntity<>(
					"successfully we can close the day of pos " + " uncleared delivery Slips count :  " + DsList.size(),
					HttpStatus.OK);

		} else
			return new ResponseEntity<>("to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size(), HttpStatus.BAD_REQUEST);
	}

	/*
	 * @Override public DeliverySlipVo getNewsaleWithDeliveryslip(String dsNumber) {
	 * 
	 * 
	 * 
	 * ResponseEntity<?> deliveryResponse =
	 * template.exchange(getDeliveryslipWithDsnumber + "?dsNumber=" + dsNumber,
	 * HttpMethod.GET, null, String.class); ObjectMapper mapper = new
	 * ObjectMapper(); // DeliverySlipVo vo = null;
	 * 
	 * DeliverySlipVo vo = null; try { vo =
	 * mapper.readValue(deliveryResponse.getBody().toString(),
	 * DeliverySlipVo.class); } catch (JsonMappingException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } catch
	 * (JsonProcessingException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * return vo; }
	 */
	/*
	 * @Override public Optional<PromoExchangeEntity> getPromoItems(Long
	 * promoExchangeId) { Optional<PromoExchangeEntity> vo =
	 * promoExchangeRepository.findById(promoExchangeId);
	 * 
	 * return vo; }
	 */
}
