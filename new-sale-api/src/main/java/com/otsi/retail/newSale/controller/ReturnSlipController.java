package com.otsi.retail.newSale.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.service.ReturnslipService;
import com.otsi.retail.newSale.vo.GenerateReturnSlipRequest;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;
import com.otsi.retail.newSale.vo.ReturnSlipVo;

@RestController
@RequestMapping("/return_slip")

public class ReturnSlipController {
	
	
	private Logger log = LogManager.getLogger(ReturnSlipController.class);

	@Autowired
	private ReturnslipService returnSlipService;

	/**
	 * save functionality through service
	 */

	/**
	 * getListOfReturnSlips functionality through service
	 */
	/*@PostMapping("/getListOfReturnSlips")
	public GateWayResponse<?> getListOfReturnSlips(@RequestBody ListOfReturnSlipsVo vo) {
		log.info("Received request to getListOfReturnSlips:" + vo);
		List<ListOfReturnSlipsVo> listVo = null;

		listVo = returnSlipService.getListOfReturnSlips(vo);
		return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");
	}

	@DeleteMapping("/DeleteReturnSlip")
	public GateWayResponse<?> deleteReturnSlip(@RequestParam int rsId) {
		log.info("Received request to deleteReturnSlips:" + rsId);
		// List<ListOfReturnSlipsVo> listVo = null;+

		String msg = returnSlipService.deleteReturnSlips(rsId);
		return new GateWayResponse<>(HttpStatus.OK, msg, "Success");
	}
	
	@PostMapping("/createReturnSlip")
	public GateWayResponse<?> createReturnSlip(@RequestBody GenerateReturnSlipRequest request) throws Exception {
		log.info("Received request to createReturnSlip:" + request);
		String message = returnSlipService.createReturnSlip(request);
		return new GateWayResponse<>("ReturnSlip Created Successfully", message);
	}

	@PostMapping("/updateReturnSlip")
	public GateWayResponse<?> updateReturnSlip(@RequestParam String rtNumber,
			@RequestBody GenerateReturnSlipRequest request) throws Exception {
		log.info("Received request to updateReturnSlip:" + rtNumber + "and the request is:" + request);
		String message = returnSlipService.updateReturnSlip(rtNumber, request);
		return new GateWayResponse<>("Return Slip Details Updated Successfully", message);

	}*/
	@PostMapping("/createReturnSlip")
	public GateWayResponse<?> createReturnSlip(@RequestBody ReturnSlipRequestVo returnSlipRequestVo) throws Exception {
		log.info("Received request to createReturnSlip:" + returnSlipRequestVo);
		List<ReturnSlipRequestVo> returnSlip = returnSlipService.createReturnSlip(returnSlipRequestVo);
		return new GateWayResponse<>("Return Slip created Successfully", returnSlip);

	}
	
	@GetMapping("/getReturnSlip")
	public GateWayResponse<?> getReturnSlip(@RequestParam String returnReferenceNumber,@RequestParam Long storeId) throws Exception {
		log.info("Received request to updateReturnSlip:" + returnReferenceNumber + "and storeId is:" + storeId);
		ReturnSlipRequestVo returnSlip = returnSlipService.getReturnSlip(returnReferenceNumber, storeId);
		return new GateWayResponse<>("Return Slip Details getting Successfully", returnSlip);

	}

}
