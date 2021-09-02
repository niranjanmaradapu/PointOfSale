package com.otsi.retail.promoexchange.serviceimpl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.promoexchange.Entity.CustomerDetailsEntity;
import com.otsi.retail.promoexchange.mapper.CustomerMapper;
import com.otsi.retail.promoexchange.repository.CustomerDetailsRepo;
import com.otsi.retail.promoexchange.service.CustomerService;
import com.otsi.retail.promoexchange.vo.CustomerVo;


@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDetailsRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public ResponseEntity<?> saveCustomerDetails(CustomerVo details) {
		try {
			Optional<CustomerDetailsEntity> list = customerRepo.findByMobileNumber(details.getMobileNumber());

			if (!list.isPresent()) {

				CustomerDetailsEntity entity = customerMapper.convertVoToEntity(details);// mapper for VO to DTO
				customerRepo.save(entity);
				return new ResponseEntity<>("Customer details saved successfully..", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Mobile number is already in my records", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("error occurs while saving customer details", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<?> getCustomerByMobileNumber(String mobileNumber) {

		Optional<CustomerDetailsEntity> customerDetails = customerRepo.findByMobileNumber(mobileNumber);
		if (!customerDetails.isPresent()) {
			return new ResponseEntity<>(
					"Customer is with mobile number " + mobileNumber + " not exists. So please fill all the details..",
					HttpStatus.BAD_REQUEST);
		} else {
			CustomerVo details = customerMapper.convertEntityToVo(customerDetails.get());
			return new ResponseEntity<>(details, HttpStatus.OK);

		}
	}

}
