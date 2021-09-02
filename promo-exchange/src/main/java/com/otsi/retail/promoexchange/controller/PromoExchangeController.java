package com.otsi.retail.promoexchange.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;
import com.otsi.retail.promoexchange.common.CommonRequestMappings;
import com.otsi.retail.promoexchange.service.CustomerService;
import com.otsi.retail.promoexchange.service.PromoExchangeService;
import com.otsi.retail.promoexchange.vo.BarcodeVo;
import com.otsi.retail.promoexchange.vo.CustomerVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfDeliverySlipVo;
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
@CrossOrigin
@RequestMapping(CommonRequestMappings.PROMO_ITEM_EXCHANGE)
public class PromoExchangeController {

	@Autowired
	private PromoExchangeService promoExchangeService;

	@Autowired
	private CustomerService service;

	// Save customer details API
	@PostMapping(path = CommonRequestMappings.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details) {
		try {
			ResponseEntity<?> result = service.saveCustomerDetails(details);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappings.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public ResponseEntity<?> getCustomerByMobileNumber(@RequestParam String mobileNumber) {

		ResponseEntity<?> customer = service.getCustomerByMobileNumber(mobileNumber);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	// @ Method for saving promo exchange items...
	@PostMapping(CommonRequestMappings.PROMO_ITEM_EXCHANGE)
	public ResponseEntity<?> saveItemExchange(@RequestBody PromoExchangeVo vo) {

		ResponseEntity<?> message = promoExchangeService.savePromoItemExchangeRequest(vo);

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// Method for create new Barcode..
	@PostMapping(CommonRequestMappings.CREATE_BARCODE)
	public ResponseEntity<?> saveBarcode(@RequestBody BarcodeVo vo) {

		ResponseEntity<?> result = promoExchangeService.saveBarcode(vo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// Method for getting Barcode details from Barcode table using Barcode number
	@GetMapping(CommonRequestMappings.GET_BARCODE_DETAILS)
	public ResponseEntity<?> getBarcodeDetails(@RequestParam String barCode) {

		ResponseEntity<?> barCodeDetails = promoExchangeService.getBarcodeDetails(barCode);

		return new ResponseEntity<>(barCodeDetails, HttpStatus.OK);

	}

	// Method for creating Delivery slip using List of Barcodes
	@PostMapping(CommonRequestMappings.CREATE_DS)
	public ResponseEntity<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo) {

		try {
			ResponseEntity<?> saveDs = promoExchangeService.saveDeliverySlip(vo);
			return new ResponseEntity<>(saveDs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappings.GET_DS)
	public ResponseEntity<?> getDeliverySlipDetails(@RequestParam String dsNumber) {

		try {
			DeliverySlipVo dsDetails = promoExchangeService.getDeliverySlipDetails(dsNumber);
			return new ResponseEntity<>(dsDetails, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<?> getlistofReturnSlips() {

		try {

			return new ResponseEntity<>(promoExchangeService.getListOfRetunSlips(), HttpStatus.OK);
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		throw new RuntimeException("return slip details not found");

	}

	// Method for getting list of sale bills

	@GetMapping(CommonRequestMappings.GET_LIST_OF_PROMO_EXCHANGE_BILLS)
	public ResponseEntity<?> getListOfPromoItemExchangeBills(@RequestBody ListOfSaleBillsVo svo) {
		try {

			ResponseEntity<?> listOfSaleBills = promoExchangeService.getListOfSaleBills(svo);
			return new ResponseEntity<>(listOfSaleBills, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

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
