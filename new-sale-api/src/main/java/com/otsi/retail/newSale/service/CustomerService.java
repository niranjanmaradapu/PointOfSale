package com.otsi.retail.newSale.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.UserDataVo;


@Component
public interface CustomerService {

	ResponseEntity<?> saveCustomerDetails(CustomerVo details) throws Exception;

	CustomerVo getCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundExcecption;

	String saveUserData(UserDataVo vo);

	ResponseEntity<?> getUserByMobileNo(Long mobileNum);

}

