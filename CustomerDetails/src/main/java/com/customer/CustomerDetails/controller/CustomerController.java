package com.customer.CustomerDetails.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.CustomerDetails.common.Status;
import com.customer.CustomerDetails.customer.CustomerDetails;
import com.customer.CustomerDetails.service.CustomerService;

@RestController
@CrossOrigin
@RequestMapping(Status.CUSTOMER)
public class CustomerController {

	@Autowired
	private CustomerService service;

	@PostMapping(path = Status.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveCustomerDetails(@Valid @RequestBody CustomerDetails details) {
		try {
			ResponseEntity<?> result = service.saveCustomerDetails(details);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
