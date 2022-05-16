package com.otsi.retail.newSale.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.common.AccountType;
import com.otsi.retail.newSale.common.ReturnSlipStatus;
import com.otsi.retail.newSale.config.Config;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.mapper.ReturnSlipMapper;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.repository.ReturnSlipRepo;
import com.otsi.retail.newSale.vo.GenerateReturnSlipRequest;
import com.otsi.retail.newSale.vo.InventoryUpdateVo;
import com.otsi.retail.newSale.vo.LedgerLogBookVo;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;
import com.otsi.retail.newSale.vo.ReturnSlipVo;
import com.otsi.retail.newSale.vo.UserDetailsVo;

@Service
public class ReturnSlipServiceImp implements ReturnslipService {

	private Logger log = LogManager.getLogger(ReturnSlipServiceImp.class);

	@Autowired
	private ReturnSlipRepo returnSlipRepo;
	@Autowired
	private NewSaleRepository newsaleRepo;

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
		PaymentAmountType returnslip = orderTransactionRepo.findByReturnReferenceAndStoreId(returnReferenceNumber, storeId);
		if (returnslip != null) {

			ReturnSlipRequestVo returnSlipVo = returnSlipMapper.convertDtoToVo(returnslip);
			return returnSlipVo;
		}
		return null;
	}

	private String generateRtNumber() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		LocalDate currentdate = LocalDate.now();
		int month = currentdate.getMonth().getValue();
		int year = currentdate.getYear();
		return "RT" + timestamp.getTime();
	}

	@Override
	public List<ReturnSlipRequestVo> createReturnSlip(ReturnSlipRequestVo returnSlipRequestVo) {

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

}
