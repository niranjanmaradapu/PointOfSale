package com.otsi.retail.promoexchange.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.otsi.retail.promoexchange.vo.BarcodeVo;
import com.otsi.retail.promoexchange.vo.CustomerVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfSaleBillsVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;

/**
 * Controller class for accepting all the requests which are related to
 * CustomerSaving API, NewSale API, Create delivery slip API
 * 
 * @author Manikanta Guptha
 *
 */

@RestController
@RequestMapping(CommonRequestMappings.PROMO_ITEM_EXCHANGE)
public class PromoExchangeController {

	@Autowired
	private PromoExchangeService promoExchangeService;

	@Autowired
	private CustomerService service;

	// Save customer details API
	@PostMapping(path = CommonRequestMappings.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details) {
		try {
			String result = service.saveCustomerDetails(details);
			return new GateWayResponse<>(HttpStatus.OK, result, "");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappings.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public GateWayResponse<?> getCustomerByMobileNumber(@RequestParam String mobileNumber) {

		CustomerVo customer;
		try {
			customer = service.getCustomerByMobileNumber(mobileNumber);
		} catch (Exception e) {

			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new GateWayResponse<>(HttpStatus.OK, customer, "");
	}

	// @ Method for saving promo exchange items...
	@PostMapping(CommonRequestMappings.ITEM_EXCHANGE)
	public GateWayResponse<?> saveItemExchange(@RequestBody PromoExchangeVo vo) {

		String message = promoExchangeService.savePromoItemExchangeRequest(vo);

		return new GateWayResponse<>(HttpStatus.OK, message, "");
	}

	// Method for create new Barcode..
	@PostMapping(CommonRequestMappings.CREATE_BARCODE)
	public GateWayResponse<?> saveBarcode(@RequestBody BarcodeVo vo) {

		String result;
		try {
			result = promoExchangeService.saveBarcode(vo);
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return new GateWayResponse<>(HttpStatus.OK, result, "");
	}

	// Method for getting Barcode details from Barcode table using Barcode number
	@GetMapping(CommonRequestMappings.GET_BARCODE_DETAILS)
	public GateWayResponse<?> getBarcodeDetails(@RequestParam String barCode) {

		BarcodeVo barCodeDetails;
		try {
			barCodeDetails = promoExchangeService.getBarcodeDetails(barCode);
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return new GateWayResponse<>(HttpStatus.OK, barCodeDetails, "");

	}

	// Method for creating Delivery slip using List of Barcodes
	@PostMapping(CommonRequestMappings.CREATE_DS)
	public GateWayResponse<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo) {

		try {
			String saveDs = promoExchangeService.saveDeliverySlip(vo);
			return new GateWayResponse<>(HttpStatus.OK, saveDs, "");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappings.GET_DS)
	public GateWayResponse<?> getDeliverySlipDetails(@RequestParam String dsNumber) {

		try {
			DeliverySlipVo dsDetails = promoExchangeService.getDeliverySlipDetails(dsNumber);
			return new GateWayResponse<>(HttpStatus.OK, dsDetails, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

// @
	/*
	 * @GetMapping(CommonRequestMappings.GET_NEWSALE_WITH_DELIVERYSLIPS) public
	 * ResponseEntity<?> getNewsaleWithDeliveryslip(@RequestParam String dsNumber) {
	 * 
	 * 
	 * DeliverySlipVo dsDetails =
	 * promoExchangeService.getNewsaleWithDeliveryslip(dsNumber); return new
	 * ResponseEntity<>(dsDetails, HttpStatus.OK);
	 * 
	 * 
	 * 
	 * }
	 */
	@GetMapping(CommonRequestMappings.GET_LIST_OF_RETURN_SLIPS)
	public GateWayResponse<?> getlistofReturnSlips() {

		try {

			return new GateWayResponse<>(promoExchangeService.getListOfRetunSlips());
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		throw new RuntimeException("return slip details not found");

	}

	// Method for getting list of sale bills

	@GetMapping(CommonRequestMappings.GET_LIST_OF_PROMO_EXCHANGE_BILLS)
	public GateWayResponse<?> getListOfPromoItemExchangeBills(@RequestBody ListOfSaleBillsVo svo) {
		try {

			ListOfSaleBillsVo listOfSaleBills = promoExchangeService.getListOfSaleBills(svo);
			return new GateWayResponse<>(HttpStatus.OK, listOfSaleBills, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());

		}
	}

	// Method for getting list of delivery slips

	/*
	 * @GetMapping(CommonRequestMappigs.GET_LISTOF_DS) public ResponseEntity<?>
	 * getlistofDeliverySlips1(@RequestBody ListOfDeliverySlipVo
	 * listOfDeliverySlipVo) {
	 * 
	 * // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 * try {
	 * 
	 * ResponseEntity<?> getDs =
	 * promoExchangeService.getDeliverySlipDetails(listOfDeliverySlipVo);
	 * 
	 * return new ResponseEntity<>(getDs, HttpStatus.OK); } catch (Exception e) {
	 * return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	 * 
	 * }}
	 */

	// Method for day closer

	/*
	 * @GetMapping(path = CommonRequestMappings.GET_PROMO_ITEMS) public
	 * Optional<PromoExchangeEntity> getPromoItems(@RequestParam Long
	 * promoExchangeId) { return
	 * promoExchangeService.getPromoItems(promoExchangeId); }
	 */
}
