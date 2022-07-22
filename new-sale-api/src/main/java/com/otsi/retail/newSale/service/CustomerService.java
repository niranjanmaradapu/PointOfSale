package com.otsi.retail.newSale.service;

import org.springframework.stereotype.Component;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.UserDataVo;

@Component
public interface CustomerService {

	String saveCustomerDetails(CustomerVo details) throws DuplicateRecordException;

	CustomerVo getCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundExcecption;

	String saveUserData(UserDataVo vo) throws DuplicateRecordException;

	UserDataVo getUserByMobileNo(Long mobileNum) throws RecordNotFoundException;

}
