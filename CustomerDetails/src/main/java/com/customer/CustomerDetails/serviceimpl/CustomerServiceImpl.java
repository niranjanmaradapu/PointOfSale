package com.customer.CustomerDetails.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.customer.CustomerDetails.Entity.CustomerDetailsEntity;
import com.customer.CustomerDetails.customer.CustomerDetails;
import com.customer.CustomerDetails.mapper.CustomerMapper;
import com.customer.CustomerDetails.repository.CustomerDetailsRepo;
import com.customer.CustomerDetails.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public ResponseEntity<?> saveCustomerDetails(CustomerDetails details) throws Exception {
		try {
			Optional<CustomerDetailsEntity> list = customerRepo.findByMobileNumber(details.getMobileNumber());

			if (list.isEmpty()) {

				CustomerDetailsEntity entity = customerMapper.convertVoToEntity(details);
				customerRepo.save(entity);
				return new ResponseEntity<>("Customer details saved successfully..", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Mobile number is already in my records", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("error occurs while saving customer details", HttpStatus.BAD_REQUEST);
		}

	}

}
