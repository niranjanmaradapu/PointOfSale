package com.otsi.retail.promoexchange.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.promoexchange.Entity.CustomerDetailsEntity;
import com.otsi.retail.promoexchange.exceptions.DuplicateRecordFoundException;
import com.otsi.retail.promoexchange.exceptions.RecordNotFoundException;
import com.otsi.retail.promoexchange.mapper.CustomerMapper;
import com.otsi.retail.promoexchange.repository.CustomerDetailsRepo;
import com.otsi.retail.promoexchange.vo.CustomerVo;


@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public String saveCustomerDetails(CustomerVo details) throws Exception {
		
			Optional<CustomerDetailsEntity> list = customerRepo.findByMobileNumber(details.getMobileNumber());

			if (!list.isPresent()) {

				CustomerDetailsEntity entity = customerMapper.convertVoToEntity(details);// mapper for VO to DTO
				customerRepo.save(entity);
				return "Customer details saved successfully..";
			} else {
				throw new DuplicateRecordFoundException("Record already exists");
			}
		
	}

	@Override
	public CustomerVo getCustomerByMobileNumber(String mobileNumber) throws Exception {

		Optional<CustomerDetailsEntity> customerDetails = customerRepo.findByMobileNumber(mobileNumber);
		if (!customerDetails.isPresent()) {
			throw new RecordNotFoundException("Record Not Found");
		} else {
			CustomerVo details = customerMapper.convertEntityToVo(customerDetails.get());
			return details;

		}
	}

}
