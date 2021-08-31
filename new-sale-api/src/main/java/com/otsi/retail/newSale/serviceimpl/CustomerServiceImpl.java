package com.otsi.retail.newSale.serviceimpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.mapper.CustomerMapper;
import com.otsi.retail.newSale.repository.CustomerDetailsRepo;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.vo.CustomerVo;


@Service
public class CustomerServiceImpl implements CustomerService {
	
	private Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public ResponseEntity<?> saveCustomerDetails(CustomerVo details) {
		log.debug("deugging saveCustomerDetails:" + details);
		try {
			Optional<CustomerDetailsEntity> list = customerRepo.findByMobileNumber(details.getMobileNumber());

			if (!list.isPresent()) {

				CustomerDetailsEntity entity = customerMapper.convertVoToEntity(details);// mapper for VO to DTO
				details=customerMapper.convertEntityToVo(customerRepo.save(entity));
				log.warn("we are testing customer is saved succesfully..");
				log.info("customer details saved succesfully..." + details);
				return new ResponseEntity<>("Customer details saved successfully..", HttpStatus.OK);
			} else {
				log.error("Mobile number is already in my records");
				return new ResponseEntity<>("Mobile number is already in my records", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			log.error("error occurs while saving customer details");
			return new ResponseEntity<>("error occurs while saving customer details", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public CustomerVo getCustomerByMobileNumber(String mobileNumber) {
		log.debug("deugging getCustomerByMobileNumber:" + mobileNumber);
		Optional<CustomerDetailsEntity> customerDetails = customerRepo.findByMobileNumber(mobileNumber);
		if (!customerDetails.isPresent()) {
			log.error(
					"Customer is with mobile number " + mobileNumber + " not exists. So please fill all the details..");
			throw new RuntimeException(
					"Customer is with mobile number " + mobileNumber + " not exists. So please fill all the details.."+
					HttpStatus.BAD_REQUEST);
		} else {
			CustomerVo details = customerMapper.convertEntityToVo(customerDetails.get());
			log.warn("we are testing getting customer by mobile number");
			log.info("after getting CustomerByMobileNumber :" + details);
			return details;

			
		} 
	}

}
