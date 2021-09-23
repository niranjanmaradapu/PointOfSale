package com.otsi.retail.promoexchange.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.promoexchange.Entity.CustomerDetailsEntity;
import com.otsi.retail.promoexchange.controller.PromoExchangeController;
import com.otsi.retail.promoexchange.exceptions.DuplicateRecordFoundException;
import com.otsi.retail.promoexchange.exceptions.RecordNotFoundException;
import com.otsi.retail.promoexchange.mapper.CustomerMapper;
import com.otsi.retail.promoexchange.repository.CustomerDetailsRepo;
import com.otsi.retail.promoexchange.vo.CustomerVo;

@Service
public class CustomerServiceImpl implements CustomerService {

	private Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public String saveCustomerDetails(CustomerVo details) throws Exception {
		log.debug("debugging saveCustomerDetails:" + details);
		Optional<CustomerDetailsEntity> list = customerRepo.findByMobileNumber(details.getMobileNumber());

		if (!list.isPresent()) {

			CustomerDetailsEntity entity = customerMapper.convertVoToEntity(details);// mapper for VO to DTO
			customerRepo.save(entity);
			log.warn("wew re checking if customer is saved...");
			log.info("after saving customer details" + entity);
			return "Customer details saved successfully..";
		} else {
			log.error("Record already exists");
			throw new DuplicateRecordFoundException("Record already exists");
		}

	}

	@Override
	public CustomerVo getCustomerByMobileNumber(String mobileNumber) throws Exception {
		log.debug("debugging getCustomerByMobileNumber:" + mobileNumber);
		Optional<CustomerDetailsEntity> customerDetails = customerRepo.findByMobileNumber(mobileNumber);
		if (!customerDetails.isPresent()) {
			log.error("Record already exists");
			throw new RecordNotFoundException("Record Not Found");
		} else {
			CustomerVo details = customerMapper.convertEntityToVo(customerDetails.get());
			log.warn("we are checking if customer is fetching with mobile number...");
			log.info("after fetching  customer details with mobile number" + details);
			return details;

		}
	}

}
