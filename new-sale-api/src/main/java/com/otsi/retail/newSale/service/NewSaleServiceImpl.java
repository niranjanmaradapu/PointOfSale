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
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.GiftVoucherEntity;
import com.otsi.retail.newSale.Entity.LineItemsEntity;
import com.otsi.retail.newSale.Entity.LineItemsReEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.common.BillType;
import com.otsi.retail.newSale.common.DSAttributes;
import com.otsi.retail.newSale.common.DSStatus;
import com.otsi.retail.newSale.common.DomainData;
import com.otsi.retail.newSale.common.PaymentType;
import com.otsi.retail.newSale.config.Config;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.mapper.CustomerMapper;
import com.otsi.retail.newSale.mapper.DeliverySlipMapper;
import com.otsi.retail.newSale.mapper.NewSaleMapper;
import com.otsi.retail.newSale.mapper.PaymentAmountTypeMapper;
import com.otsi.retail.newSale.repository.BarcodeRepository;
import com.otsi.retail.newSale.repository.CustomerDetailsRepo;
import com.otsi.retail.newSale.repository.DeliverySlipRepository;
import com.otsi.retail.newSale.repository.GiftVoucherRepo;
import com.otsi.retail.newSale.repository.LineItemReRepo;
import com.otsi.retail.newSale.repository.LineItemRepo;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GetUserRequestVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleList;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.ReturnSummeryVo;
import com.otsi.retail.newSale.vo.SaleReportVo;
import com.otsi.retail.newSale.vo.SalesSummeryVo;
import com.otsi.retail.newSale.vo.TaggedItems;
import com.otsi.retail.newSale.vo.TaxVo;
import com.otsi.retail.newSale.vo.UserDetailsVo;

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

	@Autowired
	private HSNVoService hsnService;

	@Autowired
	private GiftVoucherRepo gvRepo;

	@Autowired
	private Config config;

	@Autowired
	private LineItemRepo lineItemRepo;

	@Autowired
	private LineItemReRepo lineItemReRepo;

	// Method for saving order
	@Override
	public String saveNewSaleRequest(NewSaleVo vo) {
		log.debug("deugging saveNewSaleRequest" + vo);
		NewSaleEntity entity = new NewSaleEntity();

		if (vo.getCustomerDetails() != null) {

			if (vo.getCustomerDetails().getCustomerId() != null) {
				entity.setUserId(vo.getCustomerDetails().getCustomerId());
			} else {
				// Need to get userId from Userdata table once user is saved.
			}

		} else {
			throw new DataNotFoundException("data not found");
		}

		Random ran = new Random();
		entity.setNatureOfSale(vo.getNatureOfSale());
		entity.setDomainId(vo.getDomainId());
		entity.setGrossValue(vo.getGrossAmount());
		entity.setPromoDisc(vo.getTotalPromoDisc());
		entity.setManualDisc(vo.getTotalManualDisc());
		entity.setTaxValue(vo.getTaxAmount());
		entity.setCreatedBy(vo.getApprovedBy());
		entity.setDiscApprovedBy(vo.getDiscApprovedBy());
		entity.setDiscType(vo.getDiscType());
		entity.setCreationDate(LocalDate.now());
		entity.setLastModified(LocalDate.now());
		entity.setStatus(vo.getStatus());
		entity.setOrderNumber(
				"KLM/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());
		entity.setNetValue(vo.getNetPayableAmount());
		entity.setStoreId(vo.getStoreId());
		entity.setOfflineNumber(vo.getOfflineNumber());

		if (vo.getDomainId() == DomainData.TE.getId()) {

			List<DeliverySlipVo> dlSlips = vo.getDlSlip();

			List<String> dlsList = dlSlips.stream().map(x -> x.getDsNumber()).collect(Collectors.toList());

			List<DeliverySlipEntity> dsList = dsRepo.findByDsNumberIn(dlsList);

			if (dsList.size() == vo.getDlSlip().size()) {

				NewSaleEntity saveEntity = newSaleRepository.save(entity);

				dsList.stream().forEach(a -> {

					a.setOrder(saveEntity);
					a.setStatus(DSStatus.Completed);
					a.setLastModified(LocalDateTime.now());
					dsRepo.save(a);

					a.getLineItems().stream().forEach(x -> {

						x.setLastModified(LocalDateTime.now());
						x.setDsEntity(a);
						lineItemRepo.save(x);

					});

				});

			} else {
				log.error("Please provide Valid delivery slips..");
				throw new InvalidInputException("Please provide Valid delivery slips..");
			}
		}
		if (vo.getDomainId() != DomainData.TE.getId()) {

			List<LineItemVo> lineItems = vo.getLineItemsReVo();

			List<Long> lineItemIds = lineItems.stream().map(x -> x.getLineItemId()).collect(Collectors.toList());

			List<LineItemsReEntity> lineItemsList = lineItemReRepo.findByLineItemReIdIn(lineItemIds);

			if (lineItems.size() == lineItemsList.size()) {

				NewSaleEntity saveEntity = newSaleRepository.save(entity);

				lineItemsList.stream().forEach(x -> {

					x.setLastModified(LocalDateTime.now());
					x.setOrderId(saveEntity);
					lineItemReRepo.save(x);

				});

			} else {
				log.error("Please provide valid LineItems..");
				throw new InvalidInputException("Please provide Valid delivery slips..");

			}
		}

		log.warn("we are testing bill generated with number");
		log.info("after generated bill with number:" + entity.getOrderNumber());
		return entity.getOrderNumber();
	}

	@Override
	public String saveBarcode(BarcodeVo vo) throws DuplicateRecordException {
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
			throw new DuplicateRecordException("Barcode with " + vo.getBarcode() + " is already exists");
		}
	}

	@Override
	public BarcodeVo getBarcodeDetails(String barCode, String smId) throws RecordNotFoundException {
		log.debug("deugging getBarcodeDetails" + barCode);
		BarcodeEntity barcodeDetails = barcodeRepository.findByBarcode(barCode);

		if (barcodeDetails == null) {
			log.error("Barcode with number " + barCode + " is not exists");
			throw new RecordNotFoundException("Barcode with number " + barCode + " is not exists");
		} else {
			BarcodeVo vo = newSaleMapper.convertBarcodeEntityToVo(barcodeDetails);
			log.warn("we are fetching barcode details...");
			log.info("after getting barcode details :" + vo);
			return vo;
		}
	}

	// Method for saving delivery slip
	@Override
	public String saveDeliverySlip(DeliverySlipVo vo) throws RecordNotFoundException {
		log.debug("deugging saveDeliverySlip:" + vo);

		DeliverySlipEntity entity = new DeliverySlipEntity();

		Random ran = new Random();
		entity.setDsNumber("DS/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());
		entity.setStatus(DSStatus.Pending);
		entity.setCreationDate(LocalDateTime.now());
		entity.setLastModified(LocalDateTime.now());
		entity.setSalesMan(vo.getSalesMan());

		DeliverySlipEntity savedEntity = dsRepo.save(entity);

		List<Long> lineItems = new ArrayList<>();

		if (!vo.getLineItems().isEmpty()) {

			lineItems = vo.getLineItems().stream().map(x -> x.getLineItemId()).collect(Collectors.toList());

		} else {
			throw new RecordNotFoundException("Provide line items");
		}
		List<LineItemsEntity> listLineItems = lineItemRepo.findByLineItemIdIn(lineItems);

		if (!listLineItems.isEmpty()) {

			listLineItems.stream().forEach(x -> {

				x.setLastModified(LocalDateTime.now());
				x.setDsEntity(savedEntity);
				lineItemRepo.save(x);
			});

			return entity.getDsNumber();
		} else {
			throw new RecordNotFoundException("Provide valid line items");
		}

	}

	@Override
	public DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws RecordNotFoundException {
		log.debug("deugging getDeliverySlipDetails:" + dsNumber);

		DeliverySlipEntity ds = dsRepo.findByDsNumber(dsNumber);

		if (ds != null && ds.getOrder() == null) {

			DeliverySlipVo dsVo = dsMapper.convertDsEntityToDsVo(ds);
			return dsVo;

		} else {
			throw new RecordNotFoundException("Provide valid DS Number");
		}
	}

	@Override
	public ListOfSaleBillsVo getListOfSaleBills(ListOfSaleBillsVo svo)
			throws RecordNotFoundException, JsonMappingException, JsonProcessingException {
		log.debug("deugging getListOfSaleBills:" + svo);
		List<NewSaleEntity> saleDetails = new ArrayList<>();
		// ListOfSaleBillsVo lsvo =new ListOfSaleBillsVo();

		/*
		 * getting the data using between dates and bill status or custMobileNumber or
		 * barCode or billNumber or invoiceNumber or dsNumber
		 */
		if (svo.getDateFrom() != null && svo.getDateTo() != null) {
			if (svo.getBillStatus() != null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				saleDetails = newSaleRepository.findByCreationDateBetweenAndStatus(svo.getDateFrom(), svo.getDateTo(),
						svo.getBillStatus());
			}
			/*
			 * getting the record using custmobilenumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() != null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {

				Optional<CustomerDetailsEntity> customer = customerRepo.findByMobileNumber(svo.getCustMobileNumber());

				UserDetailsVo uvo = getUserDetailsFromURM(svo.getCustMobileNumber(), null);
				if (uvo != null) {
					saleDetails = newSaleRepository.findByUserId(uvo.getUserId());

				}
//				if (customer.isPresent()) {
//					Optional<NewSaleEntity> newSaleOpt = newSaleRepository
//							.findByNewsaleId(customer.get().getNewsale().get(0).getOrderId());
//					if (newSaleOpt.isPresent()) {
//						saleDetails.add(newSaleOpt.get());
//					}
//
				else {
					log.error("No record found with given mobilenumber");
					throw new RecordNotFoundException("No record found with given mobilenumber");
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
							.findByOrderId(bar.getDeliverySlip().getOrder().getOrderId());
					if (newSaleOpt.isPresent()) {
						saleDetails.add(newSaleOpt.get());
					}

				} else {
					log.error("No record found with given barcode");
					throw new RecordNotFoundException("No record found with given barcode");
				}

			}
			/*
			 * getting the record using billNumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() != null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() == null) {
				saleDetails = newSaleRepository.findByOrderNumber(svo.getBillNumber());
			}
			/*
			 * getting the record using invoice number
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() != null && svo.getDsNumber() == null) {
				saleDetails = newSaleRepository.findByOrderNumber(svo.getInvoiceNumber());
			}
			/*
			 * getting the record using dsNumber
			 */
			else if (svo.getBillStatus() == null && svo.getCustMobileNumber() == null && svo.getBillNumber() == null
					&& svo.getBarcode() == null && svo.getInvoiceNumber() == null && svo.getDsNumber() != null) {
				DeliverySlipEntity ds = dsRepo.findByDsNumber(svo.getDsNumber());

				if (ds != null) {
					Optional<NewSaleEntity> newSaleOpt = newSaleRepository.findByOrderId(ds.getOrder().getOrderId());
					if (newSaleOpt.isPresent()) {
						saleDetails.add(newSaleOpt.get());
					}
				}
			} else
				saleDetails = newSaleRepository.findByCreationDateBetween(svo.getDateFrom(), svo.getDateTo());

		}
		if (saleDetails.isEmpty()) {

			log.error("No record found with given information");
			throw new RecordNotFoundException("No record found with given information");

		} else {

			ListOfSaleBillsVo lsvo = newSaleMapper.convertlistSalesEntityToVo(saleDetails);
			////////////////////

			// List<HsnDetailsVo> list = new ArrayList<>();
			NewSaleVo nsvo = new NewSaleVo();
			List<NewSaleVo> sVoList = new ArrayList<>();

			lsvo.getNewSaleVo().stream().forEach(x -> {
				List<BarcodeVo> listBar = new ArrayList<>();

				if (x.getLineItems() != null) {

					x.getLineItems().stream().forEach(l -> {
						// BarcodeVo barVo = new BarcodeVo();

						HsnDetailsVo hsnDetails = getHsnDetails(l.getNetAmount());

						l.setHsnDetailsVo(hsnDetails);

						listBar.add(l);

					});
				}
				/////////
				UserDetailsVo uvo = getUserDetailsFromURM(null, x.getUserId());

				//////////
				x.setCustomerName(uvo.getUserName());
				x.setMobileNumber(uvo.getPhoneNumber());
				x.setLineItems(listBar);
				x.setTotalqQty(x.getLineItems().stream().mapToInt(q -> q.getQty()).sum());
				x.setTotalMrp(x.getLineItems().stream().mapToLong(m -> m.getMrp()).sum());
				x.setTotalNetAmount(x.getLineItems().stream().mapToLong(m -> m.getNetAmount()).sum());

				x.setTotaltaxableAmount((float) listBar.stream()
						.mapToDouble(t -> t.getHsnDetailsVo().getTaxVo().getTaxableAmount()).sum());
				x.setTotalCgst(
						(float) listBar.stream().mapToDouble(t -> t.getHsnDetailsVo().getTaxVo().getCgst()).sum());
				x.setTotalSgst(
						(float) listBar.stream().mapToDouble(t -> t.getHsnDetailsVo().getTaxVo().getSgst()).sum());
				x.setTotalIgst(
						(float) listBar.stream().mapToDouble(t -> t.getHsnDetailsVo().getTaxVo().getIgst()).sum());

			});

			//////////////////

			log.warn("we are fetching sale bills details");
			log.info("after getting  sale bills details :" + lsvo);
			return lsvo;
		}

	}

	public HsnDetailsVo getHsnDetails(double netAmt) {
		log.debug("debugging getHsnDetails:" + netAmt);
		List<HsnDetailsVo> vo = hsnService.getHsn();
		if (vo == null) {
			log.error("Record not found");
			new RecordNotFoundException("Record not found");
		}
		TaxVo tvo = new TaxVo();
		HsnDetailsVo hvo = new HsnDetailsVo();

		vo.stream().forEach(x -> {

			x.getSlabVos().stream().forEach(a -> {

				if (a.getPriceFrom() <= netAmt && netAmt <= a.getPriceTo()) {

					// tvo.setGst((float) ((a.getTaxVo().getCess() * netAmt) / 100));
					tvo.setTaxableAmount((float) netAmt / (1 + (a.getTaxVo().getCess() / 100)));
					// tvo.setGst((tvo.getTaxableAmount()*a.getTaxVo().getGst())/100);
					// tvo.setSgst((float) ((a.getTaxVo().getSgst() * netAmt) / 100));
					tvo.setGst(a.getTaxVo().getCess());
					tvo.setTaxLabel(a.getTaxVo().getTaxLabel());
					tvo.setSgst((float) ((netAmt - tvo.getTaxableAmount()) / 2));
					tvo.setCgst((float) ((netAmt - tvo.getTaxableAmount()) / 2)); // tvo.setIgst((float)
																					// ((a.getTaxVo().getIgst() *
																					// netAmt) / 100));
					// tvo.setCgst((float) ((a.getTaxVo().getCgst() * netAmt) / 100));
					if (a.getTaxVo().getIgst() != 0.00) {
						tvo.setIgst(tvo.getCgst() + tvo.getSgst());

					} else {
						tvo.setIgst((float) 0.00);
					}
					hvo.setHsnCode(x.getHsnCode());
					hvo.setTaxVo(tvo);

				}
			});
		});
		log.warn("we are checking if hsn details is fetching...");
		log.info("fetching hsn details successfuuly:" + hvo);
		return hvo;

	}

	@Override
	public ListOfDeliverySlipVo getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo)
			throws RecordNotFoundException {
		log.debug("deugging getlistofDeliverySlips:" + listOfDeliverySlipVo);
		List<DeliverySlipEntity> dsDetails = new ArrayList<DeliverySlipEntity>();

		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() != null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() != null) {

			BarcodeEntity bar = barcodeRepository.findByBarcode(listOfDeliverySlipVo.getBarcode());

			if (bar != null) {
				dsDetails = dsRepo.findByCreationDateBetweenAndDsIdAndDsNumberAndStatusOrderByCreationDateAsc(
						listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
						bar.getDeliverySlip().getDsId(), listOfDeliverySlipVo.getDsNumber(),
						listOfDeliverySlipVo.getStatus());

			} else {
				log.error("No record found with given barcode");
				throw new RecordNotFoundException("No record found with given barcode");
			}
		}
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
				throw new RecordNotFoundException("No record found with given barcode");
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
				dsDetails = dsRepo.findByCreationDateBetweenAndDsIdOrderByCreationDateAsc(
						listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
						bar.getDeliverySlip().getDsId());

			} else {
				log.error("No record found with given barcode");
				throw new RecordNotFoundException("No record found with given barcode");
			}
		}
		/*
		 * getting the record using dsNumber and dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() != null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreationDateBetweenAndDsNumberOrderByCreationDateAsc(
					listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
					listOfDeliverySlipVo.getDsNumber());

			if (dsDetails == null) {
				log.error("No record found with given information");
				throw new RecordNotFoundException("No record found with given information");
			}

		}
		/*
		 * getting the record using status and dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreationDateBetweenAndStatusOrderByCreationDateAsc(
					listOfDeliverySlipVo.getDateFrom(), listOfDeliverySlipVo.getDateTo(),
					listOfDeliverySlipVo.getStatus());

			if (dsDetails == null) {
				log.error("No record found with given information");
				throw new RecordNotFoundException("No record found with given information");
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
			dsDetails = dsRepo.findByDsNumberInOrderByCreationDateAsc(dsList);

			if (dsDetails.isEmpty()) {

				/*
				 * throw new RuntimeException( "No record found with giver DS Number :" +
				 * listOfDeliverySlipVo.getDsNumber());
				 */
				log.error("No record found with given DS Number");
				throw new RecordNotFoundException("No record found with given DS Numbe");
			}

		}

		/*
		 * getting the record using status
		 */

		if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() != null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByStatusOrderByCreationDateAsc(listOfDeliverySlipVo.getStatus());

			if (dsDetails == null) {
				log.error("No record found with given DS Number");
				throw new RecordNotFoundException("No record found with given DS Numbe");
			}

		}
		/*
		 * getting the record using dates
		 */
		if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
				&& listOfDeliverySlipVo.getDsNumber() == null && listOfDeliverySlipVo.getStatus() == null
				&& listOfDeliverySlipVo.getBarcode() == null) {

			dsDetails = dsRepo.findByCreationDateBetweenOrderByCreationDateAsc(listOfDeliverySlipVo.getDateFrom(),
					listOfDeliverySlipVo.getDateTo());

		}
		if (dsDetails.isEmpty()) {
			log.error("No record found with given information");
			throw new RecordNotFoundException("No record found with given information");
		}

		else {
			ListOfDeliverySlipVo mapper = newSaleMapper.convertListDSToVo(dsDetails);
			log.warn("we are testing is fetching list of deivery slips");
			log.info("after getting list of delivery slips :" + mapper);
			return mapper;
		}

	}

	@Override
	public String posDayClose() {
		log.debug(" debugging posDayClose");

		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreationDate(DSStatus.Pending, LocalDate.now());

		if (DsList.isEmpty()) {
			log.info("successfully we can close the day of pos " + " uncleared delivery Slips count :" + DsList.size());
			return "successfully we can close the day of pos " + " uncleared delivery Slips count :  " + DsList.size();

		} else
			log.error("to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size());
		return "to  close the day of pos please clear pending  delivery Slips" + " uncleared delivery Slips count   "
				+ DsList.size();

	}

	@Override
	public String posClose(Boolean posclose) {
		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreationDate(DSStatus.Pending, LocalDate.now());

		if (DsList.isEmpty() && (posclose == true)) {
			return "successfully we can close the day of pos ";

		} else
			return "to  close the day of pos please clear pending  delivery Slips"
					+ " uncleared delivery Slips count   " + DsList.size();
	}

	@Override
	public List<NewSaleResponseVo> getNewsaleByCustomerId(Long customerId) throws DataNotFoundException {
		log.debug(" debugging getNewsaleByCustomerId:" + customerId);
		List<NewSaleEntity> entity = newSaleRepository.findByUserId(customerId);
		if (entity == null) {
			throw new DataNotFoundException("Data not found");

		}

		List<NewSaleResponseVo> vo = newSaleMapper.entityToResVo(entity);
		log.warn("we are testing is fetching getNewsaleByCustomerId");
		log.info("after fetching newsaleByCustomerId:" + vo);
		return vo;

	}

	@Override
	public NewSaleVo updateNewSale(NewSaleResponseVo vo) throws RecordNotFoundException {
		log.debug(" debugging updateNewSale");
		Optional<NewSaleEntity> newSaleOpt = newSaleRepository.findByOrderId(vo.getNewsaleId());
		NewSaleVo newsaleVo = new NewSaleVo();
		if (newSaleOpt.isPresent()) {
			NewSaleEntity newSale = newSaleOpt.get();
			if (!newSale.getOrderNumber().equals(vo.getInvoiceNumber())) {
				log.error("invoice is not present");
				throw new RecordNotFoundException("invoice is not present");
			}
			vo.getPaymentAmountTypeId().forEach(p -> {
				PaymentAmountType paymentAmountType = new PaymentAmountType();
				List<PaymentAmountType> paymentAmountTypeList = new ArrayList<>();

				/* any payment type which is paid or cleared debits include in if stmt */
				if (p.getPaymentType() == PaymentType.Card || p.getPaymentType() == PaymentType.Cash
						|| p.getPaymentType() == PaymentType.GETQRCODE || p.getPaymentType() == PaymentType.UPI
						|| p.getPaymentType() == PaymentType.OtherPayments) {
					/* Only paid amounts are deducted from received amount column of newSale */
					newSale.setGrossValue(newSale.getGrossValue() - p.getPaymentAmount());
				}

				paymentAmountType.setPaymentAmount(p.getPaymentAmount());
				// paymentAmountType.setPaymentType(p.getPaymentType());

				paymentAmountTypeList.add(paymentAmountType);

				// newSale.setPaymentType(paymentAmountTypeList);
			});

			NewSaleEntity saveEntity = newSaleRepository.save(newSale);
//			newSale.getPaymentType().forEach(p -> {
//				p.setOrderId(saveEntity.getNewsaleId());
//				paymentAmountTypeRepository.save(p);
//			});
			newsaleVo = newSaleMapper.entityToVo(saveEntity);
		}
		log.warn("we are checking if newsale is updated");
		log.info("after updating newsaleVo:" + newsaleVo);
		return newsaleVo;
	}

	@Override
	public NewSaleList getInvoicDetails(InvoiceRequestVo vo) throws RecordNotFoundException {
		log.debug(" debugging getInvoicDetails:" + vo);
		NewSaleList newSaleList1 = new NewSaleList();
		List<NewSaleVo> newSaleList = new ArrayList<>();
		if (null != vo.getInvoiceNo() && !vo.getInvoiceNo().isEmpty()) {

			List<NewSaleEntity> newSaleEntity = newSaleRepository.findByOrderNumber(vo.getInvoiceNo());
			newSaleList = newSaleEntity.stream().map(dto -> newSaleMapper.convertNewSaleDtoToVo(dto))
					.collect(Collectors.toList());
			newSaleList1.setNewSaleVo(newSaleList);
			return newSaleList1;
		}
		if (null != vo.getBarCode() && !vo.getBarCode().isEmpty()) {
			BarcodeEntity barcode = barcodeRepository.findByBarcode(vo.getBarCode());
			DeliverySlipEntity dsSlip = dsRepo.findByDsNumber(barcode.getDeliverySlip().getDsNumber());
			// newSaleList.add(newSaleMapper.convertNewSaleDtoToVo(dsSlip.getNewsale()));
			newSaleList1.setNewSaleVo(newSaleList);
			return newSaleList1;
		}
		if (null != vo.getMobileNo() && !vo.getMobileNo().isEmpty()) {

			List<NewSaleEntity> newSaleEntity = newSaleRepository
					.findByCustomerDetailsMobileNumberAndCreationDateBetween(vo.getMobileNo(), vo.getFromDate(),
							vo.getToDate());
			newSaleList = newSaleEntity.stream().map(dto -> newSaleMapper.convertNewSaleDtoToVo(dto))
					.collect(Collectors.toList());
			newSaleList1.setNewSaleVo(newSaleList);
			return newSaleList1;
		}
		log.error("No records found with your inputs");
		throw new RecordNotFoundException("No records found with your inputs");
	}

	@Override
	public CustomerVo getCustomerFromNewSale(String mobileNo) throws DataNotFoundException {
		log.debug(" debugging getCustomerFromNewSale:" + mobileNo);
		Optional<CustomerDetailsEntity> responce = newSaleRepository.findByCustomerDetailsMobileNumber(mobileNo);
		if (responce.isPresent() == Boolean.TRUE) {
			return customerMapper.convertEntityToVo(responce.get());
		} else {
			log.error("No customer Found");
			throw new DataNotFoundException("No Customer Found");
		}

	}

	/*
	 * getting getNewSaleWithHsn
	 */
	double result = 0.0;

	@Override
	public double getNewSaleWithHsn(double netAmt)
			throws JsonMappingException, JsonProcessingException, DataNotFoundException {
		log.debug(" debugging getNewSaleWithHsn:" + netAmt);
		List<HsnDetailsVo> vo = hsnService.getHsn();

		if (vo == null) {
			log.error("Data not found");
			throw new DataNotFoundException("Data not found");
		}

		vo.stream().forEach(x -> {

			x.getSlabVos().stream().forEach(a -> {

				if (a.getPriceFrom() <= netAmt && netAmt <= a.getPriceTo()) {

					result = (a.getTaxVo().getSgst() * netAmt) / 100;
					// System.out.println("Value Sgst " + a.getTaxVo().getSgst());
				}
			});
		});
		log.warn("we are testing if fetching new sale with hsn");
		log.info("after getting new sale with hsn:" + result);
		return result;
	}

	@Override
	public void tagCustomerToExisitingNewSale(String mobileNo, Long invoiceNo) throws CustomerNotFoundExcecption {
		log.debug(" debugging tagCustomerToExisitingNewSale:" + mobileNo + "and the invoice is:" + invoiceNo);
		Optional<CustomerDetailsEntity> custmoreOptional = customerRepo.findByMobileNumber(mobileNo);

		if (custmoreOptional.isPresent()) {
			CustomerDetailsEntity customer = custmoreOptional.get();
			List<NewSaleEntity> newSaleList = newSaleRepository.findByOrderNumber(invoiceNo);
			newSaleList.stream().forEach(newSale -> {
				try {
					String message = setCustomerToNewSale(newSale, customer);
					log.warn("we are testing if customer is tagged to Exisiting NewSale");
					log.info("after tagCustomerToExisitingNewSale:" + message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} else {
			log.error("No Customer Found with " + mobileNo);
			throw new CustomerNotFoundExcecption("No Customer Found with " + mobileNo);
		}
	}

	private String setCustomerToNewSale(NewSaleEntity newSale, CustomerDetailsEntity customer) throws Exception {
		// newSale.setCustomerDetails(customer);
		log.debug(" debugging setCustomerToNewSale:" + newSale + "and the customer is:" + customer);
		NewSaleEntity responce = newSaleRepository.save(newSale);
		log.warn("we are testing if customer is set to newsale...");
		log.info("after Succesfully Tagged Customer:" + responce);

		return "Succesfully Tagged Customer";

	}

	// Method to save Gift vouchers and GiftVoucher Number should be unique
	@Override
	public String saveGiftVoucher(GiftVoucherVo vo) throws DuplicateRecordException {
		log.debug(" debugging saveGiftVoucher:" + vo);
		// Check condition for Duplicate GiftVoucher Numbers
		Optional<GiftVoucherEntity> gvEntity = gvRepo.findByGvNumber(vo.getGvNumber());

		if (!gvEntity.isPresent()) {

			GiftVoucherEntity entity = new GiftVoucherEntity();
			BeanUtils.copyProperties(vo, entity);
			entity.setCreatedDate(LocalDate.now());
			entity.setIsTagged(Boolean.FALSE);
			GiftVoucherEntity savedGf = gvRepo.save(entity);
			log.warn("we are testing if gv voucher is saved...");
			log.info("after Succesfully saving gift voucher:" + savedGf);
			return "Gift voucher saved succesfully..";
		} else {
			log.error("Given Giftvoucher number is already in records.." + gvEntity.get().getGvNumber());
			throw new DuplicateRecordException(
					"Given Giftvoucher number is already in records.." + gvEntity.get().getGvNumber());
		}
	}

	// Method for getting Gift voucher details by Gv Number
	@Override
	public GiftVoucherVo getGiftVoucher(String gvNumber) throws InvalidInputException {
		log.debug(" debugging getGiftVoucher:" + gvNumber);
		Optional<GiftVoucherEntity> gvEntity = gvRepo.findByGvNumber(gvNumber);

		if (gvEntity.isPresent()) {
			GiftVoucherVo vo = new GiftVoucherVo();
			BeanUtils.copyProperties(gvEntity.get(), vo);
			log.warn("we are testing if gv voucher is fetching...");
			log.info("after fetching  gift voucher:" + vo);
			return vo;
		} else {
			log.error("please enter valid Gift Voucher number.");
			throw new InvalidInputException("please enter valid Gift Voucher number.");
		}
	}

	// Method for tagging Gift voucher to Customer
	@Override
	public String tagCustomerToGv(Long userId, Long gvId) throws InvalidInputException, DataNotFoundException {
		log.debug(" debugging tagCustomerToGv:" + userId + "and the gv id is :" + gvId);
		Optional<CustomerDetailsEntity> user = customerRepo.findById(userId);
		if (user.isPresent()) {
			Optional<GiftVoucherEntity> gv = gvRepo.findById(gvId);
			// Gift voucher should not be tagged and expiry date should greater than today
			if (gv.isPresent() && gv.get().getExpiryDate().isAfter(LocalDate.now()) && !gv.get().getIsTagged()) {
				gv.get().setUserId(userId);
				gv.get().setIsTagged(Boolean.TRUE);
				gvRepo.save(gv.get());
			} else {
				log.error("Gift voucher is not valid");
				throw new InvalidInputException("Gift voucher is not valid");
			}
		} else {
			throw new DataNotFoundException("User data is not Available..");
		}
		log.warn("we are testing if customer is tagged to gv voucher...");
		log.info("after tagging customer to  gift voucher:" + user.get().getName());
		return "Gift vocher tagged successfully to " + user.get().getName();
	}

	// Method for Return all Bar code items
	@Override
	public List<BarcodeVo> getAllBarcodes() throws DataNotFoundException {
		log.debug(" debugging getAllBarcodes()");
		List<BarcodeEntity> listOfBarcodes = barcodeRepository.findAll();
		if (listOfBarcodes.isEmpty()) {
			log.error("data not found");
			throw new DataNotFoundException("data not found");
		}
		List<BarcodeVo> mappedList = newSaleMapper.convertBarcodeListFromEntityToVo(listOfBarcodes);
		log.warn("we are testing if all barcodes is fetching...");
		log.info("after fetching all barcodes:" + mappedList);
		return mappedList;

	}

	@Override
	public List<BarcodeVo> getBarcodes(List<String> barCode) throws RecordNotFoundException {
		log.debug("deugging getBarcodeDetails" + barCode);
		List<BarcodeEntity> barcodeDetails = barcodeRepository.findByBarcodeIn(barCode);
		if (barcodeDetails.isEmpty()) {
			log.error("Barcode with number " + barCode + " is not exists");
			throw new RecordNotFoundException("Barcode with number " + barCode + " is not exists");
		} else {
			List<BarcodeVo> vo = newSaleMapper.convertBarcodesEntityToVo(barcodeDetails);
			log.warn("we are fetching barcode details...");
			log.info("after getting barcode details :" + vo);
			return vo;
		}
	}

	/////////////

	@Override
	public SaleReportVo getSaleReport(SaleReportVo srvo) throws RecordNotFoundException {

		List<NewSaleEntity> saleDetails = new ArrayList<>();

		if ((srvo.getBillType().getId() == BillType.B2C_BILLS.getId())
				|| srvo.getBillType().getId() == BillType.ALL_BILLS.getId()) {
			if (srvo.getDateFrom() != null && srvo.getDateTo() != null) {
				saleDetails = newSaleRepository.findByCreationDateBetween(srvo.getDateFrom(), srvo.getDateTo());
			}
		}

		if (saleDetails.isEmpty()) {
			throw new RecordNotFoundException("Sale bills are not exists");
		} else {

			SaleReportVo slr = newSaleMapper.convertlistSaleReportEntityToVo(saleDetails);
			HsnDetailsVo hsnDetails = getHsnDetails(slr.getBillValue());
			SalesSummeryVo salesSummery = new SalesSummeryVo();
			salesSummery.setTotalTaxableAmount(hsnDetails.getTaxVo().getTaxableAmount());
			salesSummery.setTotalCgst(hsnDetails.getTaxVo().getCgst());
			salesSummery.setTotalSgst(hsnDetails.getTaxVo().getSgst());
			salesSummery.setTotalIgst(hsnDetails.getTaxVo().getIgst());
			salesSummery.setTaxDescription(hsnDetails.getTaxVo().getTaxLabel());
			salesSummery.setBillValue(slr.getBillValue());
			salesSummery.setTotalMrp(slr.getTotalMrp());
			salesSummery.setTotalDiscount(slr.getTotalDiscount());

			salesSummery.setTotalTaxAmount(hsnDetails.getTaxVo().getCgst() + hsnDetails.getTaxVo().getSgst());

			ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetListOfReturnSlips(),
					HttpMethod.GET, null, GateWayResponse.class);

			ObjectMapper mapper = new ObjectMapper();

			GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
					GateWayResponse.class);

			List<ListOfReturnSlipsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
					new TypeReference<List<ListOfReturnSlipsVo>>() {
					});
			Long rAmount = vo.stream().mapToLong(a -> a.getAmount()).sum();
			ReturnSummeryVo retunVo = new ReturnSummeryVo();

			List<BarcodeVo> barVoList = new ArrayList<BarcodeVo>();
			vo.stream().forEach(b -> {

				List<TaggedItems> tgItems = b.getBarcodes();

				List<String> barcodes = tgItems.stream().map(x -> x.getBarCode()).collect(Collectors.toList());

				try {
					List<BarcodeVo> barVo = getBarcodes(barcodes);
					barVo.stream().forEach(r -> {

						barVoList.add(r);

					});

				} catch (RecordNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			System.out.println(vo);

			HsnDetailsVo hsnDetails1 = getHsnDetails(rAmount);

			retunVo.setTotalDiscount(barVoList.stream().mapToLong(d -> d.getPromoDisc()).sum());
			retunVo.setTotalMrp(barVoList.stream().mapToLong(a -> a.getMrp()).sum());
			retunVo.setTaxDescription(hsnDetails1.getTaxVo().getTaxLabel());
			retunVo.setBillValue(rAmount);
			retunVo.setTotalTaxableAmount(hsnDetails1.getTaxVo().getTaxableAmount());
			retunVo.setTotalSgst(hsnDetails1.getTaxVo().getSgst());
			retunVo.setTotalCgst(hsnDetails1.getTaxVo().getCgst());
			retunVo.setTotalIgst(hsnDetails1.getTaxVo().getIgst());
			retunVo.setTotalTaxAmount(hsnDetails1.getTaxVo().getSgst() + hsnDetails1.getTaxVo().getCgst());

			slr.setRetunSummery(retunVo);
			slr.setSalesSummery(salesSummery);
			slr.setBillValue(slr.getSalesSummery().getBillValue() - slr.getRetunSummery().getBillValue());
			slr.setTotalCgst(slr.getSalesSummery().getTotalCgst() - slr.getRetunSummery().getTotalCgst());
			slr.setTotalSgst(slr.getSalesSummery().getTotalSgst() - slr.getRetunSummery().getTotalSgst());
			slr.setTotalIgst(slr.getSalesSummery().getTotalIgst() - slr.getRetunSummery().getTotalIgst());
			slr.setTotalTaxableAmount(
					slr.getSalesSummery().getTotalTaxableAmount() - slr.getRetunSummery().getTotalTaxableAmount());
			slr.setTotalDiscount(slr.getSalesSummery().getTotalDiscount() - slr.getRetunSummery().getTotalDiscount());
			slr.setTotalMrp(slr.getSalesSummery().getTotalMrp() - slr.getRetunSummery().getTotalMrp());
			slr.setTotalTaxAmount(
					slr.getSalesSummery().getTotalTaxAmount() - slr.getRetunSummery().getTotalTaxAmount());

			return slr;
		}

	}

	// Method for saving Line items
	@Override
	public Long saveLineItems(LineItemVo lineItem) throws InvalidInputException {
		log.debug("Debugging saveLineItems() " + lineItem);
		try {
			if (lineItem.getDomainId() == DomainData.TE.getId()) {

				LineItemsEntity lineEntity = new LineItemsEntity();

				lineEntity.setBarCode(lineItem.getBarCode());
				lineEntity.setQuantity(lineItem.getQuantity());
				lineEntity.setDiscount(lineItem.getDiscount());
				lineEntity.setNetValue(lineItem.getNetValue());
				lineEntity.setItemPrice(lineItem.getItemPrice());

				// GrossValue is multiple of net value of product and quantity
				lineEntity.setGrossValue(lineItem.getNetValue() * lineItem.getQuantity());

				lineEntity.setCreationDate(LocalDateTime.now());
				lineEntity.setLastModified(LocalDateTime.now());
				LineItemsEntity saved = lineItemRepo.save(lineEntity);

				log.info("successfully saved line item with id " + saved.getLineItemId());
				return saved.getLineItemId();

			} else {

				LineItemsReEntity lineReEntity = new LineItemsReEntity();

				lineReEntity.setBarCode(lineItem.getBarCode());
				lineReEntity.setQuantity(lineItem.getQuantity());
				lineReEntity.setDiscount(lineItem.getDiscount());
				lineReEntity.setNetValue(lineItem.getNetValue());
				lineReEntity.setItemPrice(lineItem.getItemPrice());

				// GrossValue is multiple of net value of product and quantity
				lineReEntity.setGrossValue(lineItem.getNetValue() * lineItem.getQuantity());

				lineReEntity.setCreationDate(LocalDateTime.now());
				lineReEntity.setLastModified(LocalDateTime.now());
				LineItemsReEntity saved = lineItemReRepo.save(lineReEntity);

				log.info("successfully saved line item with id " + saved.getLineItemReId());
				return saved.getLineItemReId();

			}
		} catch (Exception e) {
			log.error("Getting exception while saving Line item..");
			throw new InvalidInputException("Getting exception while saving Line item..");
		}
	}

	public UserDetailsVo getUserDetailsFromURM(@RequestParam String MobileNumber, @RequestParam Long UserId) {

		UserDetailsVo vo = new UserDetailsVo();
		GetUserRequestVo uvo = new GetUserRequestVo();
		uvo.setPhoneNo(MobileNumber);
		uvo.setId(UserId);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GetUserRequestVo> entity = new HttpEntity<>(uvo, headers);

		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetCustomerDetailsFromURM(),
				HttpMethod.POST, entity, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		vo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<UserDetailsVo>() {
		});

		return vo;

	}

	// Method for modifying Line Item
	@Override
	public String editLineItem(LineItemVo lineItem) {

		if (lineItem.getDomainId() == DomainData.TE.getId()) {

			LineItemsEntity line = lineItemRepo.findByLineItemId(lineItem.getLineItemId());

			if (line != null && line.getDsEntity() == null) {

				line.setLineItemId(lineItem.getLineItemId());
				line.setBarCode(lineItem.getBarCode());
				line.setQuantity(lineItem.getQuantity());
				line.setDiscount(lineItem.getDiscount());
				line.setNetValue(lineItem.getNetValue());
				line.setItemPrice(lineItem.getItemPrice());

				// GrossValue is multiple of net value of product and quantity
				line.setGrossValue(lineItem.getNetValue() * lineItem.getQuantity());

				line.setCreationDate(LocalDateTime.now());
				line.setLastModified(LocalDateTime.now());
				LineItemsEntity saved = lineItemRepo.save(line);

			} else {

				return "provide valid line item";
			}

		} else {

			LineItemsReEntity line = lineItemReRepo.findByLineItemReId(lineItem.getLineItemId());
			if (line != null && line.getOrderId() == null) {
				line.setLineItemReId(lineItem.getLineItemId());
				line.setBarCode(lineItem.getBarCode());
				line.setQuantity(lineItem.getQuantity());
				line.setDiscount(lineItem.getDiscount());
				line.setNetValue(lineItem.getNetValue());
				line.setItemPrice(lineItem.getItemPrice());

				// GrossValue is multiple of net value of product and quantity
				line.setGrossValue(lineItem.getNetValue() * lineItem.getQuantity());

				line.setCreationDate(LocalDateTime.now());
				line.setLastModified(LocalDateTime.now());
				LineItemsReEntity saved = lineItemReRepo.save(line);
			} else {

				return "provide valid line item";
			}
		}
		return "Successfully modified Line Item.";

	}

}
