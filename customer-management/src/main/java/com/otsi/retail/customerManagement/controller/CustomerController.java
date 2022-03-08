/**
 * controller for generateReturnSlip,searchFilterbyName,getListOfReturnSlips
 */
package com.otsi.retail.customerManagement.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.customerManagement.gatewayresponse.GateWayResponse;
import com.otsi.retail.customerManagement.model.ReturnSlip;
import com.otsi.retail.customerManagement.service.CustomerService;
import com.otsi.retail.customerManagement.service.ReturnSlipVo;
import com.otsi.retail.customerManagement.vo.CustomerDetailsVo;
import com.otsi.retail.customerManagement.vo.GenerateReturnSlipRequest;
import com.otsi.retail.customerManagement.vo.HsnDetailsVo;
import com.otsi.retail.customerManagement.vo.InvoiceRequestVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;
import com.otsi.retail.customerManagement.vo.RetrnSlipDetailsVo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController

@RequestMapping("/customer")
public class CustomerController {

	private Logger log = LogManager.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	/**
	 * save functionality through service
	 */

	/**
	 * getListOfReturnSlips functionality through service
	 */
	@PostMapping("/getListOfReturnSlips")
	public GateWayResponse<?> getListOfReturnSlips(@RequestBody ListOfReturnSlipsVo vo) {
		log.info("Received request to getListOfReturnSlips:" + vo);
		List<ListOfReturnSlipsVo> listVo = null;

		listVo = customerService.getListOfReturnSlips(vo);
		return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");
	}

	@DeleteMapping("/DeleteReturnSlip")
	public GateWayResponse<?> deleteReturnSlip(@RequestParam int rsId) {
		log.info("Received request to deleteReturnSlips:" + rsId);
		// List<ListOfReturnSlipsVo> listVo = null;

		String msg = customerService.deleteReturnSlips(rsId);
		return new GateWayResponse<>(HttpStatus.OK, msg, "Success");
	}

	@PostMapping("/getInvoiceDetails")
	@CircuitBreaker(name = "newSale", fallbackMethod = "getInvoiceFallback")
	public GateWayResponse<?> getInvoiceDetails(@RequestBody InvoiceRequestVo searchVo) throws Exception {
		log.info("Received request to getInvoiceDetails:" + searchVo);
		List<ReturnSlipVo> newSale = customerService.getInvoiceDetailsFromNewSale(searchVo);
		return new GateWayResponse<>(HttpStatus.OK, newSale, "");

	}

	public GateWayResponse<?> getInvoiceFallback(Exception e) {
		log.error("Third Party Service Down");
		return new GateWayResponse<>("Third Party Service  is Down", null);
	}

	@PostMapping("/createReturnSlip")
	public GateWayResponse<?> createReturnSlip(@RequestBody GenerateReturnSlipRequest request) throws Exception {
		log.info("Received request to createReturnSlip:" + request);
		String message = customerService.createReturnSlip(request);
		return new GateWayResponse<>("ReturnSlip Created Successfully", message);
	}

	@PostMapping("/updateReturnSlip")
	public GateWayResponse<?> updateReturnSlip(@RequestParam String rtNumber,
			@RequestBody GenerateReturnSlipRequest request) {
		log.info("Received request to updateReturnSlip:" + rtNumber + "and the request is:" + request);
		String message = customerService.updateReturnSlip(rtNumber, request);
		return new GateWayResponse<>("Return Slip Details Updated Successfully", message);

	}

	@GetMapping("/getCustomerDetails/{mobileNo}")
	@CircuitBreaker(name = "GetCustomerDetailsCB", fallbackMethod = "getCustomerFallback")
	public GateWayResponse<?> getCustomerDetails(@PathVariable String mobileNo) throws Exception {
		log.info("Received request to getCustomerDetails:" + mobileNo);
		CustomerDetailsVo customerVo = customerService.getCustomerFDetailsFromInvoice(mobileNo);
		return new GateWayResponse<>(HttpStatus.OK, customerVo, "");

	}

	public GateWayResponse<?> getCustomerFallback(Exception ex) {
		log.error("Third Party Service Down");
		return new GateWayResponse<>("Third Party Service  is Down", null);
	}

	@GetMapping("/getAllListOfReturnSlips")
	public GateWayResponse<?> getAllListOfReturnSlips(@RequestParam Long storeId,@RequestParam Long domainId) {
		log.info("Received request to getAllListOfReturnSlips()");
		List<ListOfReturnSlipsVo> listVo = null;

		listVo = customerService.getAllListOfReturnSlips(storeId,domainId);

		return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");

	}

	@GetMapping("/getReturnSlipsDetails")
	@CircuitBreaker(name = "GetReturnSlipDetailsCB", fallbackMethod = "returnSlipDetailsFallback")
	public GateWayResponse<?> ReturnSlipsDeatils(@RequestParam String rtNumber)
			throws JsonMappingException, JsonProcessingException, URISyntaxException {
		log.info("Received request to ReturnSlipsDeatils():" + rtNumber);
		RetrnSlipDetailsVo listVo = null;

		listVo = customerService.ReturnSlipsDeatils(rtNumber);

		return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");

	}

	public GateWayResponse<?> returnSlipDetailsFallback(Exception ex) {
		log.error("Third Party Service Down");
		return new GateWayResponse<>("Third Party Service  is Down", null);
	}

	@GetMapping("/getHsnDetails/{netAmt}")
	@CircuitBreaker(name = "GetHsnDetailsCB", fallbackMethod = "hsnDetailsFallback")
	public GateWayResponse<?> getHsnDetails(@PathVariable double netAmt)
			throws JsonMappingException, JsonProcessingException {

		log.info("Recieved request to getNewSaleWithHsn()");
		HsnDetailsVo netamt = customerService.getHsnDetails(netAmt);
		return new GateWayResponse<>(HttpStatus.OK, netamt, "");

	}
	
	public GateWayResponse<?> hsnDetailsFallback(Exception ex) {
		log.error("Third Party Service Down");
		return new GateWayResponse<>("Third Party Service  is Down", null);
	}

	@GetMapping("/getReturnSlipbyRtNo/{rtNo}")
	public GateWayResponse<?> getReturnSlip(@PathVariable String rtNo) {
		try {
			log.info("Recieved request to getReturnSlip() with rtNo : {}", rtNo);
			ReturnSlip res = customerService.getReturnSlipByrtNo(rtNo);
			return new GateWayResponse<>(HttpStatus.OK, res, "");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(), "");

		}

	}

}
