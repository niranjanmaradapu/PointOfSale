package com.customer.CustomerDetails.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.customer.CustomerDetails.customer.CustomerDetails;

@Component
public interface CustomerService {

	ResponseEntity<?> saveCustomerDetails(CustomerDetails details) throws Exception;

}
