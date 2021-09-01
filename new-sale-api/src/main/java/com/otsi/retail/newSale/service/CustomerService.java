package com.otsi.retail.newSale.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.controller.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.vo.CustomerVo;


@Component
public interface CustomerService {

	ResponseEntity<?> saveCustomerDetails(CustomerVo details) throws Exception;

	CustomerVo getCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundExcecption;

}

