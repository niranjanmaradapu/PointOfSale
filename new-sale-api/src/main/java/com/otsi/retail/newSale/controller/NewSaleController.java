package com.otsi.retail.newSale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.common.CommonRequestMappigs;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.service.NewSaleService;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleList;
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
			CustomerVo result = service.saveCustomerDetails(details);
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
		CustomerVo customer;
		try {
			customer = service.getCustomerByMobileNumber(mobileNumber);
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} catch (CustomerNotFoundExcecption e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

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
	public ResponseEntity<?> getBarcodeDetails(@RequestParam String barCode, @RequestParam String smId) {
		log.info("Received Request to getBarcodeDetails:" + barCode);
		ResponseEntity<?> barCodeDetails = newSaleService.getBarcodeDetails(barCode, smId);

		return new ResponseEntity<>(barCodeDetails, HttpStatus.OK);

	}

	// Method for creating Delivery slip using List of Barcodes
	@PostMapping(CommonRequestMappigs.CREATE_DS)
	public ResponseEntity<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo,String enumName) {
		log.info("Received Request to saveDeliverySlip :" + vo.toString());
		try {
			ResponseEntity<?> saveDs = newSaleService.saveDeliverySlip(vo, enumName);
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

	@GetMapping(CommonRequestMappigs.DAY_CLOSER)
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

	@GetMapping(CommonRequestMappigs.POS_CLOSEDAY)
	public ResponseEntity<?> posclose(@RequestParam Boolean posclose) {
		try {
			ResponseEntity<?> dayclose = newSaleService.posClose(posclose);
			return new ResponseEntity<>(dayclose, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

		}
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

	@PostMapping(value = "getInvoiceDetails", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getInvoiceDetails(@RequestBody InvoiceRequestVo vo) {
		try {
			NewSaleList newSaleList = newSaleService.getInvoicDetails(vo);
			return new ResponseEntity<>(newSaleList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getCustomerFromNewSale/{mobileNo}")
	public ResponseEntity<?> getCustomerFromNewSale(@PathVariable String mobileNo) {
		try {
			CustomerVo customer = service.getCustomerByMobileNumber(mobileNo);
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} catch (CustomerNotFoundExcecption ce) {
			return new ResponseEntity<>(ce.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getHsnDetails/{netAmt}")
	public ResponseEntity<?> getHsnDetails(@PathVariable double netAmt) {
		try {
			log.info("Recieved request to getNewSaleWithHsn()");
			return new ResponseEntity<>(newSaleService.getNewSaleWithHsn(netAmt), HttpStatus.OK);
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		log.error("hsn details not found");
		throw new RuntimeException("hsn details not found");
	}

	@GetMapping("/tagCustomerToInvoice/{mobileNo}/{invoiceNo}")
	public ResponseEntity<?> tagCustomerToInvoice(@PathVariable String mobileNo, @PathVariable String invoiceNo) {
		try {
			newSaleService.tagCustomerToExisitingNewSale(mobileNo, Long.parseLong(invoiceNo));
		} catch (CustomerNotFoundExcecption e) {
			e.printStackTrace();
		}
		return null;

	}

	@GetMapping("/discTypes")
	public List<String> getDiscountsTypes() {

		List<String> discTypes = new ArrayList<>();

		discTypes.add("Promotion not applied");
		discTypes.add("RT return discount");
		discTypes.add("Mgnt. SPL Discount");
		discTypes.add("Management discount");
		discTypes.add("DMG Discount");
		discTypes.add("Other");

		return discTypes;
	}
}
