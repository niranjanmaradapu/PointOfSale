package com.otsi.retail.promoexchange.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.promoexchange.common.CommonRequestMappings;
import com.otsi.retail.promoexchange.gateway.GateWayResponse;
import com.otsi.retail.promoexchange.service.CustomerService;
import com.otsi.retail.promoexchange.service.PromoExchangeService;
import com.otsi.retail.promoexchange.vo.CustomerVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfReturnSlipsVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping(CommonRequestMappings.PROMO_ITEM_EXCHANGE)
public class PromoExchangeController {

	//private Logger log = LoggerFactory.getLogger(PromoExchangeController.class);
	
	  private Logger log=LogManager.getLogger(PromoExchangeController.class);

	@Autowired
	private PromoExchangeService promoExchangeService;

	@Autowired
	private CustomerService service;

	// Save customer details API
	@PostMapping(path = CommonRequestMappings.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details) throws Exception {
		log.info("Recieved request to saveCustomerDetails():" + details);
		String result = service.saveCustomerDetails(details);
		return new GateWayResponse<>(result);

	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappings.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public GateWayResponse<?> getCustomerByMobileNumber(@RequestParam String mobileNumber) throws Exception {
		log.info("Recieved request to getCustomerByMobileNumber():" + mobileNumber);
		CustomerVo customer = service.getCustomerByMobileNumber(mobileNumber);
		return new GateWayResponse<>(customer);
	}

	// @ Method for saving promo exchange items...
	@PostMapping(CommonRequestMappings.ITEM_EXCHANGE)
	public GateWayResponse<?> saveItemExchange(@RequestBody PromoExchangeVo vo) {
		log.info("Recieved request to saveItemExchange():" + vo);
		String message = promoExchangeService.savePromoItemExchangeRequest(vo);
		return new GateWayResponse<>("Item Exchanged Successfully", message);
	}

	// CircuitBreaker implemented
	@GetMapping(CommonRequestMappings.GET_DS)
	@CircuitBreaker(name = "GetDeliverySlipCB", fallbackMethod = "getDeliverySlipsFallback")
	public GateWayResponse<?> getDeliverySlipDetails(@RequestParam String dsNumber) throws Exception {
		log.info("Recieved request to getDeliverySlipDetails():" + dsNumber);
		DeliverySlipVo dsDetails = promoExchangeService.getDeliverySlipDetails(dsNumber);
		return new GateWayResponse<>(HttpStatus.OK, dsDetails, "");

	}
	
	public GateWayResponse<?> getDeliverySlipsFallback(Exception ex) {
		log.error("Third Party Service Down");
		return new GateWayResponse<>("Third Party Service  is Down", null);
	}
	
	// CircuitBreaker implemented
	@GetMapping(CommonRequestMappings.GET_LIST_OF_RETURN_SLIPS)
	@CircuitBreaker(name = "GetReturnSlipsCB", fallbackMethod = "getReturnSlipsFallback")
	public GateWayResponse<?> getlistofReturnSlips() throws JsonMappingException, JsonProcessingException {
		log.info("Recieved request to getlistofReturnSlips()");
		List<ListOfReturnSlipsVo> returnSlips = promoExchangeService.getListOfRetunSlips();
		return new GateWayResponse<>(HttpStatus.OK, returnSlips, "Success");

	}
	
	public GateWayResponse<?> getReturnSlipsFallback(Exception ex) {
		log.error("Third Party Service Down");
		return new GateWayResponse<>("Third Party Service  is Down", null);
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappings.GET_PROMO_EXCHANGE_BILLS_BY_BILL_NUMBER)
	public GateWayResponse<?> getSaleBillsBillNumber(@RequestParam String billNumber) {
		log.info("Recieved request to getSaleBillsBillNumber():" + billNumber);
		List<PromoExchangeVo> billDetails = promoExchangeService.getSaleBillByBillNumber(billNumber);
		return new GateWayResponse<>(HttpStatus.OK, billDetails, "Success");

	}

	@GetMapping(CommonRequestMappings.GET_LIST_OF_PROMO_EXCHANGE_BILLS)
	public GateWayResponse<?> getlistofPromoExchageBills() {
		log.info("Recieved request to getlistofPromoExchageBills()");
		List<PromoExchangeVo> vo = promoExchangeService.getListOfSaleBills();

		return new GateWayResponse<>(HttpStatus.OK, vo, "Success");

	}

}
