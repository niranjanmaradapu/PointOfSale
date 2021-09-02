package com.otsi.kalamandhir.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.kalamandhir.gatewayresponse.GateWayResponse;
import com.otsi.kalamandhir.model.Reason;
import com.otsi.kalamandhir.serviceimpl.ReasonService;

@RestController
@RequestMapping("/reason")
public class ReasonController {
	@Autowired
	private ReasonService reasonService;

	@GetMapping()
	public GateWayResponse<?> getAllReason() {
		try {
		List<Reason> reasons=reasonService.getAllReasons();
			return new GateWayResponse<>(HttpStatus.OK, reasons, "");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
	}

	@PostMapping()
	public GateWayResponse<?> saveReason(@RequestBody Reason reason) {

		try {
			String result = reasonService.saveReason(reason);
			return new GateWayResponse<>(HttpStatus.OK, result);
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@DeleteMapping()
	public GateWayResponse<?> deleteReason(Long id) {
		try {
			String result=	reasonService.deleteReason(id);
			return new GateWayResponse<>(HttpStatus.OK, result);

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

}
