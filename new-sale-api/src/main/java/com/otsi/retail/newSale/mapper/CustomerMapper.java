package com.otsi.retail.newSale.mapper;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.vo.CustomerVo;

@Component
public class CustomerMapper {

	public CustomerDetailsEntity convertVoToEntity(CustomerVo details) {
		CustomerDetailsEntity entity = new CustomerDetailsEntity();

		entity.setName(details.getName());
		entity.setMobileNumber(details.getMobileNumber());
		entity.setGender(details.getGender());
		entity.setAddress(details.getAddress());
		entity.setAltMobileNo(details.getAltMobileNo());
		entity.setDob(details.getDob());
		entity.setGstNumber(details.getGstNumber());
		entity.setEmail(details.getEmail());

		// BeanUtils.copyProperties(details, entity);

		return entity;
	}

	public CustomerVo convertEntityToVo(CustomerDetailsEntity customerDetailsEntity) {

		CustomerVo vo = new CustomerVo();
		if (customerDetailsEntity.getMobileNumber() != null) {
			vo.setMobileNumber(customerDetailsEntity.getMobileNumber());

		}
		if (customerDetailsEntity.getAltMobileNo() != null) {
			vo.setAltMobileNo(customerDetailsEntity.getAltMobileNo());

		}
		if (customerDetailsEntity.getAddress() != null) {
			vo.setAddress(customerDetailsEntity.getAddress());

		}
		if (customerDetailsEntity.getDob() != null) {
			vo.setDob(customerDetailsEntity.getDob());

		}
		if (customerDetailsEntity.getGender() != null) {
			vo.setGender(customerDetailsEntity.getGender());

		}
		if (customerDetailsEntity.getGstNumber() != null) {
			vo.setGstNumber(customerDetailsEntity.getGstNumber());

		}
		if (customerDetailsEntity.getName() != null) {
			vo.setName(customerDetailsEntity.getName());

		}
		if (customerDetailsEntity.getEmail() != null) {
			vo.setEmail(customerDetailsEntity.getEmail());
		}

		// BeanUtils.copyProperties(customerDetailsEntity, vo);

		return vo;
	}

}