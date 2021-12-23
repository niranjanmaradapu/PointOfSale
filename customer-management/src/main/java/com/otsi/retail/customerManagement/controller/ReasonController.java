package com.otsi.retail.customerManagement.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.customerManagement.gatewayresponse.GateWayResponse;
import com.otsi.retail.customerManagement.model.Reason;
import com.otsi.retail.customerManagement.service.ReasonService;

@RestController
@RequestMapping("/reason")
public class ReasonController {

	private Logger log = LogManager.getLogger(ReasonController.class);
	@Autowired
	private ReasonService reasonService;

	@GetMapping()
	public GateWayResponse<?> getAllReason() {
		log.info("Received request to getAllReason()");
		List<Reason> reasons = reasonService.getAllReasons();
		return new GateWayResponse<>(HttpStatus.OK, reasons, "Success");

	}

	@PostMapping()
	public GateWayResponse<?> saveReason(@RequestBody Reason reason) {
		log.info("Received request to saveReason():" + reason);
		String result = reasonService.saveReason(reason);
		return new GateWayResponse<>("Reason Saved Successfully", result);

	}

	@DeleteMapping()
	public GateWayResponse<?> deleteReason(Long id) {
		log.info("Received request to deleteReason():" + id);
		String result = reasonService.deleteReason(id);
		return new GateWayResponse<>("Reason Deleted successfully", result);

	}

}
