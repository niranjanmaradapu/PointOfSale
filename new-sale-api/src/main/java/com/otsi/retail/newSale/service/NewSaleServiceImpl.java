package com.otsi.retail.newSale.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.GiftVoucherEntity;
import com.otsi.retail.newSale.Entity.LineItemsEntity;
import com.otsi.retail.newSale.Entity.LoyalityPointsEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.Exceptions.BusinessException;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.common.AccountType;
import com.otsi.retail.newSale.common.DSStatus;
import com.otsi.retail.newSale.common.DomainData;
import com.otsi.retail.newSale.common.OrderStatus;
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
import com.otsi.retail.newSale.repository.LoyalityPointsRepo;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.utils.DateConverters;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GetUserRequestVo;
import com.otsi.retail.newSale.vo.GiftVoucherSearchVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.InventoryUpdateVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.LedgerLogBookVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.LoyalityPointsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.PaymentDetailsVo;
import com.otsi.retail.newSale.vo.ReturnSlipVo;
import com.otsi.retail.newSale.vo.ReturnSummeryVo;
import com.otsi.retail.newSale.vo.SaleBillsVO;
import com.otsi.retail.newSale.vo.SaleReportVo;
import com.otsi.retail.newSale.vo.SalesSummeryVo;
import com.otsi.retail.newSale.vo.SearchLoyaltyPointsVo;
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

	private Logger log = LogManager.getLogger(NewSaleServiceImpl.class);

	@Autowired
	private RestTemplate template;

	@Autowired
	private NewSaleMapper newSaleMapper;

	@Autowired
	private ReturnSlipServiceImp returnSlipServiceImpl;

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
	private ReturnSlipServiceImp returnSlipServiceImp;

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

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private LoyalityPointsRepo loyalityRepo;

	// Method for saving order
	@Override
	public String saveNewSaleRequest(NewSaleVo newsaleVo) throws InvalidInputException {

		NewSaleEntity entity = new NewSaleEntity();

		entity.setUserId(newsaleVo.getUserId());
		entity.setNatureOfSale(newsaleVo.getNatureOfSale());
		entity.setNote(newsaleVo.getNote());
		entity.setDomainId(newsaleVo.getDomainId());
		entity.setGrossValue(newsaleVo.getGrossAmount());
		entity.setPromoDisc(newsaleVo.getTotalPromoDisc());
		entity.setManualDisc(newsaleVo.getTotalManualDisc());
		entity.setTaxValue(newsaleVo.getTaxAmount());
		entity.setCreatedBy(newsaleVo.getApprovedBy());
		entity.setDiscApprovedBy(newsaleVo.getDiscApprovedBy());
		entity.setDiscType(newsaleVo.getDiscType());
		/*
		 * entity.setCreationDate(LocalDate.now());
		 * entity.setLastModified(LocalDate.now());
		 */
		entity.setStatus(OrderStatus.New);// Initial Order status should be new
		entity.setStatus(newsaleVo.getStatus());
		entity.setNetValue(newsaleVo.getNetPayableAmount());
		entity.setStoreId(newsaleVo.getStoreId());
		entity.setOfflineNumber(newsaleVo.getOfflineNumber());
		Random ran = new Random();
		entity.setOrderNumber(
				"KLM/" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + "/" + ran.nextInt());

		// Check for payment type
		Long paymentValue = 0l;
		if (newsaleVo.getPaymentAmountType() != null) {
			paymentValue = newsaleVo.getPaymentAmountType().stream().mapToLong(x -> x.getPaymentAmount()).sum();
		}
		if (newsaleVo.getReturnAmount() != null) {
			paymentValue = newsaleVo.getRecievedAmount() - newsaleVo.getReturnAmount();
			if (paymentValue.equals(newsaleVo.getNetPayableAmount())) {
				entity.setStatus(OrderStatus.Completed);// Status should override once it is cash only
			}
		} else if (newsaleVo.getReturnAmount() == null || newsaleVo.getReturnAmount() == 0) {
			if (paymentValue.equals(newsaleVo.getNetPayableAmount())) {
				entity.setStatus(OrderStatus.Completed);// Status should override once it is cash only
			}
		}

		// if (newsaleVo.getDomainId() == DomainData.TE.getId()) {

		List<DeliverySlipVo> dlSlips = newsaleVo.getDlSlip();

		List<String> dlsList = dlSlips.stream().map(x -> x.getDsNumber()).collect(Collectors.toList());

		List<DeliverySlipEntity> dsList = dsRepo.findByDsNumberInAndOrderIsNull(dlsList);

		if (dsList.size() == newsaleVo.getDlSlip().size()) {

			NewSaleEntity saveEntity = newSaleRepository.save(entity);

			List<DeliverySlipEntity> dsLists = new ArrayList<>();
			log.info("Saved order :" + saveEntity);

			dsList.stream().forEach(a -> {

				a.setOrder(saveEntity);
				a.setStatus(DSStatus.Completed);
				DeliverySlipEntity ds = dsRepo.save(a);
				dsLists.add(ds);

				a.getLineItems().stream().forEach(x -> {

					x.setLastModifiedDate(LocalDateTime.now());
					x.setDsEntity(a);
					lineItemRepo.save(x);

				});

			});
			saveEntity.setDlSlip(dsLists);
			// Saving order details in order_transaction

			if (newsaleVo.getPaymentAmountType() != null) {
				newsaleVo.getPaymentAmountType().stream().forEach(x -> {

					// Checking the payment type condition
					if ((x.getPaymentType().equals(PaymentType.PKTADVANCE))
							|| (x.getPaymentType().equals(PaymentType.PKTPENDING))) {
						// Calling the accounting(either debit/credit)
						updateAccounting(newsaleVo);
					}
					if ((x.getPaymentType().equals(PaymentType.RTSlip))) {
						try {
							if (newsaleVo.getNetPayableAmount() >= x.getPaymentAmount()) {

								// update returnSlipStatus
								returnSlipServiceImp.updateReturnSlip(newsaleVo.getStoreId(),
										newsaleVo.getReturnSlipNumber());

							} else if (newsaleVo.getNetPayableAmount() < x.getPaymentAmount()) {
								Long creditAmount = 0l;
								returnSlipServiceImp.updateReturnSlip(newsaleVo.getStoreId(),
										newsaleVo.getReturnSlipNumber());
								creditAmount = x.getPaymentAmount() - newsaleVo.getNetPayableAmount();
								saveCreditNotes(creditAmount, newsaleVo.getMobileNumber(), newsaleVo.getStoreId(),
										newsaleVo.getReturnSlipNumber(), newsaleVo.getUserId());

							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					PaymentAmountType type = new PaymentAmountType();
					type.setOrderId(saveEntity);
					type.setPaymentAmount(x.getPaymentAmount());
					type.setPaymentType(x.getPaymentType().getType());

					paymentAmountTypeRepository.save(type);
				});
				log.info("payment is done for order : " + saveEntity.getOrderNumber());
			}
			// Condition to update inventory
			// if (vo.getReturnAmount() != null) {
			// Condition to update inventory
			if (paymentValue.equals(newsaleVo.getNetPayableAmount())) {
				updateOrderItemsInInventory(saveEntity);
			}
			// }

			/*
			 * } else { log.error("Delivery slips are not valid" + newsaleVo); throw new
			 * InvalidInputException("Please provide Valid delivery slips.."); } } if
			 * (newsaleVo.getDomainId() != DomainData.TE.getId()) {
			 * 
			 * Map<String, Integer> map = new HashMap<>();
			 * 
			 * List<LineItemVo> lineItems = newsaleVo.getLineItemsReVo();
			 * 
			 * List<Long> lineItemIds = lineItems.stream().map(x ->
			 * x.getLineItemId()).collect(Collectors.toList());
			 * 
			 * List<LineItemsReEntity> lineItemsList =
			 * lineItemReRepo.findByLineItemReIdInAndOrderIdIsNull(lineItemIds);
			 * 
			 * if (lineItems.size() == lineItemsList.size()) {
			 * 
			 * NewSaleEntity saveEntity = newSaleRepository.save(entity);
			 * List<LineItemsReEntity> lineItemsForUpdate = new ArrayList<>();
			 * 
			 * lineItemsList.stream().forEach(x -> {
			 * 
			 * map.put(x.getBarCode(), x.getQuantity());
			 * 
			 * x.setLastModified(LocalDate.now()); x.setOrderId(saveEntity);
			 * lineItemReRepo.save(x); lineItemsForUpdate.add(x);
			 * 
			 * }); saveEntity.setLineItemsRe(lineItemsForUpdate); // Saving order details in
			 * order_transaction table if (newsaleVo.getPaymentAmountType() != null) {
			 * 
			 * newsaleVo.getPaymentAmountType().stream().forEach(x -> {
			 * 
			 * PaymentAmountType type = new PaymentAmountType();
			 * type.setOrderId(saveEntity); type.setPaymentAmount(x.getPaymentAmount());
			 * type.setPaymentType(x.getPaymentType().getType());
			 * 
			 * paymentAmountTypeRepository.save(type); });
			 * log.info("payment is done for order : " + saveEntity.getOrderNumber()); }
			 * 
			 * // Condition to update inventory if
			 * (paymentValue.equals(newsaleVo.getNetPayableAmount())) {
			 * updateOrderItemsInInventory(saveEntity); }
			 * 
			 * } else { log.error("LineItems are not valid : " + newsaleVo); throw new
			 * InvalidInputException("Please provide Valid delivery slips..");
			 * 
			 * }
			 */
		}
		log.info("Order generated with number : " + entity.getOrderNumber());
		return entity.getOrderNumber();
	}

	// UPDATE ACCOUNTING

	private void updateAccounting(NewSaleVo newsaleVo) {

		if (newsaleVo.getDomainId() == DomainData.TE.getId()) {
			LedgerLogBookVo ledgerLogBookVo = new LedgerLogBookVo();
			ledgerLogBookVo.setStoreId(newsaleVo.getStoreId());
			ledgerLogBookVo.setMobileNumber(newsaleVo.getMobileNumber());
			ledgerLogBookVo.setAmount(newsaleVo.getNetPayableAmount());
			newsaleVo.getPaymentAmountType().stream().forEach(paymentAmountType -> {

				// Checking the payment type condition
				if (paymentAmountType.getPaymentType().equals(PaymentType.PKTADVANCE)) {
					ledgerLogBookVo.setAccountType(AccountType.CREDIT);
				} else if (paymentAmountType.getPaymentType().equals(PaymentType.PKTPENDING)) {
					ledgerLogBookVo.setAccountType(AccountType.DEBIT);
				} else if (paymentAmountType.getPaymentType().equals(PaymentType.Cash)) {
					ledgerLogBookVo.setPaymentType(PaymentType.Cash);
				} else if (paymentAmountType.getPaymentType().equals(PaymentType.Card)) {
					ledgerLogBookVo.setPaymentType(PaymentType.Card);
				}
			});

			log.info("Update request to accounting: " + ledgerLogBookVo);
			rabbitTemplate.convertAndSend(config.getAccountingExchange(), config.getAccountingRK(), ledgerLogBookVo);

		}
	}

	private String updateCreditNotes(Long amount, String mobileNumber, Long storeId, String type) {

		LedgerLogBookVo ledgerLogBookRequest = new LedgerLogBookVo();

		ledgerLogBookRequest.setAmount(amount);
		ledgerLogBookRequest.setMobileNumber(mobileNumber);
		ledgerLogBookRequest.setStoreId(storeId);
		if (type.equals("PKTADVANCE")) {
			ledgerLogBookRequest.setAccountType(AccountType.CREDIT);
		} else if (type.equals("PKTPENDING")) {
			ledgerLogBookRequest.setAccountType(AccountType.DEBIT);
		}

		System.out.println("Update updateAccounting Request:" + ledgerLogBookRequest);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LedgerLogBookVo> entity = new HttpEntity<>(ledgerLogBookRequest, headers);

		ResponseEntity<?> creditNotesResponse = template.exchange(config.getUpdateAccountingDetails(), HttpMethod.POST,
				entity, GateWayResponse.class);

		System.out.println("Credit Rest Call Response:: " + creditNotesResponse.toString());

		return "Notes Updated Successfully";

	}

	// save creditnotes

	private void saveCreditNotes(Long amount, String mobileNumber, Long storeId, String returnSlipNumber,
			Long customerId) {

		LedgerLogBookVo ledgerLogBookRequest = new LedgerLogBookVo();

		ledgerLogBookRequest.setAmount(amount);
		ledgerLogBookRequest.setMobileNumber(mobileNumber);
		ledgerLogBookRequest.setStoreId(storeId);
		ledgerLogBookRequest.setAccountType(AccountType.CREDIT);
		ledgerLogBookRequest.setPaymentType(PaymentType.RTSlip);
		ledgerLogBookRequest.setReturnSlipNumber(returnSlipNumber);
		ledgerLogBookRequest.setIsReturned(Boolean.TRUE);
		ledgerLogBookRequest.setCustomerId(customerId);

		// Pass object to Rabbit queue
		rabbitTemplate.convertAndSend(config.getCreditNotesExchange(), config.getCreditnotesRK(), ledgerLogBookRequest);

	}

	@RabbitListener(queues = "newsale_queue")
	public void paymentConfirmation(PaymentDetailsVo paymentDetails) {

		NewSaleEntity entity = newSaleRepository.findByOrderNumber(paymentDetails.getReferenceNumber());

		/*
		 * NewSaleEntity orderRecord = null;
		 * 
		 * if (entity.size() != 0) { orderRecord = entity.stream().findFirst().get(); }
		 */
		if (entity != null) {

			PaymentAmountType payDetails = new PaymentAmountType();
			payDetails.setOrderId(entity);
			payDetails.setPaymentType(paymentDetails.getPayType());
			payDetails.setPaymentAmount(paymentDetails.getAmount());
			payDetails.setPaymentId(paymentDetails.getRazorPayId());
			payDetails.setRazorPayStatus(false);

			paymentAmountTypeRepository.save(payDetails);

			log.info("save payment details for order : " + entity.getOrderNumber());
		}
	}

	// Method for update the payment status in order_transaction table
	@Override
	public String paymentConfirmationFromRazorpay(String razorPayId, boolean payStatus) {

		if (payStatus) {

			PaymentAmountType payment = paymentAmountTypeRepository.findByPaymentId(razorPayId);
			payment.setRazorPayStatus(payStatus);

			paymentAmountTypeRepository.save(payment);

			log.info("update payment details for razorpay Id: " + razorPayId);

			Optional<NewSaleEntity> order = newSaleRepository.findById(payment.getOrderId().getOrderId());

			// Call method to update order items into inventory
			order.get().setStatus(OrderStatus.Completed);
			// Update order status once payment is done
			NewSaleEntity save = newSaleRepository.save(order.get());

			updateOrderItemsInInventory(order.get());
			return "successfully updated payment deatils";

		} else {
			log.info("Payment failed for razoer pay id : " + razorPayId);
			return "please do payment again";
		}

	}

	// Method for update order item into inventory
	private void updateOrderItemsInInventory(NewSaleEntity orderRecord) {

		// if (orderRecord.getDomainId() == DomainData.TE.getId()) {

		List<LineItemsEntity> lineItems = orderRecord.getDlSlip().stream().flatMap(x -> x.getLineItems().stream())
				.collect(Collectors.toList());

		// System.out.println("Line items : " +lineItems.toString());

		List<InventoryUpdateVo> updateVo = new ArrayList<>();

		lineItems.stream().forEach(x -> {

			InventoryUpdateVo vo = new InventoryUpdateVo();
			vo.setBarCode(x.getBarCode());
			vo.setLineItemId(x.getLineItemId());
			vo.setQuantity(x.getQuantity());
			vo.setStoreId(orderRecord.getStoreId());
			vo.setDomainId(orderRecord.getDomainId());
			updateVo.add(vo);
		});
		log.info("Update request to Textile: " + updateVo);
		rabbitTemplate.convertAndSend(config.getUpdateInventoryExchange(), config.getUpdateInventoryRK(), updateVo);

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
			throw new RecordNotFoundException("Barcode with number " + barCode + " is not exists",
					BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		} else {
			BarcodeVo vo = newSaleMapper.convertBarcodeEntityToVo(barcodeDetails);
			log.warn("we are fetching barcode details...");
			log.info("after getting barcode details :" + vo);
			return vo;
		}
	}

	public static String getSaltString() {
		String SALTCHARS = "1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 5) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	// Method for saving delivery slip
	@Override
	public String saveDeliverySlip(DeliverySlipVo vo) throws RecordNotFoundException {
		log.info("Save request for delivery slip : " + vo);

		DeliverySlipEntity entity = new DeliverySlipEntity();

		Random ran = new Random();
		entity.setDsNumber("ES" + LocalDate.now().getYear() + LocalDate.now().getDayOfMonth() + getSaltString());
		entity.setStatus(DSStatus.Pending);
		entity.setUserId(vo.getSalesMan());
		entity.setStoreId(vo.getStoreId());

		List<Long> lineItems = new ArrayList<>();

		if (!vo.getLineItems().isEmpty()) {

			lineItems = vo.getLineItems().stream().map(x -> x.getLineItemId()).collect(Collectors.toList());

		} else {
			log.error("Line items are not valid : " + vo.getLineItems().toString());
			throw new RecordNotFoundException("Provide line items", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}
		List<LineItemsEntity> listLineItems = lineItemRepo.findByLineItemIdInAndDsEntityIsNull(lineItems);

		if (lineItems.size() == listLineItems.size() && !listLineItems.isEmpty()) {

			DeliverySlipEntity savedEntity = dsRepo.save(entity);

			listLineItems.stream().forEach(x -> {

				x.setLastModifiedDate(LocalDateTime.now());
				x.setDsEntity(savedEntity);
				lineItemRepo.save(x);
			});

			return entity.getDsNumber();
		} else {
			log.error("Line items are not valid : " + vo.getLineItems().toString());
			throw new RecordNotFoundException("Provide valid line items",
					BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}

	}

	@Override
	public DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws RecordNotFoundException {
		log.info("Request for getting estimation slip : " + dsNumber);

		DeliverySlipEntity ds = dsRepo.findByDsNumber(dsNumber);

		if (ds != null && ds.getOrder() == null) {

			DeliverySlipVo dsVo = dsMapper.convertDsEntityToDsVo(ds);
			return dsVo;

		} else {
			log.error("Estimationslip number is not valid : " + dsNumber);
			throw new RecordNotFoundException("Provide valid DS Number", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}
	}

	@Override
	public SaleBillsVO getListOfSaleBills(SaleBillsVO saleBillsVO, Pageable pageable)
			throws RecordNotFoundException, JsonMappingException, JsonProcessingException {
		Page<NewSaleEntity> saleDetails = null;

		/*
		 * using invoice number/order number
		 */
		if (saleBillsVO.getDateFrom() == null && saleBillsVO.getDateTo() == null && saleBillsVO.getStoreId() != 0L
				&& saleBillsVO.getBillStatus() == null && saleBillsVO.getCustMobileNumber() == null
				&& saleBillsVO.getEmpId() == null && StringUtils.isNotEmpty(saleBillsVO.getInvoiceNumber())) {
			saleDetails = newSaleRepository.findByOrderNumberAndStoreId(saleBillsVO.getInvoiceNumber(),
					saleBillsVO.getStoreId(), pageable);
		}

		/*
		 * using from date
		 */
		else if (saleBillsVO.getDateFrom() != null && saleBillsVO.getDateTo() == null && saleBillsVO.getStoreId() != 0L
				&& saleBillsVO.getBillStatus() == null && saleBillsVO.getCustMobileNumber() == null
				&& saleBillsVO.getEmpId() == null && StringUtils.isEmpty(saleBillsVO.getInvoiceNumber())) {
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(saleBillsVO.getDateFrom());
			LocalDateTime fromTimeMax = DateConverters.convertToLocalDateTimeMax(saleBillsVO.getDateFrom());

			saleDetails = newSaleRepository.findByCreatedDateBetweenAndStoreId(fromTime, fromTimeMax,
					saleBillsVO.getStoreId(), pageable);
		}

		/*
		 * getting the data using between dates and bill status or customer MobileNumber
		 * or invoiceNumber
		 */
		else if (saleBillsVO.getDateFrom() != null && saleBillsVO.getDateTo() != null && saleBillsVO.getStoreId() != 0L)

		{
			LocalDateTime fromTime = DateConverters.convertLocalDateToLocalDateTime(saleBillsVO.getDateFrom());
			LocalDateTime toTime = DateConverters.convertToLocalDateTimeMax(saleBillsVO.getDateTo());
			/*
			 * using bill status
			 */
			if (saleBillsVO.getBillStatus() != null && saleBillsVO.getCustMobileNumber() == null
					&& saleBillsVO.getEmpId() == null && saleBillsVO.getInvoiceNumber() == null) {

				saleDetails = newSaleRepository.findByCreatedDateBetweenAndStatusAndStoreId(fromTime, toTime,
						saleBillsVO.getBillStatus(), saleBillsVO.getStoreId(), pageable);
			}
			/*
			 * using customer mobile number
			 */
			else if (saleBillsVO.getBillStatus() == null && StringUtils.isNotEmpty(saleBillsVO.getCustMobileNumber())
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() == null) {

				UserDetailsVo UserDetailsVO = getUserDetailsFromURM(saleBillsVO.getCustMobileNumber());

				if (UserDetailsVO != null) {

					saleDetails = newSaleRepository.findByUserIdAndStoreIdAndCreatedDateBetween(UserDetailsVO.getId(),
							saleBillsVO.getStoreId(), fromTime, toTime, pageable);

				}

			}

			/*
			 * getting the record using invoice number
			 */
			else if (saleBillsVO.getBillStatus() == null && saleBillsVO.getCustMobileNumber() == null
					&& saleBillsVO.getEmpId() == null && saleBillsVO.getInvoiceNumber() != null) {

				saleDetails = newSaleRepository.findByOrderNumberAndStoreId(saleBillsVO.getInvoiceNumber(),
						saleBillsVO.getStoreId(), pageable);
			}
			/*
			 * using barcode
			 */
			else if (saleBillsVO.getBillStatus() == null && StringUtils.isEmpty(saleBillsVO.getCustMobileNumber())
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() == null
					&& StringUtils.isNotEmpty(saleBillsVO.getBarcode())
					&& StringUtils.isEmpty(saleBillsVO.getDsNumber())) {
				saleDetails = newSaleRepository.findByDlSlipLineItemsBarCode(saleBillsVO.getBarcode(), pageable);
			}
			/*
			 * using ds number
			 */
			else if (saleBillsVO.getBillStatus() == null && StringUtils.isEmpty(saleBillsVO.getCustMobileNumber())
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() == null
					&& StringUtils.isEmpty(saleBillsVO.getBarcode())
					&& StringUtils.isNotEmpty(saleBillsVO.getDsNumber())) {
				saleDetails = newSaleRepository.findByDlSlipDsNumber(saleBillsVO.getDsNumber(), pageable);

			}
			/*
			 * getting the record using empId
			 */
			else if (saleBillsVO.getBillStatus() == null && saleBillsVO.getCustMobileNumber() == null
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() != null) {
				saleDetails = newSaleRepository.findByCreatedByAndStoreIdAndCreatedDateBetween(saleBillsVO.getEmpId(),
						saleBillsVO.getStoreId(), fromTime, toTime, pageable);

			} else
				/*
				 * using dates only
				 */
				saleDetails = newSaleRepository.findByCreatedDateBetweenAndStoreIdOrderByCreatedDateDesc(fromTime,
						toTime, saleBillsVO.getStoreId(), pageable);

		}

		else if (saleBillsVO.getDateFrom() == null && saleBillsVO.getDateTo() == null
				&& saleBillsVO.getStoreId() != 0L) {

			/*
			 * getting the record using empId without dates
			 */
			if (saleBillsVO.getBillStatus() == null && saleBillsVO.getCustMobileNumber() == null
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() != null) {
				saleDetails = newSaleRepository.findByCreatedByAndStoreId(saleBillsVO.getEmpId(),
						saleBillsVO.getStoreId(), pageable);

			}
			/*
			 * using barcode
			 */
			else if (saleBillsVO.getBillStatus() == null && StringUtils.isEmpty(saleBillsVO.getCustMobileNumber())
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() == null
					&& StringUtils.isNotEmpty(saleBillsVO.getBarcode())
					&& StringUtils.isEmpty(saleBillsVO.getDsNumber())) {
				saleDetails = newSaleRepository.findByDlSlipLineItemsBarCode(saleBillsVO.getBarcode(), pageable);
			}
			/*
			 * using ds number
			 */
			else if (saleBillsVO.getBillStatus() == null && StringUtils.isEmpty(saleBillsVO.getCustMobileNumber())
					&& saleBillsVO.getInvoiceNumber() == null && saleBillsVO.getEmpId() == null
					&& StringUtils.isEmpty(saleBillsVO.getBarcode())
					&& StringUtils.isNotEmpty(saleBillsVO.getDsNumber())) {
				saleDetails = newSaleRepository.findByDlSlipDsNumber(saleBillsVO.getDsNumber(), pageable);

			}
		}

		if (saleDetails == null) {

			Page.empty();

		}
		// ListOfSaleBillsVo listOfSaleBillsVo = new ListOfSaleBillsVo();
		SaleBillsVO listOfSaleBillsVo = saleMapToVo(saleDetails);
		// listOfSaleBillsVo.setNewSale(saleDetails);

		return listOfSaleBillsVo;
	}

	private SaleBillsVO saleMapToVo(Page<NewSaleEntity> newSaleEntityList) {
		SaleBillsVO listOfSaleBillsVO = newSaleMapper.converEntityToVo(newSaleEntityList);
		listOfSaleBillsVO.getNewSale().map(x -> map(x));
		return listOfSaleBillsVO;
	}

	private NewSaleVo map(NewSaleVo x) {
		List<UserDetailsVo> userDetailsVo = new ArrayList<>();
		try {
			userDetailsVo = getUsersForGivenId(Arrays.asList(x.getUserId()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		if (userDetailsVo != null) {
			userDetailsVo.stream().forEach(userDetails -> {
				x.setCustomerName(userDetails.getUserName());
				x.setMobileNumber(userDetails.getPhoneNumber());
			});
		}
		return x;
	}

	private List<UserDetailsVo> getUsersForGivenId(List<Long> userIds) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getUserIdsUrl())).build().encode().toUri();
		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(userIds, headers);
		ResponseEntity<?> usersResponse = template.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);
		ObjectMapper mapper = new ObjectMapper();
		GateWayResponse<?> gatewayResponse = mapper.convertValue(usersResponse.getBody(), GateWayResponse.class);
		List<UserDetailsVo> bvo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<UserDetailsVo>>() {
				});
		return bvo;

	}

	public HsnDetailsVo getHsnDetails(double netAmt) {
		log.debug("debugging getHsnDetails:" + netAmt);
		List<HsnDetailsVo> vo = hsnService.getHsn();
		if (vo == null) {
			log.error("Record not found");
			new RecordNotFoundException("Record not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}
		TaxVo tvo = new TaxVo();
		HsnDetailsVo hvo = new HsnDetailsVo();

		vo.stream().forEach(x -> {

			x.getSlabVos().stream().forEach(a -> {

				if (a.getPriceFrom() <= netAmt && netAmt <= a.getPriceTo()) {

					tvo.setTaxableAmount((float) netAmt / (1 + (a.getTaxVo().getCess() / 100)));
					tvo.setGst(a.getTaxVo().getCess());
					tvo.setTaxLabel(a.getTaxVo().getTaxLabel());
					tvo.setSgst((float) ((netAmt - tvo.getTaxableAmount()) / 2));
					tvo.setCgst((float) ((netAmt - tvo.getTaxableAmount()) / 2));
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
	public ListOfDeliverySlipVo getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo, Pageable pageable)
			throws RecordNotFoundException {
		log.debug("deugging getlistofDeliverySlips:" + listOfDeliverySlipVo);

		Page<DeliverySlipEntity> dsDetails = null;
		LocalDateTime createdDateTo;
		LocalDateTime createdDatefrom = DateConverters
				.convertLocalDateToLocalDateTime(listOfDeliverySlipVo.getDateFrom());
		if (listOfDeliverySlipVo.getDateTo() != null) {
			createdDateTo = DateConverters.convertToLocalDateTimeMax(listOfDeliverySlipVo.getDateTo());
		} else {
			createdDateTo = DateConverters.convertToLocalDateTimeMax(listOfDeliverySlipVo.getDateFrom());

		}
		try {

			/*
			 * getting the record using all fields
			 */
			if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
					&& StringUtils.isNotEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() != null
					&& StringUtils.isNotEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				List<LineItemsEntity> bar = lineItemRepo.findByBarCodeAndStoreId(listOfDeliverySlipVo.getBarcode(),
						listOfDeliverySlipVo.getStoreId());

				if (bar != null) {
					List<Long> dsIds = bar.stream().map(barcode -> barcode.getDsEntity().getDsId())
							.collect(Collectors.toList());
					/*
					 * bar.stream().forEach(b -> { DeliverySlipEntity dsEntity = new
					 * DeliverySlipEntity();
					 */
					dsDetails = dsRepo
							.findByCreatedDateBetweenAndDsIdInAndDsNumberAndStatusAndStoreIdOrderByCreatedDateDesc(
									createdDatefrom, createdDateTo, dsIds, listOfDeliverySlipVo.getDsNumber(),
									listOfDeliverySlipVo.getStatus(), listOfDeliverySlipVo.getStoreId(), pageable);
					// dsDetails.add(dsEntity);
					// });

				}

			}
			/*
			 * getting the record using barcode
			 */

			if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() == null
					&& StringUtils.isNotEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				List<LineItemsEntity> bar = lineItemRepo.findByBarCodeAndStoreId(listOfDeliverySlipVo.getBarcode(),
						listOfDeliverySlipVo.getStoreId());

				if (bar != null) {
					List<Long> dsIds = bar.stream().map(barcode -> barcode.getDsEntity().getDsId())
							.collect(Collectors.toList());

					/*
					 * bar.stream().forEach(b -> { DeliverySlipEntity dentity = new
					 * DeliverySlipEntity();
					 */
					dsDetails = dsRepo.findByDsIdInAndStoreIdOrderByCreatedDateDesc(dsIds,
							listOfDeliverySlipVo.getStoreId(), pageable);
					// dsDetails.add(dentity);
					// });

				}

			}
			/*
			 * getting the record using barcode and dates
			 */

			if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() == null
					&& StringUtils.isNotEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				List<LineItemsEntity> bar = lineItemRepo.findByBarCodeAndStoreId(listOfDeliverySlipVo.getBarcode(),
						listOfDeliverySlipVo.getStoreId());

				if (!CollectionUtils.isEmpty(bar)) {
					List<DeliverySlipEntity> den = bar.stream().map(d -> d.getDsEntity()).filter(n -> n != null)
							.collect(Collectors.toList());

//					den.stream().forEach(b -> {
//						DeliverySlipEntity dentity = new DeliverySlipEntity();
					List<Long> dsIds = den.stream().map(dsSlip -> dsSlip.getDsId()).collect(Collectors.toList());

					dsDetails = dsRepo.findByCreatedDateBetweenAndDsIdInAndStoreIdOrderByCreatedDateDesc(
							createdDatefrom, createdDateTo, dsIds, listOfDeliverySlipVo.getStoreId(), pageable);
					/*
					 * if (dentity != null) { dsDetails.add(dentity); } });
					 */

				}
			}
			/*
			 * getting the record using dsNumber and dates
			 */
			if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
					&& StringUtils.isNotEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				dsDetails = dsRepo.findByCreatedDateBetweenAndDsNumberAndStoreIdOrderByCreatedDateDesc(createdDatefrom,
						createdDateTo, listOfDeliverySlipVo.getDsNumber(), listOfDeliverySlipVo.getStoreId(), pageable);

			}
			/*
			 * getting the record using status and dates
			 */
			if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() != null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				dsDetails = dsRepo.findByCreatedDateBetweenAndStatusAndStoreIdOrderByCreatedDateDesc(createdDatefrom,
						createdDateTo, listOfDeliverySlipVo.getStatus(), listOfDeliverySlipVo.getStoreId(), pageable);

			}

			/*
			 * getting the record using dsNumber
			 */

			if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
					&& StringUtils.isNotEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				// List<String> dlsList = listOfDeliverySlipVo.stream().map(x ->
				// x.getDsNumber()).collect(Collectors.toList());
				List<String> dsList = new ArrayList<>();
				dsList.add(listOfDeliverySlipVo.getDsNumber());
				dsDetails = dsRepo.findByDsNumberInAndStoreIdOrderByCreatedDateDesc(dsList,
						listOfDeliverySlipVo.getStoreId(), pageable);

			}

			/*
			 * getting the record using status
			 */

			if (listOfDeliverySlipVo.getDateFrom() == null && listOfDeliverySlipVo.getDateTo() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() != null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				dsDetails = dsRepo.findByStatusAndStoreIdOrderByCreatedDateDesc(listOfDeliverySlipVo.getStatus(),
						listOfDeliverySlipVo.getStoreId(), pageable);

			}
			/*
			 * getting the record using dates
			 */
			if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() != null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				dsDetails = dsRepo.findByCreatedDateBetweenAndStoreIdOrderByCreatedDateDesc(createdDatefrom,
						createdDateTo, listOfDeliverySlipVo.getStoreId(), pageable);

			}
			if (listOfDeliverySlipVo.getDateFrom() != null && listOfDeliverySlipVo.getDateTo() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getDsNumber())
					&& listOfDeliverySlipVo.getStatus() == null
					&& StringUtils.isEmpty(listOfDeliverySlipVo.getBarcode())
					&& ObjectUtils.isNotEmpty(listOfDeliverySlipVo.getStoreId())) {

				dsDetails = dsRepo.findByCreatedDateBetweenAndStoreIdOrderByCreatedDateDesc(createdDatefrom,
						createdDateTo, listOfDeliverySlipVo.getStoreId(), pageable);

			}
			ListOfDeliverySlipVo lvo = null;
			if (dsDetails.hasContent()) {
				lvo = newSaleMapper.convertListDSToVo(dsDetails);

				log.warn("we are testing is fetching list of deivery slips");
				log.info("after getting list of delivery slips :" + lvo);
				return lvo;

			} else {
				throw new RecordNotFoundException("record not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
			}

		} catch (Exception ex) {

			throw new RecordNotFoundException("record not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}

	}

	@Override
	public List<DeliverySlipVo> posDayClose(Long storeId) {
		log.debug(" debugging posDayClose");
		List<DeliverySlipVo> dvo = new ArrayList<>();

		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreatedDateAndStoreId(DSStatus.Pending,
				LocalDateTime.now(), storeId);
		if (!DsList.isEmpty()) {

			List<DeliverySlipVo> dsVo = new ArrayList<>();
			DsList.stream().forEach(d -> {
				DeliverySlipVo dVo = new DeliverySlipVo();
				dVo.setDsNumber(d.getDsNumber());
				dVo.setDsId(d.getDsId());
				dVo.setLastModifiedDate(d.getLastModifiedDate());
				dVo.setCreatedDate(d.getCreatedDate());
				dVo.setStatus(d.getStatus());
				dVo.setSalesMan(d.getUserId());
				dsVo.add(dVo);

			});

			log.info("successfully we can close the day of pos " + " uncleared delivery Slips count :" + DsList.size());
			return dsVo;
		} else
			return dvo;
	}

	@Override
	public String posClose(Long storeId) {

		List<DeliverySlipEntity> DsList = dsRepo.findByStatusAndCreatedDateAndStoreId(DSStatus.Pending,
				LocalDateTime.now(), storeId);
		if (DsList != null && !DsList.isEmpty()) {
			DsList.stream().forEach(d -> {

				d.setStatus(DSStatus.Cancelled);
				dsRepo.save(d);

			});

			return "successFully updated deliverySlips";
		} else
			return "there is no  pending delivery slips with this storeId:" + storeId;

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
				throw new RecordNotFoundException("invoice is not present",
						BusinessException.RECORD_NOT_FOUND_STATUSCODE);
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
	public List<ReturnSlipVo> getInvoicDetails(InvoiceRequestVo vo) throws RecordNotFoundException {

		log.debug(" debugging getInvoicDetails:" + vo);
		// NewSaleList newSaleList1 = new NewSaleList();
		// List<NewSaleVo> newSaleList = new ArrayList<>();

		if (null != vo.getInvoiceNo() && !vo.getInvoiceNo().isEmpty()) {
			if (vo.getDomianId() != 0) {
				List<ReturnSlipVo> rtSlipVoList = new ArrayList<>();
				NewSaleEntity newSaleEntity = newSaleRepository.findByOrderNumber(vo.getInvoiceNo());
				if (newSaleEntity != null) {

					if (vo.getDomianId() == DomainData.TE.getId()) {
						newSaleEntity.getDlSlip().stream().forEach(dSlip -> {
							dSlip.getLineItems().stream().forEach(lItem -> {
								ReturnSlipVo rtSlipVo = new ReturnSlipVo();
								rtSlipVo.setBarcode(lItem.getBarCode());
								rtSlipVo.setNetValue(lItem.getNetValue());
								rtSlipVo.setQuantity(lItem.getQuantity());
								rtSlipVoList.add(rtSlipVo);
							});
						});
					}
					if (vo.getDomianId() == DomainData.RE.getId()) {

						newSaleEntity.getLineItemsRe().stream().forEach(lItem -> {
							ReturnSlipVo rtSlipVo = new ReturnSlipVo();
							rtSlipVo.setBarcode(lItem.getBarCode());
							rtSlipVo.setNetValue(lItem.getNetValue());
							rtSlipVo.setQuantity(lItem.getQuantity());
							rtSlipVoList.add(rtSlipVo);
						});

					}

					return rtSlipVoList;
				} else {
					throw new RuntimeException("Invoice details not found with this OrderId : " + vo.getInvoiceNo());
				}

			} else {
				throw new RuntimeException("DomianId should not be null");
			}
		}

		if (null != vo.getMobileNo() && !vo.getMobileNo().isEmpty()) {
			if (0 == vo.getDomianId()) {
				throw new RuntimeException("DomianId should not be null");
			}
			// get customer details from URM
			UserDetailsVo customer = getUserDetailsFromURM(vo.getMobileNo());

			if (customer != null) {
				List<ReturnSlipVo> rtSlipVoList = new ArrayList<>();

				List<NewSaleEntity> newSaleEntity = newSaleRepository.findByUserId(customer.getId());

				if (!CollectionUtils.isEmpty(newSaleEntity)) {

					newSaleEntity.stream().forEach(a -> {
						if (vo.getDomianId() == DomainData.TE.getId()) {
							a.getDlSlip().stream().forEach(dSlip -> {
								dSlip.getLineItems().stream().forEach(lItem -> {
									ReturnSlipVo rtSlipVo = new ReturnSlipVo();
									rtSlipVo.setBarcode(lItem.getBarCode());
									rtSlipVo.setNetValue(lItem.getNetValue());
									rtSlipVo.setQuantity(lItem.getQuantity());
									rtSlipVoList.add(rtSlipVo);
								});
							});
						}
						if (vo.getDomianId() == DomainData.RE.getId()) {
							newSaleEntity.stream().forEach(newSale -> {
								newSale.getLineItemsRe().stream().forEach(lItem -> {
									ReturnSlipVo rtSlipVo = new ReturnSlipVo();
									rtSlipVo.setBarcode(lItem.getBarCode());
									rtSlipVo.setNetValue(lItem.getNetValue());
									rtSlipVo.setQuantity(lItem.getQuantity());
									rtSlipVoList.add(rtSlipVo);
								});
							});
						}
					});
					return rtSlipVoList;
				} else {
					throw new RuntimeException(
							"No Invoice details not found with this Mobile number : " + vo.getMobileNo());
				}

			} else {
				throw new RuntimeException("Customer details not found with this mobile number : " + vo.getMobileNo());
			}
		}
		log.error("No records found with your inputs");
		throw new RecordNotFoundException("No records found with your inputs",
				BusinessException.RECORD_NOT_FOUND_STATUSCODE);
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
		log.info("Getting save request for saving giftvoucher : " + vo);

		// Check condition for Duplicate GiftVoucher Numbers
		Optional<GiftVoucherEntity> gvEntity = gvRepo.findByGvNumber(vo.getGvNumber());

		if (!gvEntity.isPresent()) {

			GiftVoucherEntity entity = new GiftVoucherEntity();
			BeanUtils.copyProperties(vo, entity);
			entity.setCreationDate(LocalDate.now());
			entity.setIsActivated(Boolean.FALSE);
			GiftVoucherEntity savedGf = gvRepo.save(entity);
			log.info("Gift voucher saved successfully : " + savedGf);
			return "Gift voucher saved succesfully..";
		} else {
			log.error("Given Giftvoucher number is already in records : " + gvEntity.get().getGvNumber());
			throw new DuplicateRecordException(
					"Given Giftvoucher number is already in records.." + gvEntity.get().getGvNumber());
		}
	}

	// Method for getting Gift voucher details by Gv Number
	@Override
	public GiftVoucherVo getGiftVoucher(String gvNumber, Long clientId) throws InvalidInputException {

		Optional<GiftVoucherEntity> gvEntity = gvRepo.findByGvNumberAndClientId(gvNumber, clientId);

		if (gvEntity.isPresent() && gvEntity.get().getIsActivated()

				&& gvEntity.get().getFromDate().isBefore(LocalDate.now())
				|| gvEntity.get().getFromDate().isEqual(LocalDate.now())
						&& gvEntity.get().getToDate().isAfter(LocalDate.now())
				|| gvEntity.get().getToDate().isEqual(LocalDate.now())) {

			GiftVoucherVo vo = new GiftVoucherVo();
			BeanUtils.copyProperties(gvEntity.get(), vo);
			return vo;
		} else {
			log.error("Entered Invalid Gift Voucher number : " + gvNumber);
			throw new InvalidInputException("please enter valid Gift Voucher number.");
		}
	}

	// Method for tagging Gift voucher to Customer
	@Override
	public String tagCustomerToGv(Long userId, Long gvId) throws InvalidInputException, DataNotFoundException {

		Optional<GiftVoucherEntity> gv = gvRepo.findById(gvId);

		return "Gift vocher tagged successfully  " + gv.get().getGvNumber();
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
	public List<LineItemVo> getBarcodes(List<String> barCode/* , Long domainId */) throws RecordNotFoundException {
		log.debug("deugging getBarcodeDetails" + barCode);
		List<LineItemVo> vo = new ArrayList<LineItemVo>();

		// if (domainId == 1) {
		List<LineItemsEntity> barcodeDetails = lineItemRepo.findByBarCodeIn(barCode);
		vo = newSaleMapper.convertBarcodesEntityToVo(barcodeDetails);
		/*
		 * } else { List<LineItemsReEntity> barcodeDetails1 =
		 * lineItemReRepo.findByBarCodeIn(barCode); vo =
		 * newSaleMapper.convertBarcodesReEntityToVo(barcodeDetails1); }
		 */
		return vo;
	}

	/////////////

	@Override
	public SaleReportVo getSaleReport(SaleReportVo srvo) throws RecordNotFoundException {
		SaleReportVo slr = new SaleReportVo();
		List<NewSaleEntity> saleDetails = new ArrayList<>();
		LocalDateTime createdDateTo;
		LocalDateTime createdDatefrom = DateConverters.convertLocalDateToLocalDateTime(srvo.getDateFrom());
		if (srvo.getDateTo() != null) {
			createdDateTo = DateConverters.convertToLocalDateTimeMax(srvo.getDateTo());
		} else {
			createdDateTo = DateConverters.convertToLocalDateTimeMax(srvo.getDateFrom());

		}
		if (srvo.getDateFrom() != null && srvo.getDateTo() != null && ObjectUtils.isNotEmpty(srvo.getStoreId())) {
			saleDetails = newSaleRepository.findByCreatedDateBetweenAndStoreId(createdDatefrom, createdDateTo,
					srvo.getStoreId());
		} else if (srvo.getDateFrom() != null && srvo.getDateTo() == null
				&& ObjectUtils.isNotEmpty(srvo.getStoreId())) {

			saleDetails = newSaleRepository.findByCreatedDateBetweenAndStoreId(createdDatefrom, createdDateTo,
					srvo.getStoreId());
		}

		if (saleDetails.isEmpty()) {
			throw new RecordNotFoundException(BusinessException.RNF_DESCRIPTION,
					BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		} else {

			SalesSummeryVo salesSummery = newSaleMapper.convertlistSaleReportEntityToVo(saleDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			ListOfReturnSlipsVo rvo = new ListOfReturnSlipsVo();
			rvo.setDateFrom(srvo.getDateFrom());
			rvo.setDateTo(srvo.getDateTo());
			rvo.setStoreId(srvo.getStoreId());

			Pageable pageable = null;

			Page<ListOfReturnSlipsVo> vo = returnSlipServiceImpl.getListOfReturnSlips(rvo, pageable);

			Long rAmount = vo.stream().mapToLong(a -> a.getAmount()).sum();
			ReturnSummeryVo retunVo = new ReturnSummeryVo();

			List<LineItemVo> barVoList = new ArrayList<LineItemVo>();
			vo.stream().forEach(b -> {

				List<TaggedItems> tgItems = b.getBarcodes();

				List<String> barcodes = tgItems.stream().map(x -> x.getBarCode()).collect(Collectors.toList());

				try {
					List<LineItemVo> barVo = getBarcodes(barcodes/* , b.getDomainId() */);
					barVo.stream().forEach(r -> {

						barVoList.add(r);

					});

				} catch (RecordNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			System.out.println(vo);

			// HsnDetailsVo hsnDetails1 = getHsnDetails(rAmount);
			List<Long> result = barVoList.stream().map(num -> num.getDiscount()).filter(n -> n != null)
					.collect(Collectors.toList());
			retunVo.setTotalDiscount(result.stream().mapToLong(d -> d).sum());
			retunVo.setTotalMrp(barVoList.stream().mapToLong(a -> a.getItemPrice() * a.getQuantity()).sum());
			retunVo.setBillValue(rAmount);
		List<LineItemVo> taxValue =	barVoList.stream().filter(lineitem->lineitem.getTaxValue()!=null).collect(Collectors.toList());
			retunVo.setTotalTaxAmount(taxValue.stream().mapToLong(a -> a.getTaxValue()).sum());
			
			List<LineItemVo> lineitemVo1=	barVoList.stream().filter(lineitem->lineitem.getCgst()!=null).collect(Collectors.toList());
			List<LineItemVo> lineitemVo2=	barVoList.stream().filter(lineitem->lineitem.getSgst()!=null).collect(Collectors.toList());
			List<LineItemVo> lineitemVo3=	barVoList.stream().filter(lineitem->lineitem.getIgst()!=null).collect(Collectors.toList());
			List<LineItemVo> lineitemVo4=	barVoList.stream().filter(lineitem->lineitem.getCess()!=null).collect(Collectors.toList());

			Double TotalSgst = lineitemVo1.stream().mapToDouble(a -> a.getSgst()).sum();
			Double TotalCgst = lineitemVo2.stream().mapToDouble(a -> a.getCgst()).sum();
			Double TotalIgst = lineitemVo3.stream().mapToDouble(a -> a.getIgst()).sum();
			Double TotalCess = lineitemVo4.stream().mapToDouble(a -> a.getCess()).sum();

			retunVo.setTotalSgst(TotalSgst.floatValue());
			retunVo.setTotalCgst(TotalCgst.floatValue());
			retunVo.setTotalIgst(TotalIgst.floatValue());
			retunVo.setTotalCess(TotalCess.floatValue());
			retunVo.setStoreId(srvo.getStoreId());

			salesSummery.setStoreId(srvo.getStoreId());
			slr.setRetunSummery(retunVo);
			slr.setSalesSummery(salesSummery);
			slr.setBillValue(slr.getSalesSummery().getBillValue() - slr.getRetunSummery().getBillValue());

			slr.setTotalDiscount(slr.getSalesSummery().getTotalDiscount() - slr.getRetunSummery().getTotalDiscount());
			slr.setTotalMrp(slr.getSalesSummery().getTotalMrp() - slr.getRetunSummery().getTotalMrp());

			return slr;
		}

	}

	// Method for saving Line items
	@Override
	public List<Long> saveLineItems(List<LineItemVo> lineItems/* , Long domainId */) throws InvalidInputException {
		try {
			// if (domainId == DomainData.TE.getId()) {

			List<LineItemsEntity> list = new ArrayList<>();

			lineItems.stream().forEach(lineItem -> {

				LineItemsEntity lineEntity = new LineItemsEntity();

				lineEntity.setBarCode(lineItem.getBarCode());
				lineEntity.setQuantity(lineItem.getQuantity());
				lineEntity.setDiscount(lineItem.getDiscount());
				lineEntity.setGrossValue(lineItem.getNetValue());
				lineEntity.setItemPrice(lineItem.getItemPrice());
				lineEntity.setSection(lineItem.getSection());
				lineEntity.setSubSection(lineItem.getSubSection());
				lineEntity.setDivision(lineItem.getDivision());
				lineEntity.setHsnCode(lineItem.getHsnCode());
				lineEntity.setActualValue(lineItem.getActualValue());
				lineEntity.setTaxValue(lineItem.getTaxValue());
				lineEntity.setCgst(lineItem.getCgst());
				lineEntity.setSgst(lineItem.getSgst());
				lineEntity.setIgst(lineItem.getIgst());
				lineEntity.setCess(lineItem.getCess());
				lineEntity.setStoreId(lineItem.getStoreId());
				lineEntity.setSalesManId(lineItem.getSalesManId());

				// GrossValue is multiple of net value of product and quantity
				lineEntity.setNetValue(lineEntity.getGrossValue() - lineItem.getDiscount());

				lineEntity.setCreatedDate(LocalDateTime.now());
				lineEntity.setLastModifiedDate(LocalDateTime.now());

				list.add(lineEntity);
			});
			List<LineItemsEntity> saved = lineItemRepo.saveAll(list);
			log.info("successfully saved line items " + saved);
			return saved.stream().map(x -> x.getLineItemId()).collect(Collectors.toList());

		} catch (InvalidInputException e) {
			log.error("Getting exception while saving Line item.." + lineItems.toString());
			throw new InvalidInputException(e.getMsg());
		}
	}

	public UserDetailsVo getUserDetailsFromURM(@RequestParam String mobileNumber) {

		GetUserRequestVo userRequestVo = new GetUserRequestVo();
		String phoneNumber = "+" + mobileNumber.trim();
		userRequestVo.setPhoneNo(phoneNumber);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GetUserRequestVo> entity = new HttpEntity<>(userRequestVo, headers);

		ResponseEntity<?> returnSlipListResponse = template.exchange(
				config.getMobileNumberUrl() + "?mobileNumber=" + phoneNumber, HttpMethod.GET, entity,
				GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		UserDetailsVo UserDetailsVO = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<UserDetailsVo>() {
				});

		return UserDetailsVO;

	}

	// Method for modifying Line Item
	@Override
	public String editLineItem(LineItemVo lineItem) throws RecordNotFoundException {

		// if (lineItem.getDomainId() == DomainData.TE.getId()) {

		LineItemsEntity line = lineItemRepo.findByLineItemId(lineItem.getLineItemId());

		if (line != null && line.getDsEntity() == null) {

			line.setLineItemId(lineItem.getLineItemId());
			line.setBarCode(lineItem.getBarCode());
			line.setQuantity(lineItem.getQuantity());
			line.setDiscount(lineItem.getDiscount());
			line.setNetValue(lineItem.getNetValue());
			line.setItemPrice(lineItem.getItemPrice());
			line.setTaxValue(lineItem.getTaxValue());
			line.setCgst(lineItem.getCgst());
			line.setSgst(lineItem.getSgst());
			line.setStoreId(lineItem.getStoreId());

			// GrossValue is multiple of net value of product and quantity
			line.setGrossValue(lineItem.getNetValue() * lineItem.getQuantity());

			line.setCreatedDate(LocalDateTime.now());
			line.setLastModifiedDate(LocalDateTime.now());
			LineItemsEntity saved = lineItemRepo.save(line);
			log.info("Successfully modified line item : " + line);

		}
		return "Successfully modified Line Item.";
	}

	// Method for getting line item by using Bar code
	public List<LineItemVo> getLineItemByBarcode(String barCode/* , Long domainId */) throws RecordNotFoundException {

		// if (domainId == DomainData.TE.getId()) {
		List<LineItemsEntity> lineItem = lineItemRepo.findByBarCode(barCode);
		List<LineItemVo> lvo = new ArrayList<>();
		if (lineItem != null) {
			lineItem.stream().forEach(b -> {

				LineItemVo vo = new LineItemVo();
				BeanUtils.copyProperties(b, vo);
				lvo.add(vo);

			});
			return lvo;

		} else {
			log.info("Provide valid barcode for getting line item : " + barCode);
			throw new RecordNotFoundException("provide valide barcode", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}

	}

	// Method for deleting existing line item by using barcode
	@Override
	public String deleteLineItem(String barCode/* , Long domainId */) throws RecordNotFoundException {

		// if (domainId == DomainData.TE.getId()) {
		List<LineItemsEntity> lineItem = lineItemRepo.findByBarCode(barCode);
		if (lineItem != null) {

			lineItem.stream().forEach(a -> {
				lineItemRepo.delete(a);

			});
			return "Successfully deleted";

		} else {
			throw new RecordNotFoundException("provide valid barcode", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}

	}

	@Override
	public String getTaggedCustomerForInvoice(String mobileNo, String invoiceNo) {

		return null;
	}

	@Override
	public void deleteDeliverySlipDetails(String dsNumber) {
		DeliverySlipEntity dsEntity = dsRepo.findByDsNumber(dsNumber);
		if (dsEntity != null && dsEntity.getOrder() == null) {
			dsEntity.setStatus(DSStatus.Cancelled);
			dsRepo.save(dsEntity);
		}
	}

	// Method for fetching list of Gift vouchers
	@Override
	public List<GiftVoucherVo> getListOfGiftvouchers() throws RecordNotFoundException {

		try {
			List<GiftVoucherEntity> listOfGvs = gvRepo.findAll();
			if (!listOfGvs.isEmpty()) {
				List<GiftVoucherVo> listVo = new ArrayList<>();

				listOfGvs.stream().forEach(x -> {
					GiftVoucherVo vo = new GiftVoucherVo();

					BeanUtils.copyProperties(x, vo);
					listVo.add(vo);
				});
				return listVo;
			} else {
				throw new RecordNotFoundException("No records found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
			}
		} catch (InvalidInputException iie) {
			throw new RecordNotFoundException("Exception while fetching records",
					BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}
	}

	// Method for fetching gift vouchers by using UserId
	@Override
	public List<GiftVoucherVo> getGvByUserId(Long userId) throws RecordNotFoundException {

//		List<GiftVoucherEntity> gvsList = gvRepo.findByUserIdAndExpiryDateGreaterThanEqual(userId, LocalDate.now());
//		List<GiftVoucherVo> gvVosList = new ArrayList<>();

//		if (!gvsList.isEmpty()) {
//			gvsList.stream().forEach(x -> {
//				GiftVoucherVo vo = new GiftVoucherVo();
//				BeanUtils.copyProperties(x, vo);
//				gvVosList.add(vo);
//			});
//		} else {
//			throw new RecordNotFoundException("No records found with user id : " + userId);
//		}
		return null;
	}

	// Method for fetching invoice details by using order number
	@Override
	public NewSaleVo getInvoiceDetails(String orderNumber) throws RecordNotFoundException {

		log.info("Request for fetching invoice details : " + orderNumber);
		NewSaleEntity orderRecord = newSaleRepository.findByOrderNumber(orderNumber);
		if (orderRecord != null) {
			// NewSaleEntity order = orderRecord.stream().findFirst().get();
			NewSaleVo result = newSaleMapper.convertNewSaleDtoToVo(orderRecord);
			return result;
		} else {
			throw new RecordNotFoundException("Provide valid order number",
					BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}
	}

	@Override
	public String saveLoyaltyPoints(LoyalityPointsVo loyalityVo) {

		LoyalityPointsEntity entity = newSaleMapper.convertLoyaltyVoToEntity(loyalityVo);
		LoyalityPointsEntity save = loyalityRepo.save(entity);

		return "Loyality Points Saved Successfully";
	}

	@Override
	public LoyalityPointsVo getLoyaltyPointsByLoyaltyId(Long loyaltyId) throws RecordNotFoundException {

		LoyalityPointsEntity loyalityIds = loyalityRepo.findByLoyaltyId(loyaltyId);

		if (loyalityIds == null) {

			throw new RecordNotFoundException("Records not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);

		} else {

			LoyalityPointsVo result = newSaleMapper.convertLoyaltyEntityToVo(loyalityIds);

			return result;

		}

	}

	@Override
	public List<LoyalityPointsVo> getAllLoyaltyPoints() throws RecordNotFoundException {

		List<LoyalityPointsEntity> entity = loyalityRepo.findAll();

		if (entity == null) {
			throw new RecordNotFoundException("Records not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		} else {

			List<LoyalityPointsVo> result = newSaleMapper.convertLoyaltyEntityToVo(entity);

			return result;
		}

	}

	@Override
	public LoyalityPointsVo getLoyaltyPointsByUserId(Long userId) throws RecordNotFoundException {

		LoyalityPointsEntity userIdValue = loyalityRepo.findByUserId(userId);

		if (userIdValue == null) {

			throw new RecordNotFoundException("Records not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);

		} else {

			LoyalityPointsVo result = newSaleMapper.convertLoyaltyEntityToVo(userIdValue);

			return result;

		}

	}

	@Override
	public List<LoyalityPointsVo> searchLoyaltyPoints(SearchLoyaltyPointsVo vo) throws RecordNotFoundException {

		List<LoyalityPointsEntity> loyaltylist = new ArrayList<>();

		if (vo.getInvoiceNumber() != null && vo.getMobileNumber() != null) {

			loyaltylist = loyalityRepo.findByInvoiceNumberAndMobileNumber(vo.getInvoiceNumber(), vo.getMobileNumber());

		} else if (vo.getInvoiceNumber() != null && vo.getMobileNumber() == null) {

			loyaltylist = loyalityRepo.findByInvoiceNumber(vo.getInvoiceNumber());

		} else if (vo.getInvoiceNumber() == null && vo.getMobileNumber() != null) {
			loyaltylist = loyalityRepo.findByMobileNumber(vo.getMobileNumber());
		}

		if (loyaltylist.isEmpty()) {
			throw new RecordNotFoundException("Records not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}

		List<LoyalityPointsVo> dataList = newSaleMapper.convertLoyaltyEntityToVo(loyaltylist);

		return dataList;
	}

	// Method for change flag of Giftvoucher
	@Override
	public String activateGiftvoucher(List<String> gvsList, Boolean flag) throws RecordNotFoundException {

		List<GiftVoucherEntity> gvEntities = gvRepo.findByGvNumberIn(gvsList);

		if (!gvEntities.isEmpty()) {

			gvEntities.stream().forEach(x -> {

				x.setIsActivated(flag);

				gvRepo.save(x);
			});
			return "success";
		} else {
			throw new RecordNotFoundException("Record not found", BusinessException.RECORD_NOT_FOUND_STATUSCODE);
		}
	}

	@Override
	public List<GiftVoucherVo> giftVoucherSearching(GiftVoucherSearchVo searchVo) {

		List<GiftVoucherEntity> giftVoucherEntities = new ArrayList<>();
		List<GiftVoucherVo> giftVoucherVoList = new ArrayList<>();

		if (searchVo.getFromDate() != null && searchVo.getToDate() != null && searchVo.getGvNumber() != null
				&& searchVo.getClientId() != null) {

			giftVoucherEntities = gvRepo.findByFromDateAndToDateAndGvNumberAndClientId(searchVo.getFromDate(),
					searchVo.getToDate(), searchVo.getGvNumber(), searchVo.getClientId());

		} else if (searchVo.getFromDate() != null && searchVo.getToDate() != null && searchVo.getClientId() != null) {

			giftVoucherEntities = gvRepo.findByFromDateAndToDateAndClientId(searchVo.getFromDate(),
					searchVo.getToDate(), searchVo.getClientId());

		} else if (searchVo.getFromDate() != null && searchVo.getToDate() == null && searchVo.getGvNumber() != null
				&& searchVo.getClientId() != null) {

			giftVoucherEntities = gvRepo.findByFromDateAndGvNumberAndClientId(searchVo.getFromDate(),
					searchVo.getGvNumber(), searchVo.getClientId());

		} else if (searchVo.getFromDate() == null && searchVo.getToDate() != null && searchVo.getGvNumber() != null
				&& searchVo.getClientId() != null) {

			giftVoucherEntities = gvRepo.findByToDateAndGvNumberAndClientId(searchVo.getToDate(),
					searchVo.getGvNumber(), searchVo.getClientId());

		} else if (searchVo.getFromDate() == null && searchVo.getToDate() == null && searchVo.getGvNumber() != null
				&& searchVo.getClientId() != null) {
			Optional<GiftVoucherEntity> gv = gvRepo.findByGvNumberAndClientId(searchVo.getGvNumber(),
					searchVo.getClientId());
			giftVoucherEntities.add(gv.get());
		} else if (searchVo.getFromDate() != null && searchVo.getToDate() == null && searchVo.getGvNumber() == null
				&& searchVo.getClientId() != null) {

			giftVoucherEntities = gvRepo.findByFromDateAndClientId(searchVo.getFromDate(), searchVo.getClientId());

		} else if (searchVo.getFromDate() == null && searchVo.getToDate() != null && searchVo.getGvNumber() == null
				&& searchVo.getClientId() != null) {

			giftVoucherEntities = gvRepo.findByToDateAndClientId(searchVo.getToDate(), searchVo.getClientId());

		} else {
			giftVoucherEntities = gvRepo.findByClientId(searchVo.getClientId());
		}

		if (CollectionUtils.isEmpty(giftVoucherEntities)) {

			throw new DataNotFoundException("Records not found");

		} else {

			giftVoucherEntities.stream().forEach(g -> {

				GiftVoucherVo gvo = new GiftVoucherVo();
				gvo.setGvId(g.getGvId());
				gvo.setGvNumber(g.getGvNumber());
				gvo.setValue(g.getValue());
				gvo.setClientId(g.getClientId());
				gvo.setFromDate(g.getFromDate());
				gvo.setToDate(g.getToDate());
				gvo.setDescription(g.getDescription());
				gvo.setIsActivated(g.getIsActivated());
				gvo.setCreationDate(g.getCreationDate());

				giftVoucherVoList.add(gvo);
			});
		}

		return giftVoucherVoList;

	}

}