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

import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.config.Config;
import com.otsi.retail.newSale.mapper.ReturnSlipMapper;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.repository.PaymentAmountTypeRepository;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;

@Service
public class ReturnSlipServiceImp implements ReturnslipService {

	private Logger log = LogManager.getLogger(ReturnSlipServiceImp.class);

	
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
