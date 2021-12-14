package com.otsi.retail.newSale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.common.CommonRequestMappigs;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.service.NewSaleService;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.LoyalityPointsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.ReturnSlipVo;
import com.otsi.retail.newSale.vo.SaleReportVo;
import com.otsi.retail.newSale.vo.SearchLoyaltyPointsVo;
import com.otsi.retail.newSale.vo.UserDataVo;

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
	private CustomerService customerService;

	// Save customer details API
	@PostMapping(path = CommonRequestMappigs.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details)
			throws DuplicateRecordException {
		log.info("Received Request to saveCustomerDetails :" + details.toString());

		String result = customerService.saveCustomerDetails(details);
		return new GateWayResponse<>(HttpStatus.OK, result, "");

	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappigs.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public GateWayResponse<?> getCustomerByMobileNumber(@RequestParam String mobileNumber)
			throws CustomerNotFoundExcecption {
		log.info("Received Request to getCustomerByMobileNumber :" + mobileNumber);
		CustomerVo customer;

		customer = customerService.getCustomerByMobileNumber(mobileNumber);
		return new GateWayResponse<>(HttpStatus.OK, customer, "successfully getting the record");

	}

	// Method for saving new sale items...
	@PostMapping(CommonRequestMappigs.SALE)
	public GateWayResponse<?> saveNewSale(@RequestBody NewSaleVo vo) throws InvalidInputException {
		log.info("Received Request to saveNewSale :" + vo);

		try {
			if ((vo.getDlSlip() == null || vo.getDlSlip().isEmpty())
					&& (vo.getLineItemsReVo() == null || vo.getLineItemsReVo().isEmpty())) {

				return new GateWayResponse<>("Provide valid inputs", "Provide valid inputs");
			}
			String message = newSaleService.saveNewSaleRequest(vo);

			return new GateWayResponse<>("Successfully created order.", message);
		} catch (InvalidInputException e) {
			log.error("Exception occurs while creating order : " + vo);
			return new GateWayResponse<>(e.getMsg(), "Exception occurs while creating order");
		}
	}

	// Method for getting invoice details using Order number
	@GetMapping("/getinvoicedata")
	public GateWayResponse<?> getInvoicedetails(@RequestParam String orderNumber) throws RecordNotFoundException {
		try {
			NewSaleVo result = newSaleService.getInvoiceDetails(orderNumber);
			return new GateWayResponse<>("Success..", result);
		} catch (RecordNotFoundException re) {
			return new GateWayResponse<>(re.getMsg(), "Exception occurs while fetching invoice details");
		}
	}

	// Method to confirm payment status based on Razorpay Id
	@PostMapping("payconfirmation")
	public GateWayResponse<?> paymentConfirmationFromRazorpay(@RequestParam String razorPayId,
			@RequestParam boolean payStatus) {
		log.info("Received payment confirmation for razorpayId :" + razorPayId);
		try {
			String result = newSaleService.paymentConfirmationFromRazorpay(razorPayId, payStatus);
			return new GateWayResponse<>(result, "Success..");

		} catch (Exception e) {
			log.error("Exception occurs while confirming payment for Id : " + razorPayId);
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), "Exception occurs");
		}
	}

	// Method for create new Barcode..
	@PostMapping(CommonRequestMappigs.CREATE_BARCODE)
	public GateWayResponse<?> saveBarcode(@RequestBody BarcodeVo vo) throws DuplicateRecordException {
		log.info("Received Request to saveBarcode :" + vo.toString());

		String result = newSaleService.saveBarcode(vo);

		return new GateWayResponse<>(HttpStatus.OK, result);

	}

	// Method for getting Barcode details from Barcode table using Barcode number
	@GetMapping(CommonRequestMappigs.GET_BARCODE_DETAILS)
	public GateWayResponse<?> getBarcodeDetails(@RequestParam String barCode, @RequestParam String smId)
			throws RecordNotFoundException {
		log.info("Received Request to getBarcodeDetails:" + barCode);

		BarcodeVo barCodeDetails = newSaleService.getBarcodeDetails(barCode, smId);

		return new GateWayResponse<>(HttpStatus.OK, barCodeDetails, "");

	}

	// Method for creating Line Items
	@PostMapping("/savelineitems/{domainId}")
	public GateWayResponse<?> saveLineItems(@PathVariable Long domainId, @RequestBody List<LineItemVo> lineItems)
			throws InvalidInputException {
		log.info("Save Line items with values " + lineItems.toString());
		try {
			List<Long> result = newSaleService.saveLineItems(lineItems, domainId);

			return new GateWayResponse<>("Successfully saved Line item..", result.toString());
		} catch (InvalidInputException e) {
			log.error("Exception occurs while line items " + lineItems.toString());
			return new GateWayResponse<>(e.getMsg(), "Exception occurs");
		}
	}

	// Method for modifying line items
	@PutMapping("/editlineitem")
	public GateWayResponse<?> editLineItems(@RequestBody LineItemVo lineItem) throws RecordNotFoundException {
		log.info("edit Line item with values " + lineItem);

		String result = newSaleService.editLineItem(lineItem);

		return new GateWayResponse<>(result, result);
	}

	// Method for creating Delivery slip using List of LineItems
	@PostMapping(CommonRequestMappigs.CREATE_DS)
	public GateWayResponse<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo) throws RecordNotFoundException {
		log.info("Received Request to saveDeliverySlip :" + vo);
		try {

			String saveDs = newSaleService.saveDeliverySlip(vo);
			return new GateWayResponse<>("successfully created deliveryslip", saveDs);
		} catch (RecordNotFoundException e) {
			log.error("Exception occurs while saving delivery slip : " + vo);
			return new GateWayResponse<>(e.getMsg(), "Exception occurs");
		}
	}

	// Method getting line item by using Bar code based on their domain id
	@GetMapping("/getlineitem")
	public GateWayResponse<?> getLineItemByBarcode(@RequestParam String barCode, @RequestParam Long domainId)
			throws RecordNotFoundException {

		log.info("Recieved request for getting line item : " + barCode);
		LineItemVo result = newSaleService.getLineItemByBarcode(barCode, domainId);

		return new GateWayResponse<>("Success", result);

	}

	// Method for deleting line item by using Bar code based on domain id
	@DeleteMapping("/deletelineitem")
	public GateWayResponse<?> deleteLineItemByBarCode(@RequestParam String barCode, @RequestParam Long domainId)
			throws RecordNotFoundException {

		log.info("Recieved request to delete line item : " + barCode);
		String result = newSaleService.deleteLineItem(barCode, domainId);

		return new GateWayResponse<>("Success", result);
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappigs.GET_DS)
	public GateWayResponse<?> getDeliverySlipDetails(@RequestParam String dsNumber) throws RecordNotFoundException {
		log.info("Received Request to getDeliverySlipDetails :" + dsNumber);

		DeliverySlipVo dsDetails = newSaleService.getDeliverySlipDetails(dsNumber);
		return new GateWayResponse<>(HttpStatus.OK, dsDetails, "");

	}

	// method for deleting pending delivery slip data
	@DeleteMapping(CommonRequestMappigs.DELETE_DS)
	public GateWayResponse<?> deleteDeliverySlipDetails(@RequestParam Long dsId) throws RecordNotFoundException {
		log.info("Received Request to getDeliverySlipDetails :" + dsId);

		String dsDetails = newSaleService.deleteDeliverySlipDetails(dsId);
		return new GateWayResponse<>("Success", dsDetails);
	}

	// Method for getting list of sale bills
	@PostMapping(CommonRequestMappigs.GET_LISTOF_SALEBILLS)
	public GateWayResponse<?> getListOfSaleBills(@RequestBody ListOfSaleBillsVo svo)
			throws RecordNotFoundException, JsonMappingException, JsonProcessingException {
		log.info("Received Request to getListOfSaleBills :" + svo.toString());

		ListOfSaleBillsVo listOfSaleBills = newSaleService.getListOfSaleBills(svo);
		return new GateWayResponse<>(HttpStatus.OK, listOfSaleBills, "");

	}

	// Method for getting list of delivery slips

	@PostMapping(CommonRequestMappigs.GET_LISTOF_DS)
	public GateWayResponse<?> getlistofDeliverySlips(@RequestBody ListOfDeliverySlipVo listOfDeliverySlipVo)
			throws RecordNotFoundException {
		log.info("Received Request to getlistofDeliverySlips :" + listOfDeliverySlipVo.toString());
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		ListOfDeliverySlipVo getDs = newSaleService.getlistofDeliverySlips(listOfDeliverySlipVo);

		return new GateWayResponse<>(HttpStatus.OK, getDs, "");

	}
	// Method for day closer

	@GetMapping(CommonRequestMappigs.DAY_CLOSER)
	public GateWayResponse<?> dayclose() {
		log.info("Recieved request to dayclose()");
		try {
			List<DeliverySlipEntity> dayclose = newSaleService.posDayClose();
			return new GateWayResponse<>("Success", dayclose);
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());

		}

	}

	@GetMapping(CommonRequestMappigs.POS_CLOSEDAY)
	public GateWayResponse<?> posclose(@RequestParam Boolean posclose) {
		try {
			String dayclose = newSaleService.posClose(posclose);
			return new GateWayResponse<>("Success", dayclose);
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping(CommonRequestMappigs.GET_NEWSALEBYCUSTOMERID)
	public GateWayResponse<?> getNewsaleByCustomerId(@RequestParam Long customerId) throws DataNotFoundException {
		log.info("Recieved request to getNewsaleByCustomerId():" + customerId);

		List<NewSaleResponseVo> message = newSaleService.getNewsaleByCustomerId(customerId);
		return new GateWayResponse<>(HttpStatus.OK, message, "");

	}

	@PostMapping(CommonRequestMappigs.UPDATE_NEWSALE)
	public GateWayResponse<?> updateNewSale(@RequestBody NewSaleResponseVo vo) throws RecordNotFoundException {
		log.info("Recieved request to updateNewSale():" + vo.toString());

		NewSaleVo message = newSaleService.updateNewSale(vo);
		return new GateWayResponse<>(HttpStatus.OK, message, "");

	}

	@PostMapping(value = "getInvoiceDetails", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> getInvoiceDetails(@RequestBody InvoiceRequestVo vo) throws RecordNotFoundException {
		log.info("Recieved request to getInvoiceDetails():" + vo.toString());
		List<ReturnSlipVo> newSaleList = newSaleService.getInvoicDetails(vo);
		return new GateWayResponse<>(HttpStatus.OK, newSaleList, "");

	}

	@GetMapping("/getCustomerFromNewSale/{mobileNo}")
	public GateWayResponse<?> getCustomerFromNewSale(@PathVariable String mobileNo) throws CustomerNotFoundExcecption {
		log.info("Recieved request to getCustomerFromNewSale():" + mobileNo);
		CustomerVo customer = customerService.getCustomerByMobileNumber(mobileNo);
		return new GateWayResponse<>(HttpStatus.OK, customer, "");

	}

	@GetMapping("/getHsnDetails/{netAmt}")
	public GateWayResponse<?> getHsnDetails(@PathVariable double netAmt)
			throws JsonMappingException, JsonProcessingException, DataNotFoundException {

		log.info("Recieved request to getNewSaleWithHsn()");
		double netamt = newSaleService.getNewSaleWithHsn(netAmt);
		return new GateWayResponse<>(HttpStatus.OK, netamt, "");

	}

	@GetMapping("/tagCustomerToInvoice/{mobileNo}/{invoiceNo}")
	public GateWayResponse<?> tagCustomerToInvoice(@PathVariable String mobileNo, @PathVariable String invoiceNo)
			throws NumberFormatException, CustomerNotFoundExcecption {
		log.info("Recieved request to tagCustomerToInvoice():" + mobileNo + "and the invoice is:" + invoiceNo);
		newSaleService.tagCustomerToExisitingNewSale(mobileNo, Long.parseLong(invoiceNo));

		return new GateWayResponse<>(HttpStatus.OK, "success");

	}

	@GetMapping("/discTypes")
	public GateWayResponse<?> getDiscountsTypes() {
		log.info("Recieved request to getDiscountsTypes()");
		List<String> discTypes = new ArrayList<>();

		discTypes.add("Promotion not applied");
		discTypes.add("RT return discount");
		discTypes.add("Mgnt. SPL Discount");
		discTypes.add("Management discount");
		discTypes.add("DMG Discount");
		discTypes.add("Other");

		return new GateWayResponse<>(HttpStatus.OK, discTypes, "");
	}

	// Method for saving GiftVoucher
	@PostMapping("/saveGv")
	public GateWayResponse<?> saveGiftVoucher(@RequestBody GiftVoucherVo vo) throws DuplicateRecordException {
		log.info("Recieved request to saveGiftVoucher():" + vo);
		try {
			String result = newSaleService.saveGiftVoucher(vo);
			return new GateWayResponse<>(result, "Successfully saved");
		} catch (DuplicateRecordException dre) {
			log.error("Getting error while saving giftvoucher : " + vo);
			return new GateWayResponse<>(dre.getMsg(), "Duplicate Record");
		}
	}

	// Method for getting Gift voucher by GV Number
	@GetMapping("/getGv")
	public GateWayResponse<?> getGiftVoucher(@RequestParam String gvNumber) throws InvalidInputException {
		log.info("Recieved request to getting giftVoucher : " + gvNumber);
		try {
			GiftVoucherVo result = newSaleService.getGiftVoucher(gvNumber);
			return new GateWayResponse<>("Successfully fetch record", result);
		} catch (InvalidInputException iie) {
			log.error("Getting error while fetching giftvoucher : " + gvNumber);
			return new GateWayResponse<>(iie.getMsg(), "Record not found");
		}

	}

	// Method for fetching Gift voucher by userId
	@GetMapping("/getgvbyuserid")
	public GateWayResponse<?> getGvByUserId(@RequestParam Long userId) throws RecordNotFoundException {
		log.info("Recieved request to fetching Giftvouchers by user Id : " + userId);
		try {
			List<GiftVoucherVo> result = newSaleService.getGvByUserId(userId);
			return new GateWayResponse<>("Success", result);
		} catch (RecordNotFoundException rfe) {
			return new GateWayResponse<>(rfe.getMsg(), "No record found");
		}
	}

	// Method for getting list of Gift vouchers
	@GetMapping("/getlistofgv")
	public GateWayResponse<?> getListOfGvs() throws RecordNotFoundException {
		log.info("Received request to fetch lift of gift vouchers");
		try {
			List<GiftVoucherVo> result = newSaleService.getListOfGiftvouchers();
			return new GateWayResponse<>("Successfully fetch records", result);
		} catch (RecordNotFoundException rfe) {
			return new GateWayResponse<>(rfe.getMsg(), "Record not found");
		}
	}

	// Method for saving Userdata
	@PostMapping("/saveuser")
	public GateWayResponse<?> saveUser(@RequestBody UserDataVo vo) throws DuplicateRecordException {
		log.info("Recieved request to saveUser():" + vo);
		String message = customerService.saveUserData(vo);
		return new GateWayResponse<>(HttpStatus.OK, message, "");

	}

	// Method for fetching user data by using mobile number
	@GetMapping("/getUserByMobileNo")
	public GateWayResponse<?> getUserByMobileNo(@RequestParam Long mobileNum) throws RecordNotFoundException {
		log.info("Recieved request to getUserByMobileNo():" + mobileNum);
		UserDataVo user = customerService.getUserByMobileNo(mobileNum);

		return new GateWayResponse<>(HttpStatus.OK, user, "");

	}

	// Method for tagging Gift voucher to Customer
	@PostMapping("/tagCustomerToGv/{userId}/{gvId}")
	public GateWayResponse<?> tagCustomerToGv(@PathVariable Long userId, @PathVariable Long gvId)
			throws InvalidInputException, DataNotFoundException {
		log.info("Recieved request to tagCustomerToGv " + userId + "and the gv is : " + gvId);
		try {
			String message = newSaleService.tagCustomerToGv(userId, gvId);
			return new GateWayResponse<>(message, "Success");
		} catch (DataNotFoundException dfe) {
			log.error("Getting error while tag customer to giftvoucher : " + userId + " " + gvId);
			return new GateWayResponse<>(dfe.getMsg(), "Please provide valid inputs");
		}
	}

	// Method for getting all Bar codes list
	@GetMapping("/getAllBarcodes")
	public GateWayResponse<?> getAllBarcodes() throws DataNotFoundException {
		log.info("Recieved request to getAllBarcodes()");
		List<BarcodeVo> listOfBarcodes = newSaleService.getAllBarcodes();
		return new GateWayResponse<>(HttpStatus.OK, listOfBarcodes, "Successfully fetched list of Barcodes");

	}

	// Method for getting list of sale report
	@PostMapping(CommonRequestMappigs.GET_SALE_REPORT)
	public GateWayResponse<?> getSaleReport(@RequestBody SaleReportVo srvo) throws RecordNotFoundException {

		SaleReportVo saleReport = newSaleService.getSaleReport(srvo);
		return new GateWayResponse<>(HttpStatus.OK, saleReport, "");

	}

	@PostMapping("/getbarcodes/{domainId}")
	// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)

	public GateWayResponse<?> getBarcodes(@RequestBody List<String> barCode, @PathVariable Long domainId)
			throws RecordNotFoundException {
		log.info("Received Request to getBarcodeDetails:" + barCode);
		System.out.println("Received Request to getBarcodeDetails:" + barCode);

		List<LineItemVo> barCodeDetails = newSaleService.getBarcodes(barCode, domainId);

		return new GateWayResponse<>(HttpStatus.OK, barCodeDetails, "");

	}

	@GetMapping("/isCustomerTaggedToNewSale/{mobileNo}/{invoiceNo}")
	public GateWayResponse<?> getTaggedCustomerForInvoice(@PathVariable String mobileNo,
			@PathVariable String invoiceNo) {
		/*
		 * try { String result=
		 * newSaleService.getTaggedCustomerForInvoice(mobileNo,invoiceNo); }
		 */
		return null;
	}

	/** Loyalty Points APIs **/

	@PostMapping(CommonRequestMappigs.SAVE_LOYALTY)
	public GateWayResponse<?> saveLoyalityPoints(@RequestBody LoyalityPointsVo vo) {
		log.info("Recieved request to saveLoyaltyPoints()" + vo);
		String result = newSaleService.saveLoyaltyPoints(vo);
		return new GateWayResponse<>("loyality points saved successfully", result);

	}

	@GetMapping(CommonRequestMappigs.GET_LOYALTY_POINTS_BY_LOYALTY_ID)
	public GateWayResponse<?> getLoyaltyPoints(@RequestParam Long loyaltyId) throws RecordNotFoundException {
		log.info("Recieved request to getLoyaltyPointsByLoyaltyId()");
		try {
			LoyalityPointsVo loyaltyPoints = newSaleService.getLoyaltyPointsByLoyaltyId(loyaltyId);
			return new GateWayResponse<>(HttpStatus.OK, loyaltyPoints, "");
		} catch (DataNotFoundException dfe) {
			log.error("Getting error while getting loyalty points by loyaltyId : " + loyaltyId);
			return new GateWayResponse<>(dfe.getMsg(), "Please provide valid inputs");
		}

	}

	@GetMapping(CommonRequestMappigs.GET_ALL_LOYALTY_POINTS)
	public GateWayResponse<?> getAllLoyaltyPoints() throws RecordNotFoundException {
		log.info("Recieved request to getAllLoyaltyPoints()");
		try {
			List<LoyalityPointsVo> loyaltyPointsList = newSaleService.getAllLoyaltyPoints();
			return new GateWayResponse<>(HttpStatus.OK, loyaltyPointsList, "");
		} catch (DataNotFoundException dfe) {
			log.error("Getting error while getting all loyalty points");
			return new GateWayResponse<>(dfe.getMsg(), "Please provide valid inputs");
		}

	}

	@GetMapping(CommonRequestMappigs.GET_LOYALTY_POINTS_BY_USER_ID)
	public GateWayResponse<?> getLoyaltyPointsByUserId(@RequestParam Long userId) throws RecordNotFoundException {
		log.info("Recieved request to getLoyaltyPointsByUserId()");
		try {
			LoyalityPointsVo result = newSaleService.getLoyaltyPointsByUserId(userId);
			return new GateWayResponse<>(HttpStatus.OK, result, "");
		} catch (DataNotFoundException dfe) {
			log.error("Getting error while getting loyalty points by userId : " + userId);
			return new GateWayResponse<>(dfe.getMsg(), "Please provide valid inputs");
		}

	}

	@PostMapping(CommonRequestMappigs.SEARCH_LOYALTY_POINTS)
	public GateWayResponse<?> searchLoyaltyPoints(@RequestBody SearchLoyaltyPointsVo vo)
			throws RecordNotFoundException {
		log.info("Recieved request to searchLoyaltyPoints():" + vo);
		List<LoyalityPointsVo> result = newSaleService.searchLoyaltyPoints(vo);

		return new GateWayResponse<>(HttpStatus.OK, result, "");

	}

}
