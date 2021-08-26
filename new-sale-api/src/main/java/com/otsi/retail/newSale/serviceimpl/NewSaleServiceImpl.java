package com.otsi.retail.newSale.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.common.DSStatus;
import com.otsi.retail.newSale.common.PaymentType;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.mapper.DeliverySlipMapper;
import com.otsi.retail.newSale.mapper.NewSaleMapper;
import com.otsi.retail.newSale.mapper.PaymentAmountTypeMapper;
import com.otsi.retail.newSale.repository.BarcodeRepository;
import com.otsi.retail.newSale.repository.CustomerDetailsRepo;
import com.otsi.retail.newSale.repository.DeliverySlipRepository;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.service.NewSaleService;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.MessageVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.PaymentAmountTypeVo;

/**
 * Service class contains all bussiness logics related to new sale , create
 * barcode , getting barcode details and create delivery slip
 * 
 * @author Manikanta Guptha
 *
 */
@Service
@Configuration
public class NewSaleServiceImpl implements NewSaleService {

	private Logger log = LoggerFactory.getLogger(NewSaleServiceImpl.class);

	@Autowired
	private RestTemplate template;

	@Autowired
	private NewSaleMapper newSaleMapper;

	@Autowired
	private BarcodeRepository barcodeRepository;

	@Autowired
	private DeliverySlipRepository dsRepo;

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private NewSaleRepository newSaleRepository;

	@Autowired
	private PaymentAmountTypeRepository paymentAmountTypeRepository;

	@Autowired
	private DeliverySlipMapper dsMapper;

	@Autowired
	private PaymentAmountTypeMapper paymentAmountTypeMapper;

	@Value("${savecustomer.url}")
	private String url;

	@Value("${getNewSaleWithHsn.url}")
	private String HsnUrl;

	@Override
	public ResponseEntity<?> saveNewSaleRequest(NewSaleVo vo) {
		log.debug("deugging saveNewSaleRequest" + vo);
		Random ran = new Random();

		NewSaleEntity entity = new NewSaleEntity();

		if (vo.getCustomerDetails() != null) {

			try {
				customerService.saveCustomerDetails(vo.getCustomerDetails());
			} catch (Exception e) {

				e.printStackTrace();
			}

			List<DeliverySlipVo> dlSlips = vo.getDlSlip();
			List<PaymentAmountTypeVo> paymentAmountTypeVos = vo.getPaymentAmountType();
			entity.setNatureOfSale(vo.getNatureOfSale());
			// entity.setPayType(vo.getPayType());
			entity.setPaymentType(paymentAmountTypeMapper.VoToEntity(vo.getPaymentAmountType()));
			entity.setRecievedAmount(paymentAmountTypeVos.stream().mapToLong(i -> i.getPaymentAmount()).sum());
			entity.setGrossAmount(dlSlips.stream().mapToLong(i -> i.getMrp()).sum());
			entity.setTotalPromoDisc(dlSlips.stream().mapToLong(i -> i.getPromoDisc()).sum());
			entity.setTotalManualDisc(vo.getTotalManualDisc());
			entity.setCreatedDate(LocalDate.now());

			Long net = dlSlips.stream().mapToLong(i -> i.getNetAmount()).sum() - vo.getTotalManualDisc();

			entity.setNetPayableAmount(net);

			entity.setBillNumber(
					"KLM/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());

			List<String> dlsList = dlSlips.stream().map(x -> x.getDsNumber()).collect(Collectors.toList());

			List<DeliverySlipEntity> dsList = dsRepo.findByDsNumberIn(dlsList);

			if (dsList.size() == vo.getDlSlip().size()) {

				NewSaleEntity saveEntity = newSaleRepository.save(entity);

				dsList.stream().forEach(a -> {

					a.setNewsale(saveEntity);
					a.setLastModified(LocalDateTime.now());

					dsRepo.save(a);
				});
				entity.getPaymentType().forEach(p -> {
					p.setNewsaleId(saveEntity);
					paymentAmountTypeRepository.save(p);

				});

			} else {
				log.error("Please enter Valid delivery slips..");
				return new ResponseEntity<>("Please enter Valid delivery slips..", HttpStatus.BAD_REQUEST);
			}
		}
		log.warn("we are testing bill generated with number");
		log.info("after generated bill with number:" + entity.getBillNumber());
		return new ResponseEntity<>("Bill generated with number :" + entity.getBillNumber(), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> saveBarcode(BarcodeVo vo) {
		log.debug("deugging saveBarcode" + vo);
		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(vo.getBarcode());
		if (barcodeDetails == null) {

			BarcodeEntity barcode = newSaleMapper.convertBarcodeVoToEntity(vo);
			vo = newSaleMapper.convertBarcodeEntityToVo(barcodeRepository.save(barcode));
			log.warn("we are testing barcode saved or not..." + vo);
			log.info("Barcode details saved successfully..");

			return new ResponseEntity<>("Barcode details saved successfully..", HttpStatus.OK);
		} else {
			log.error("Barcode with " + vo.getBarcode() + " is already exists");
			return new ResponseEntity<>("Barcode with " + vo.getBarcode() + " is already exists",
					HttpStatus.BAD_GATEWAY);
		}
	}

	@Override
	public ResponseEntity<?> getBarcodeDetails(String barCode) {
		log.debug("deugging getBarcodeDetails" + barCode);
		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(barCode);

		if (barcodeDetails == null) {
			log.error("Barcode with number " + barCode + " is not exists");
			return new ResponseEntity<>("Barcode with number " + barCode + " is not exists", HttpStatus.BAD_GATEWAY);
		} else {
			BarcodeVo vo = newSaleMapper.convertBarcodeEntityToVo(barcodeDetails);
			log.warn("we are fetching barcode details...");
			log.info("after getting barcode details :" + vo);
			return new ResponseEntity<>(vo, HttpStatus.OK);
		}
	}

	// Method for saving delivery slip
	@Override
	public ResponseEntity<?> saveDeliverySlip(DeliverySlipVo vo) {
		log.debug("deugging saveDeliverySlip:" + vo);
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
			log.warn("after saving deivery slip");
			log.info("after saving deivery slip :" + message);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (Exception e) {
			log.error("error occurs while saving Delivery slip");
			return new ResponseEntity<>("error occurs while saving Delivery slip", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getDeliverySlipDetails(String dsNumber) {
		log.debug("deugging getDeliverySlipDetails:" + dsNumber);
		try {
			DeliverySlipEntity dsEntity = dsRepo.findByDsNumber(dsNumber);

			if (dsEntity != null) {
				if (!dsEntity.getBarcodes().isEmpty()) {
					DeliverySlipVo vo = dsMapper.convertDsEntityToVo(dsEntity);
					log.warn("we are testing fetching delivery slip details");
					log.info("after getting delivery slip details :" + vo);
					return new ResponseEntity<>(vo, HttpStatus.OK);
				} else {
					log.error("Barcode details not exists with given DS Number");
					return new ResponseEntity<>("Barcode details not exists with given DS Number",
							HttpStatus.BAD_REQUEST);
				}

			} else {
				log.error("No record with DsNumber :" + dsNumber);
				return new ResponseEntity<>("No record with DsNumber :" + dsNumber, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("error occurs while saving Delivery slip");
			return new ResponseEntity<>("error occurs while saving Delivery slip", HttpStatus.BAD_REQUEST);
		}

	}

	@Override
	public ResponseEntity<?> getListOfSaleBills(ListOfSaleBillsVo svo) {
		log.debug("deugging getListOfSaleBills:" + svo);
		List<NewSaleEntity> saleDetails = new ArrayList<>();

		/*
		 * getting the data using between dates and bill status or custMobileNumber or
		 * barCode or billNumber or invoiceNumber or dsNumber
		 */
		if (svo.getDateFrom() != null && svo.getDateTo() != null) {
			if (svo.getBillStatus() != null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				saleDetails = newSaleRepository.findByCreatedDateBetweenAndBillStatus(svo.getDateFrom(),
						svo.getDateTo(), svo.getBillStatus());
			}
			/*
			 * getting the record using custmobilenumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() != null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				Optional<CustomerDetailsEntity> customer = customerRepo.findByMobileNumber(svo.getCustMobileNumber());
				if (customer.isPresent()) {
					Optional<NewSaleEntity> newSaleOpt = newSaleRepository
							.findByNewsaleId(customer.get().getNewsale().get(0).getNewsaleId());
					if (newSaleOpt.isPresent()) {
						saleDetails.add(newSaleOpt.get());
					}

				} else {
					log.error("No record found with given mobilenumber");
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

					Optional<NewSaleEntity> newSaleOpt = newSaleRepository
							.findByNewsaleId(bar.getDeliverySlip().getNewsale().getNewsaleId());
					if (newSaleOpt.isPresent()) {
						saleDetails.add(newSaleOpt.get());
					}

				} else {
					log.error("No record found with given barcode");
					return new ResponseEntity<>("No record found with given barcode", HttpStatus.BAD_REQUEST);
				}

			}
			/*
			 * getting the record using billNumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() != null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {
				saleDetails = newSaleRepository.findByBillNumber(svo.getBillNumber());
			}
			/*
			 * getting the record using invoice number
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() != null && svo.getDsNumber() == null) {
				saleDetails = newSaleRepository.findByInvoiceNumber(svo.getInvoiceNumber());
			}
			/*
			 * getting the record using dsNumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() != null) {
				DeliverySlipEntity ds = dsRepo.findByDsNumber(svo.getDsNumber());

				if (ds != null) {
					Optional<NewSaleEntity> newSaleOpt = newSaleRepository
							.findByNewsaleId(ds.getNewsale().getNewsaleId());
					if (newSaleOpt.isPresent()) {
						saleDetails.add(newSaleOpt.get());
					}
				}
			} else
				saleDetails = newSaleRepository.findByCreatedDateBetween(svo.getDateFrom(), svo.getDateTo());

			if (saleDetails != null) {

				ListOfSaleBillsVo lsvo = newSaleMapper.convertlistSalesEntityToVo(saleDetails);
				log.warn("we are fetching sale bills details");
				log.info("after getting  sale bills details :" + lsvo);
				return new ResponseEntity<>(lsvo, HttpStatus.OK);

			}

			else {
				log.error("No record found with given information");
				return new ResponseEntity<>("No record found with given information", HttpStatus.BAD_REQUEST);
			}

		}
		log.info("sucessfully getting the records");
		return new ResponseEntity<>("sucessfully getting the records", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo) {
		log.debug("deugging getlistofDeliverySlips:" + listOfDeliverySlipVo);
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
				log.error("No record found with given barcode");
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
				log.error("No record found with given barcode");
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
				log.error("No record found with given information");
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
				log.error("No record found with given information");
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
				log.error("No record found with given DS Number");
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
				log.error("No record found with given DS Number");
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
				log.error("No record found with given information");
				return new ResponseEntity<>("No record found with given information", HttpStatus.BAD_REQUEST);
			}

		}

		ListOfDeliverySlipVo mapper = newSaleMapper.convertListDSToVo(dsDetails);
		log.warn("we are testing is fetching list of deivery slips");
		log.info("after getting list of delivery slips :" + mapper);
		return new ResponseEntity<>(mapper, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> posDayClose() {
		log.debug(" debugging posDayClose");
		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreatedDate(DSStatus.Pending, LocalDate.now());

		if (DsList.isEmpty()) {
			log.info("successfully we can close the day of pos " + " uncleared delivery Slips count :" + DsList.size());
			return new ResponseEntity<>(
					"successfully we can close the day of pos " + " uncleared delivery Slips count :  " + DsList.size(),
					HttpStatus.OK);

		} else
			log.error("to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size());
		return new ResponseEntity<>("to  close the day of pos please clear pending  delivery Slips"
				+ " uncleared delivery Slips count   " + DsList.size(), HttpStatus.BAD_REQUEST);
	}

	/*
	 * getting getNewSaleWithHsn
	 */
	@Override
	public List<HsnDetailsVo> getNewSaleWithHsn() throws JsonMappingException, JsonProcessingException {

		log.debug(" debugging getNewSaleWithHsn");
		ResponseEntity<?> hsnResponse = template.exchange(HsnUrl, HttpMethod.GET, null, GateWayResponse.class);
		log.debug(" debugging getNewSaleWithHsn()");
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(hsnResponse.getBody(), GateWayResponse.class);

		List<HsnDetailsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<HsnDetailsVo>>() {
				});
		log.warn("we are testing is fetching new sale with hsn");
		log.info("after getting new sale with hsn:" + vo);
		return vo;
	}

	@Override
	public List<NewSaleResponseVo> getNewsaleByCustomerId(Long customerId) {
		log.debug(" debugging getNewsaleByCustomerId:" + customerId);
		List<NewSaleEntity> entity = newSaleRepository.findByCustomerDetailsCustomerId(customerId);
		List<NewSaleResponseVo> vo = newSaleMapper.entityToResVo(entity);
		log.warn("we are testing is fetching getNewsaleByCustomerId");
		log.info("after fetching newsaleByCustomerId:" + vo);
		return vo;
	}

	@Override
	public NewSaleVo updateNewSale(NewSaleResponseVo vo) {
		log.debug(" debugging updateNewSale");
		Optional<NewSaleEntity> newSaleOpt = newSaleRepository.findByNewsaleId(vo.getNewsaleId());
		NewSaleVo newsaleVo = new NewSaleVo();
		if (newSaleOpt.isPresent()) {
			NewSaleEntity newSale = newSaleOpt.get();
			if (!newSale.getInvoiceNumber().equals(vo.getInvoiceNumber())) {
				log.error("invoice is not present");
				throw new RuntimeException("invoice is not present");
			}
			vo.getPaymentAmountTypeId().forEach(p -> {
				PaymentAmountType paymentAmountType = new PaymentAmountType();
				List<PaymentAmountType> paymentAmountTypeList = new ArrayList<>();

				/* any payment type which is paid or cleared debits include in if stmt */
				if (p.getPaymentType() == PaymentType.Card || p.getPaymentType() == PaymentType.Cash
						|| p.getPaymentType() == PaymentType.GETQRCODE || p.getPaymentType() == PaymentType.UPI
						|| p.getPaymentType() == PaymentType.OtherPayments) {
					/* Only paid amounts are deducted from received amount column of newSale */
					newSale.setRecievedAmount(newSale.getRecievedAmount() - p.getPaymentAmount());
				}

				paymentAmountType.setPaymentAmount(p.getPaymentAmount());
				paymentAmountType.setPaymentType(p.getPaymentType());

				paymentAmountTypeList.add(paymentAmountType);

				newSale.setPaymentType(paymentAmountTypeList);
			});

			NewSaleEntity saveEntity = newSaleRepository.save(newSale);
			newSale.getPaymentType().forEach(p -> {
				p.setNewsaleId(saveEntity);
				paymentAmountTypeRepository.save(p);

			});
			newsaleVo = newSaleMapper.entityToVo(saveEntity);
		}
		log.warn("we are checking if newsale is updated");
		log.info("after updating newsaleVo:" + newsaleVo);
		return newsaleVo;
	}

}