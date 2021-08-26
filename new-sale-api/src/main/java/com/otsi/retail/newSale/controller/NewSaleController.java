package com.otsi.retail.newSale.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.otsi.retail.newSale.common.CommonRequestMappigs;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.service.NewSaleService;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;

/**
 * Controller class for accepting all the requests which are related to
 * CustomerSaving API, NewSale API, Create delivery slip API
 * 
 * @author Manikanta Guptha
 *
 */

@RestController
@CrossOrigin
@RequestMapping(CommonRequestMappigs.NEW_SALE)
public class NewSaleController {
	
	private Logger log = LoggerFactory.getLogger(NewSaleController.class);


	@Autowired
	private NewSaleService newSaleService;

	@Autowired
	private CustomerService service;

	// Save customer details API
	@PostMapping(path = CommonRequestMappigs.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details) {
		log.info("Received Request to saveCustomerDetails :" + details.toString());
		try {
			ResponseEntity<?> result = service.saveCustomerDetails(details);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			log.error("exception :" + e);
	return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappigs.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public ResponseEntity<?> getCustomerByMobileNumber(@RequestParam String mobileNumber) {
		log.info("Received Request to getCustomerByMobileNumber :" + mobileNumber);
		CustomerVo customer = service.getCustomerByMobileNumber(mobileNumber);
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	// Method for saving new sale items...
	@PostMapping(CommonRequestMappigs.SALE)
	public ResponseEntity<?> saveNewSale(@RequestBody NewSaleVo vo) {
		log.info("Received Request to saveNewSale :" + vo.toString());
		ResponseEntity<?> message = newSaleService.saveNewSaleRequest(vo);

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// Method for create new Barcode..
	@PostMapping(CommonRequestMappigs.CREATE_BARCODE)
	public ResponseEntity<?> saveBarcode(@RequestBody BarcodeVo vo) {
		log.info("Received Request to saveBarcode :" + vo.toString());
		ResponseEntity<?> result = newSaleService.saveBarcode(vo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	// Method for getting Barcode details from Barcode table using Barcode number
	@GetMapping(CommonRequestMappigs.GET_BARCODE_DETAILS)
	public ResponseEntity<?> getBarcodeDetails(@RequestParam String barCode) {
		log.info("Received Request to getBarcodeDetails:" + barCode);
		ResponseEntity<?> barCodeDetails = newSaleService.getBarcodeDetails(barCode);

		return new ResponseEntity<>(barCodeDetails, HttpStatus.OK);

	}

	// Method for creating Delivery slip using List of Barcodes
	@PostMapping(CommonRequestMappigs.CREATE_DS)
	public ResponseEntity<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo) {
		log.info("Received Request to saveDeliverySlip :" + vo.toString());
		try {
			ResponseEntity<?> saveDs = newSaleService.saveDeliverySlip(vo);
			return new ResponseEntity<>(saveDs, HttpStatus.OK);
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappigs.GET_DS)
	public ResponseEntity<?> getDeliverySlipDetails(@RequestParam String dsNumber) {
		log.info("Received Request to getDeliverySlipDetails :" + dsNumber);
		try {
			ResponseEntity<?> dsDetails = newSaleService.getDeliverySlipDetails(dsNumber);
			return new ResponseEntity<>(dsDetails, HttpStatus.OK);

		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	// Method for getting list of sale bills
	@GetMapping(CommonRequestMappigs.GET_LISTOF_SALEBILLS)
	public ResponseEntity<?> getListOfSaleBills(@RequestBody ListOfSaleBillsVo svo) {
		log.info("Received Request to getListOfSaleBills :" + svo.toString());
		try {

			ResponseEntity<?> listOfSaleBills = newSaleService.getListOfSaleBills(svo);
			return new ResponseEntity<>(listOfSaleBills, HttpStatus.OK);
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}
	}

	// Method for getting list of delivery slips

	@GetMapping(CommonRequestMappigs.GET_LISTOF_DS)
	public ResponseEntity<?> getlistofDeliverySlips(@RequestBody ListOfDeliverySlipVo listOfDeliverySlipVo) {
		log.info("Received Request to getlistofDeliverySlips :" + listOfDeliverySlipVo.toString());
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {

			ResponseEntity<?> getDs = newSaleService.getlistofDeliverySlips(listOfDeliverySlipVo);

			return new ResponseEntity<>(getDs, HttpStatus.OK);
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}

	}
	// Method for day closer

	@GetMapping(value = "daycloser")
	public ResponseEntity<?> dayclose() {
		log.info("Recieved request to dayclose()");
		try {
			ResponseEntity<?> dayclose = newSaleService.posDayClose();
			return new ResponseEntity<>(dayclose, HttpStatus.OK);
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}

	}
	
	@GetMapping(CommonRequestMappigs.GET_HSN_DETAILS)
	public ResponseEntity<?> getHsnDetails() {
		try {
			log.info("Recieved request to getNewSaleWithHsn()");
			return new ResponseEntity<>(newSaleService.getNewSaleWithHsn(), HttpStatus.OK);
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		log.error("hsn details not found");
		throw new RuntimeException("hsn details not found");
	}

	@GetMapping(CommonRequestMappigs.GET_NEWSALEBYCUSTOMERID)
	public ResponseEntity<?> getNewsaleByCustomerId(@RequestParam Long customerId) {
		log.info("Recieved request to getNewsaleByCustomerId():" + customerId);
		List<NewSaleResponseVo> message = newSaleService.getNewsaleByCustomerId(customerId);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping(CommonRequestMappigs.UPDATE_NEWSALE)
	public ResponseEntity<?> updateNewSale(@RequestBody NewSaleResponseVo vo) {
		log.info("Recieved request to updateNewSale():" + vo.toString());
		NewSaleVo message = newSaleService.updateNewSale(vo);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}