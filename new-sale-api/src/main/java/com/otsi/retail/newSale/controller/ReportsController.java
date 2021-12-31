package com.otsi.retail.newSale.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.service.ReportService;
import com.otsi.retail.newSale.vo.ReportVo;
@RequestMapping("/reports")
@RestController
public class ReportsController {

	private Logger log = LogManager.getLogger(ReportsController.class);

	@Autowired
	private ReportService reportService;

	@GetMapping(value = "/InvoicesGenerated")
	public GateWayResponse<?> getInvoicesGeneratedDetails() {
		try {

		List<ReportVo> rvo = reportService.getInvoicesGeneratedDetails();

		return new GateWayResponse<>(HttpStatus.OK, rvo, "");
		}catch(Exception e)
		{
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());		}

	}
	
	@GetMapping(value = "/getSaleMonthyTrendDetails")
	public GateWayResponse<?> getSaleMonthyTrendDetails() {
		try {

		List<ReportVo> rvo = reportService.getSaleMonthyTrendDetails();

		return new GateWayResponse<>(HttpStatus.OK, rvo, "");
		}catch(Exception e)
		{
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());		}

	}
	
	@GetMapping(value = "/getTopfiveSalesByStore")
	public GateWayResponse<?> getTopfiveSalesByStore() {
		try {

		List<ReportVo> rvo = reportService.getTopfiveSalesByStore();

		return new GateWayResponse<>(HttpStatus.OK, rvo, "");
		}catch(Exception e)
		{
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());		}

	}
	
	@GetMapping(value = "/getsaleSummery")
	public GateWayResponse<?> getsaleSummeryDetails() {
		try {

		List<ReportVo> rvo = reportService.getsaleSummeryDetails();

		return new GateWayResponse<>(HttpStatus.OK, rvo, "");
		}catch(Exception e)
		{
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());		}

	}
}