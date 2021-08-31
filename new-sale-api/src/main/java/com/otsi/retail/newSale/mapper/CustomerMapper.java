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

		// BeanUtils.copyProperties(details, entity);

		return entity;
	}

	public CustomerVo convertEntityToVo(CustomerDetailsEntity customerDetailsEntity) {

		CustomerVo vo = new CustomerVo();

		vo.setMobileNumber(customerDetailsEntity.getMobileNumber());
		System.out.println(customerDetailsEntity.getMobileNumber());
		vo.setAltMobileNo(customerDetailsEntity.getAltMobileNo());
		vo.setAddress(customerDetailsEntity.getAddress());
		vo.setDob(customerDetailsEntity.getDob());
		vo.setGender(customerDetailsEntity.getGender());
		vo.setGstNumber(customerDetailsEntity.getGstNumber());
		vo.setName(customerDetailsEntity.getName());
		System.out.println(vo.getName());


		// BeanUtils.copyProperties(customerDetailsEntity, vo);

		return vo;
	}

}