package com.otsi.retail.promoexchange.service;

import org.springframework.stereotype.Component;
import com.otsi.retail.promoexchange.vo.CustomerVo;

@Component
public interface CustomerService {

	String saveCustomerDetails(CustomerVo details) throws Exception;

	CustomerVo getCustomerByMobileNumber(String mobileNumber) throws Exception;

}
