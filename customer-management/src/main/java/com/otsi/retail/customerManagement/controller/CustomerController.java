/**
 * controller for generateReturnSlip,searchFilterbyName,getListOfReturnSlips
 */
package com.otsi.retail.customerManagement.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.otsi.retail.customerManagement.gatewayresponse.GateWayResponse;
import com.otsi.retail.customerManagement.service.CustomerService;
import com.otsi.retail.customerManagement.service.ReturnSlipVo;
import com.otsi.retail.customerManagement.vo.CustomerDetailsVo;
import com.otsi.retail.customerManagement.vo.GenerateReturnSlipRequest;
import com.otsi.retail.customerManagement.vo.HsnDetailsVo;
import com.otsi.retail.customerManagement.vo.InvoiceRequestVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;
import com.otsi.retail.customerManagement.vo.NewSaleList;
import com.otsi.retail.customerManagement.vo.RetrnSlipDetailsVo;

@RestController

@RequestMapping("/customer")
public class CustomerController {

	private Logger log = LoggerFactory.getLogger(CustomerController.class);

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

	@PostMapping("/getInvoiceDetails")
	public GateWayResponse<?> getInvoiceDetails(@RequestBody InvoiceRequestVo searchVo) throws Exception {
		log.info("Received request to getInvoiceDetails:" + searchVo);
		List<ReturnSlipVo> newSale = customerService.getInvoiceDetailsFromNewSale(searchVo);
		return new GateWayResponse<>(HttpStatus.OK, newSale, "");

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
	public GateWayResponse<?> getCustomerDetails(@PathVariable String mobileNo) throws Exception {
		log.info("Received request to getCustomerDetails:" + mobileNo);
		CustomerDetailsVo customerVo = customerService.getCustomerFDetailsFromInvoice(mobileNo);
		return new GateWayResponse<>(HttpStatus.OK, customerVo, "");

	}

	@GetMapping("/getAllListOfReturnSlips")
	public GateWayResponse<?> getAllListOfReturnSlips() {
		log.info("Received request to getAllListOfReturnSlips()");
		List<ListOfReturnSlipsVo> listVo = null;

		listVo = customerService.getAllListOfReturnSlips();

		return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");

	}

	@GetMapping("/getReturnSlipsDetails")
	public GateWayResponse<?> ReturnSlipsDeatils(@RequestParam String rtNumber) throws JsonMappingException, JsonProcessingException, URISyntaxException {
		log.info("Received request to ReturnSlipsDeatils():" + rtNumber);
		RetrnSlipDetailsVo listVo = null;

		listVo = customerService.ReturnSlipsDeatils(rtNumber);

		return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");

	}

	@GetMapping("/getHsnDetails/{netAmt}")
	public GateWayResponse<?> getHsnDetails(@PathVariable double netAmt)
			throws JsonMappingException, JsonProcessingException {

		log.info("Recieved request to getNewSaleWithHsn()");
		HsnDetailsVo netamt = customerService.getHsnDetails(netAmt);
		return new GateWayResponse<>(HttpStatus.OK, netamt, "");

	}

}
