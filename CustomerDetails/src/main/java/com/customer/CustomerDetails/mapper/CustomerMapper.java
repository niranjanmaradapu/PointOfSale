package com.customer.CustomerDetails.mapper;

import org.springframework.stereotype.Component;
import com.customer.CustomerDetails.Entity.CustomerDetailsEntity;
import com.customer.CustomerDetails.customer.CustomerDetails;

@Component
public class CustomerMapper {

	public CustomerDetailsEntity convertVoToEntity(CustomerDetails details) {
		CustomerDetailsEntity entity = new CustomerDetailsEntity();

		entity.setName(details.getName());
		entity.setMobileNumber(details.getMobileNumber());
		entity.setGender(details.getGender());
		entity.setAddress(details.getAddress());
		entity.setAltMobileNo(details.getAltMobileNo());
		entity.setDob(details.getDob());
		entity.setGstNumber(details.getGstNumber());

		return entity;
	}

}
