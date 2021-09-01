/**
 * controller for generateReturnSlip,searchFilterbyName,getListOfReturnSlips
 */
package com.otsi.kalamandhir.controller;

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
import org.springframework.web.bind.annotation.RestController;
import com.otsi.kalamandhir.gatewayresponse.GateWayResponse;
import com.otsi.kalamandhir.service.CustomerService;
import com.otsi.kalamandhir.vo.CustomerDetailsVo;
import com.otsi.kalamandhir.vo.GenerateReturnSlipRequest;
import com.otsi.kalamandhir.vo.InvoiceRequestVo;
import com.otsi.kalamandhir.vo.ListOfReturnSlipsVo;
import com.otsi.kalamandhir.vo.NewSaleList;

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

		try {
			listVo = customerService.getListOfReturnSlips(vo);
			// if vo is null,it will print failure message.Otherwise,it will give the
			// details;
			if (listVo == null) {
				return new GateWayResponse<>(false, HttpStatus.OK, "no record found with the given information",
						"Failure");
			}
			return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");
		} catch (Exception ex) {
			return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
		// return new
		// GateWayResponse<List<ListOfReturnSlipsVo>>(customerService.getListOfReturnSlips(vo));

	}

	@GetMapping("/getInvoiceDetails")
	public GateWayResponse<?> getInvoiceDetails(@RequestBody InvoiceRequestVo searchVo) {

		try {
			NewSaleList newSale = customerService.getInvoiceDetailsFromNewSale(searchVo);
			return new GateWayResponse<>(HttpStatus.OK, newSale, "");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@PostMapping("/createReturnSlip")
	public GateWayResponse<?> createReturnSlip(@RequestBody GenerateReturnSlipRequest request) {
		try {
			String message = customerService.createReturnSlip(request);
			return new GateWayResponse<>(HttpStatus.CREATED, message);
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getCustomerDetails/{mobileNo}")
	public GateWayResponse<?> getCustomerDetails(@PathVariable String mobileNo) {
		try {
			CustomerDetailsVo customerVo = customerService.getCustomerFDetailsFromInvoice(mobileNo);
			return new GateWayResponse<>(HttpStatus.OK, customerVo, "");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

	@GetMapping("/getAllListOfReturnSlips")
	public GateWayResponse<?> getAllListOfReturnSlips() {

		List<ListOfReturnSlipsVo> listVo = null;

		try {
			listVo = customerService.getAllListOfReturnSlips();
			// if vo is null,it will print failure message.Otherwise,it will give the
			// details;
			if (listVo == null) {
				return new GateWayResponse<>(false, HttpStatus.OK, "no record found with the given information",
						"Failure");
			}
			return new GateWayResponse<>(HttpStatus.OK, listVo, "Success");
		} catch (Exception ex) {
			return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
		// return new
		// GateWayResponse<List<ListOfReturnSlipsVo>>(customerService.getListOfReturnSlips(vo));

	}

}
