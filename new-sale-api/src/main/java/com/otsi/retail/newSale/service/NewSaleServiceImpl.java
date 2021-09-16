package com.otsi.retail.newSale.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.GiftVoucherEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.common.DSAttributes;
import com.otsi.retail.newSale.common.DSStatus;
import com.otsi.retail.newSale.common.PaymentType;
import com.otsi.retail.newSale.mapper.CustomerMapper;
import com.otsi.retail.newSale.mapper.DeliverySlipMapper;
import com.otsi.retail.newSale.mapper.NewSaleMapper;
import com.otsi.retail.newSale.mapper.PaymentAmountTypeMapper;
import com.otsi.retail.newSale.repository.BarcodeRepository;
import com.otsi.retail.newSale.repository.CustomerDetailsRepo;
import com.otsi.retail.newSale.repository.DeliverySlipRepository;
import com.otsi.retail.newSale.repository.GiftVoucherRepo;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.service.HSNVoService;
import com.otsi.retail.newSale.service.NewSaleService;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.MessageVo;
import com.otsi.retail.newSale.vo.NewSaleList;
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
	private CustomerMapper customerMapper;

	@Autowired
	private PaymentAmountTypeMapper paymentAmountTypeMapper;

	@Value("${savecustomer.url}")
	private String url;

	@Value("${getNewSaleWithHsn.url}")
	private String HsnUrl;

	@Autowired
	private HSNVoService hsnService;

	@Autowired
	private GiftVoucherRepo gvRepo;

	@Override
	public String saveNewSaleRequest(NewSaleVo vo) {
		log.debug("deugging saveNewSaleRequest" + vo);
		Random ran = new Random();

		NewSaleEntity entity = new NewSaleEntity();

		if (vo.getCustomerDetails() != null) {

			try {

				CustomerDetailsEntity customerEntity = customerMapper.convertVoToEntity(vo.getCustomerDetails());
				CustomerDetailsEntity savedDetails = customerRepo.save(customerEntity);
				entity.setCustomerDetails(savedDetails);
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<DeliverySlipVo> dlSlips = vo.getDlSlip();
			List<PaymentAmountTypeVo> paymentAmountTypeVos = vo.getPaymentAmountType();
			entity.setNatureOfSale(vo.getNatureOfSale());
			entity.setPaymentType(paymentAmountTypeMapper.VoToEntity(vo.getPaymentAmountType()));
			long paymentAmount = paymentAmountTypeVos.stream().mapToLong(i -> i.getPaymentAmount()).sum();
			entity.setRecievedAmount(paymentAmount);
			long sumMrp = dlSlips.stream().mapToLong(i -> i.getMrp()).sum();
			entity.setGrossAmount(sumMrp);
			long sumPromoDisc = dlSlips.stream().mapToLong(i -> i.getPromoDisc()).sum();
			entity.setTotalPromoDisc(sumPromoDisc);
			entity.setTotalManualDisc(vo.getTotalManualDisc());
			entity.setApprovedBy(vo.getApprovedBy());
			entity.setDiscApprovedBy(vo.getDiscApprovedBy());
			entity.setDiscType(vo.getDiscType());
			entity.setCreatedDate(LocalDate.now());
			// entity.setInvoiceNumber(vo.getInvoiceNumber());

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
				return "Please enter Valid delivery slips..";
			}
		}
		log.warn("we are testing bill generated with number");
		log.info("after generated bill with number:" + entity.getBillNumber());
		return "Bill generated with number :" + entity.getBillNumber();

	}

	@Override
	public String saveBarcode(BarcodeVo vo) {
		log.debug("deugging saveBarcode" + vo);
		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(vo.getBarcode());
		if (barcodeDetails == null) {

			BarcodeEntity barcode = newSaleMapper.convertBarcodeVoToEntity(vo);
			vo = newSaleMapper.convertBarcodeEntityToVo(barcodeRepository.save(barcode));
			log.warn("we are testing barcode saved or not..." + vo);
			log.info("Barcode details saved successfully..");

			return "Barcode details saved successfully..";
		} else {
			log.error("Barcode with " + vo.getBarcode() + " is already exists");
			return "Barcode with " + vo.getBarcode() + " is already exists";
		}
	}

	@Override
	public BarcodeVo getBarcodeDetails(String barCode, String smId) throws Exception {
		log.debug("deugging getBarcodeDetails" + barCode);
		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(barCode);

		if (barcodeDetails == null) {
			log.error("Barcode with number " + barCode + " is not exists");
			throw new Exception("Barcode with number " + barCode + " is not exists");
		} else {
			BarcodeVo vo = newSaleMapper.convertBarcodeEntityToVo(barcodeDetails);
			log.warn("we are fetching barcode details...");
			log.info("after getting barcode details :" + vo);
			return vo;
		}
	}

	// Method for saving delivery slip
	@Override
	public String saveDeliverySlip(DeliverySlipVo vo, String enumName) {
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

			if (enumName.equalsIgnoreCase(DSAttributes.PIECES.getName())) {
				barcodeDetails.stream().forEach(a -> {
					entity.setQty(a.getQty());
					a.setDeliverySlip(savedEntity);
					a.setLastModified(LocalDateTime.now());

					BarcodeEntity bar = barcodeRepository.save(a);

					log.info("----------->" + bar);

				});

			} else if (enumName.equalsIgnoreCase(DSAttributes.METERS.getName())) {
				barcodeDetails.stream().forEach(a -> {
					entity.setQty(a.getQty());// change chesanu
					Long mrp = (entity.getQty() * entity.getMrp());// change chesanu
					a.setMrp(mrp);// change chesanu
					a.setDeliverySlip(savedEntity);
					a.setLastModified(LocalDateTime.now());

					barcodeRepository.save(a);
				});
			}
			/*
			 * MessageVo message = new MessageVo();
			 * message.setMessage("Successfully created deliverySlip with DS Number " +
			 * entity.getDsNumber()); message.setNumber(entity.getDsNumber());
			 */
			return "Successfully created deliverySlip with DS Number " + entity.getDsNumber();

		} catch (Exception e) {
			log.error("error occurs while saving Delivery slip");
			return "error occurs while saving Delivery slip";
		}
	}

	@Override
	public DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws Exception {
		log.debug("deugging getDeliverySlipDetails:" + dsNumber);
		try {

			DeliverySlipEntity dsEntity = dsRepo.findByDsNumber(dsNumber);

			if (dsEntity != null) {
				if (!dsEntity.getBarcodes().isEmpty()) {
					DeliverySlipVo vo = dsMapper.convertDsEntityToVo(dsEntity);
					log.warn("we are testing fetching delivery slip details");
					log.info("after getting delivery slip details :" + vo);
					return vo;
				} else {
					log.error("Barcode details not exists with given DS Number");
					throw new Exception("Barcode details not exists with given DS Number");

				}

			} else {
				log.error("No record with DsNumber :" + dsNumber);
				throw new Exception("No record with DsNumber :\" + dsNumber");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public ListOfSaleBillsVo getListOfSaleBills(ListOfSaleBillsVo svo) throws Exception {
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
					 throw new Exception("No record found with given mobilenumber");
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
					throw new Exception("No record found with given barcode");
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

			if (saleDetails == null) {
				
				log.error("No record found with given information");
				throw new Exception("No record found with given information");

				

			}

			
		}
		ListOfSaleBillsVo lsvo = newSaleMapper.convertlistSalesEntityToVo(saleDetails);
		log.warn("we are fetching sale bills details");
		log.info("after getting  sale bills details :" + lsvo);
		return lsvo;


	}

	@Override
	public ListOfDeliverySlipVo getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo) throws Exception {
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
				throw new Exception("No record found with given barcode");
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
				dsDetails = dsRepo.findByCreatedDateBetweenAndDsIdOrderByCreatedDateAsc(
						listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
						bar.getDeliverySlip().getDsId());

			} else {
				log.error("No record found with given barcode");
				throw new Exception("No record found with given barcode");
			}
		}
		/*
		 * getting the record using dsNumber and dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() != null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreatedDateBetweenAndDsNumberOrderByCreatedDateAsc(
					listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
					listOfDeliverySlipVo.getDsNumber());

			if (dsDetails == null) {
				log.error("No record found with given information");
				throw new Exception("No record found with given information");
			}

		}
		/*
		 * getting the record using status and dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreatedDateBetweenAndStatusOrderByCreatedDateAsc(
					listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
					listOfDeliverySlipVo.getStatus());

			if (dsDetails == null) {
				log.error("No record found with given information");
				throw new Exception("No record found with given information");
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
			dsDetails = dsRepo.findByDsNumberInOrderByCreatedDateAsc(dsList);

			if (dsDetails.isEmpty()) {

				/*
				 * throw new RuntimeException( "No record found with giver DS Number :" +
				 * listOfDeliverySlipVo.getDsNumber());
				 */
				log.error("No record found with given DS Number");
				throw new Exception("No record found with given DS Numbe");
			}

		}

		/*
		 * getting the record using status
		 */

		if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByStatusOrderByCreatedDateAsc(listOfDeliverySlipVo.getStatus());

			if (dsDetails == null) {
				log.error("No record found with given DS Number");
				throw new Exception("No record found with given DS Numbe");
			}

		}
		/*
		 * getting the record using dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreatedDateBetweenOrderByCreatedDateAsc(listOfDeliverySlipVo.getDateFrom(),
					listOfDeliverySlipVo.getDateTo());

			if (dsDetails == null) {
				log.error("No record found with given information");
				throw new Exception("No record found with given information");
			}

		}

		ListOfDeliverySlipVo mapper = newSaleMapper.convertListDSToVo(dsDetails);
		log.warn("we are testing is fetching list of deivery slips");
		log.info("after getting list of delivery slips :" + mapper);
		return mapper;

	}

	@Override
	public String posDayClose() {
		log.debug(" debugging posDayClose");

		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreatedDate(DSStatus.Pending, LocalDate.now());

		if (DsList.isEmpty()) {
			log.info("successfully we can close the day of pos " + " uncleared delivery Slips count :" + DsList.size());
			return 
					"successfully we can close the day of pos " + " uncleared delivery Slips count :  " + DsList.size();

		} else
			log.error("to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size());
		return "to  close the day of pos please clear pending  delivery Slips"
				+ " uncleared delivery Slips count   " + DsList.size();

	}

	@Override
	public String posClose(Boolean posclose) {
		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreatedDate(DSStatus.Pending, LocalDate.now());

		if (DsList.isEmpty() && (posclose == true)) {
			return "successfully we can close the day of pos ";

		} else
			return "to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size();
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

	@Override
	public NewSaleList getInvoicDetails(InvoiceRequestVo vo) throws Exception {
		NewSaleList newSaleList1 = new NewSaleList();
		List<NewSaleVo> newSaleList = new ArrayList<>();
		if (null != vo.getInvoiceNo() && !vo.getInvoiceNo().isEmpty()) {

			List<NewSaleEntity> newSaleEntity = newSaleRepository.findByBillNumber(vo.getInvoiceNo());
			newSaleList = newSaleEntity.stream().map(dto -> newSaleMapper.convertNewSaleDtoToVo(dto))
					.collect(Collectors.toList());
			newSaleList1.setNewSaleVo(newSaleList);
			return newSaleList1;
		}
		if (null != vo.getBarCode() && !vo.getBarCode().isEmpty()) {
			BarcodeEntity barcode = barcodeRepository.findByBarcode(vo.getBarCode());
			DeliverySlipEntity dsSlip = dsRepo.findByDsNumber(barcode.getDeliverySlip().getDsNumber());
			newSaleList.add(newSaleMapper.convertNewSaleDtoToVo(dsSlip.getNewsale()));
			newSaleList1.setNewSaleVo(newSaleList);
			return newSaleList1;
		}
		if (null != vo.getMobileNo() && !vo.getMobileNo().isEmpty()) {

			List<NewSaleEntity> newSaleEntity = newSaleRepository
					.findByCustomerDetailsMobileNumberAndCreatedDateBetween(vo.getMobileNo(), vo.getFromDate(),
							vo.getToDate());
			newSaleList = newSaleEntity.stream().map(dto -> newSaleMapper.convertNewSaleDtoToVo(dto))
					.collect(Collectors.toList());
			newSaleList1.setNewSaleVo(newSaleList);
			return newSaleList1;
		}
		throw new Exception("No records found with your inputs");
	}

	@Override
	public CustomerVo getCustomerFromNewSale(String mobileNo) throws Exception {
		try {
			Optional<CustomerDetailsEntity> responce = newSaleRepository.findByCustomerDetailsMobileNumber(mobileNo);
			if (responce.isPresent() == Boolean.TRUE) {
				return customerMapper.convertEntityToVo(responce.get());
			} else {
				throw new Exception("No Customer Found");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/*
	 * getting getNewSaleWithHsn
	 */
	double result = 0.0;

	@Override
	public double getNewSaleWithHsn(double netAmt) throws JsonMappingException, JsonProcessingException {

		List<HsnDetailsVo> vo = hsnService.getHsn();

		vo.stream().forEach(x -> {

			x.getSlabVos().stream().forEach(a -> {

				if (a.getPriceFrom() <= netAmt && netAmt <= a.getPriceTo()) {

					result = (a.getTaxVo().getSgst() * netAmt) / 100;
					// System.out.println("Value Sgst " + a.getTaxVo().getSgst());
				}
			});
		});
		log.warn("we are testing is fetching new sale with hsn");
		log.info("after getting new sale with hsn:" + vo);

		return result;
	}

	@Override
	public void tagCustomerToExisitingNewSale(String mobileNo, Long invoiceNo) throws CustomerNotFoundExcecption {
		Optional<CustomerDetailsEntity> custmoreOptional = customerRepo.findByMobileNumber(mobileNo);

		if (custmoreOptional.isPresent()) {
			CustomerDetailsEntity customer = custmoreOptional.get();
			List<NewSaleEntity> newSaleList = newSaleRepository.findByInvoiceNumber(invoiceNo);
			newSaleList.stream().forEach(newSale -> {
				try {
					String message = setCustomerToNewSale(newSale, customer);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} else {
			throw new CustomerNotFoundExcecption("No Customer Found with " + mobileNo);
		}
	}

	private String setCustomerToNewSale(NewSaleEntity newSale, CustomerDetailsEntity customer) throws Exception {
		newSale.setCustomerDetails(customer);
		try {
			NewSaleEntity responce = newSaleRepository.save(newSale);
			return "Succesfully Tagged Customer";
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	// Method to save Gift vouchers and GiftVoucher Number should be unique
	@Override
	public String saveGiftVoucher(GiftVoucherVo vo) {

		// Check condition for Duplicate GiftVoucher Numbers
		Optional<GiftVoucherEntity> gvEntity = gvRepo.findByGvNumber(vo.getGvNumber());

		if (!gvEntity.isPresent()) {

			GiftVoucherEntity entity = new GiftVoucherEntity();
			BeanUtils.copyProperties(vo, entity);
			entity.setCreatedDate(LocalDate.now());
			entity.setIsTagged(Boolean.FALSE);
			GiftVoucherEntity savedGf = gvRepo.save(entity);

			return "Gift voucher saved succesfully..";
		} else {
			return "Given Giftvoucher number is already in records.." + gvEntity.get().getGvNumber();
		}
	}

	// Method for getting Gift voucher details by Gv Number
	@Override
	public GiftVoucherVo getGiftVoucher(String gvNumber) throws Exception {

		Optional<GiftVoucherEntity> gvEntity = gvRepo.findByGvNumber(gvNumber);

		if (gvEntity.isPresent()) {
			GiftVoucherVo vo = new GiftVoucherVo();
			BeanUtils.copyProperties(gvEntity.get(), vo);
			return vo;
		} else {
			throw new Exception("please enter valid Gift Voucher number.");
		}
	}

	// Method for tagging Gift voucher to Customer
	@Override
	public String tagCustomerToGv(Long userId, Long gvId) {

		Optional<CustomerDetailsEntity> user = customerRepo.findById(userId);
		if (user.isPresent()) {
			Optional<GiftVoucherEntity> gv = gvRepo.findById(gvId);
			// Gift voucher should not be tagged and expiry date should greater than today
			if (gv.isPresent() && gv.get().getExpiryDate().isAfter(LocalDate.now()) && !gv.get().getIsTagged()) {
				gv.get().setUserId(userId);
				gv.get().setIsTagged(Boolean.TRUE);
				gvRepo.save(gv.get());
			} else {
				return "Gift voucher is not valid";
			}
		} else {
			return "User data is not Available..";
		}
		return "Gift vocher tagged successfully to " + user.get().getName();
	}

}
