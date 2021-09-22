package com.otsi.retail.promoexchange.controller;

import java.util.List;

import javax.validation.Valid;

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

@RestController
@RequestMapping(CommonRequestMappings.PROMO_ITEM_EXCHANGE)
public class PromoExchangeController {

	@Autowired
	private PromoExchangeService promoExchangeService;

	@Autowired
	private CustomerService service;

	// Save customer details API
	@PostMapping(path = CommonRequestMappings.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details) throws Exception {

		String result = service.saveCustomerDetails(details);
		return new GateWayResponse<>(result);

	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappings.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public GateWayResponse<?> getCustomerByMobileNumber(@RequestParam String mobileNumber) throws Exception {

		CustomerVo customer;

		customer = service.getCustomerByMobileNumber(mobileNumber);
		return new GateWayResponse<>(customer);
	}

	// @ Method for saving promo exchange items...
	@PostMapping(CommonRequestMappings.ITEM_EXCHANGE)
	public GateWayResponse<?> saveItemExchange(@RequestBody PromoExchangeVo vo) {

		String message = promoExchangeService.savePromoItemExchangeRequest(vo);

		return new GateWayResponse<>("Item Exchanged Successfully", message);
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappings.GET_DS)
	public GateWayResponse<?> getDeliverySlipDetails(@RequestParam String dsNumber) throws Exception {

		DeliverySlipVo dsDetails = promoExchangeService.getDeliverySlipDetails(dsNumber);
		return new GateWayResponse<>(HttpStatus.OK, dsDetails, "");

	}

	@GetMapping(CommonRequestMappings.GET_LIST_OF_RETURN_SLIPS)
	public GateWayResponse<?> getlistofReturnSlips() throws JsonMappingException, JsonProcessingException {

		List<ListOfReturnSlipsVo> returnSlips = promoExchangeService.getListOfRetunSlips();

		return new GateWayResponse<>(HttpStatus.OK, returnSlips, "Success");

	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappings.GET_PROMO_EXCHANGE_BILLS_BY_BILL_NUMBER)
	public GateWayResponse<?> getSaleBillsBillNumber(@RequestParam String billNumber) {

		List<PromoExchangeVo> billDetails = promoExchangeService.getSaleBillByBillNumber(billNumber);
		return new GateWayResponse<>(HttpStatus.OK, billDetails, "Success");

	}

	@GetMapping(CommonRequestMappings.GET_LIST_OF_PROMO_EXCHANGE_BILLS)
	public GateWayResponse<?> getlistofPromoExchageBills() {

		List<PromoExchangeVo> vo = promoExchangeService.getListOfSaleBills();

		return new GateWayResponse<>(HttpStatus.OK, vo, "Success");

	}

}
