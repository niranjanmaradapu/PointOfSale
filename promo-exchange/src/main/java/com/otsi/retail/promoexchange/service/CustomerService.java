package com.otsi.retail.promoexchange.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.promoexchange.vo.CustomerVo;


@Component
public interface CustomerService {

	ResponseEntity<?> saveCustomerDetails(CustomerVo details) throws Exception;

	ResponseEntity<?> getCustomerByMobileNumber(String mobileNumber);

}

