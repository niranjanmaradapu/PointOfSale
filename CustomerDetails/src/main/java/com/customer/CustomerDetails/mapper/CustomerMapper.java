package com.customer.CustomerDetails.mapper;

import org.springframework.beans.BeanUtils;
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

		// BeanUtils.copyProperties(details, entity);

		return entity;
	}

	public CustomerDetails convertEntityToVo(CustomerDetailsEntity customerDetailsEntity) {

		CustomerDetails vo = new CustomerDetails();

		vo.setMobileNumber(customerDetailsEntity.getMobileNumber());
		vo.setAltMobileNo(customerDetailsEntity.getAltMobileNo());
		vo.setAddress(customerDetailsEntity.getAddress());
		vo.setDob(customerDetailsEntity.getDob());
		vo.setGender(customerDetailsEntity.getGender());
		vo.setGstNumber(customerDetailsEntity.getGstNumber());
		vo.setName(customerDetailsEntity.getName());

		// BeanUtils.copyProperties(customerDetailsEntity, vo);

		return vo;
	}

}
