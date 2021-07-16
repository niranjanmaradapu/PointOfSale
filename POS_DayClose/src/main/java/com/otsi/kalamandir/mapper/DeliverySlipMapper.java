package com.otsi.kalamandir.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.otsi.kalamandir.model.DeliverySlipModel;

import com.otsi.kalamandir.vo.DelivarySlipVo;
@Component
public class DeliverySlipMapper {
	/* @ MAPPER to convert DelivarySlipVo to DeliverySlipModel */
	public DeliverySlipModel convertVoToEntity(DelivarySlipVo dsvo, DeliverySlipModel dsmodel) {

		dsmodel.setSpId(dsvo.getSpId());
		dsmodel.setSpName(dsvo.getSpName());
		dsmodel.setBarcode(dsvo.getBarcode());
		dsmodel.setUnitsOfMeasurements(dsvo.getUnitsOfMeasurements());
		dsmodel.setCloseDate(LocalDate.now());
		dsmodel.setDsCreatedDate(LocalDateTime.now());
		dsmodel.setDsOpenDate(dsvo.getDsOpenDate());
		dsmodel.setOpen(dsvo.isOpen());
		return dsmodel;
		

	}

	/* MAPPER to convert DeliverySlipModel to DelivarySlipVo */
	public DelivarySlipVo convertEntityToVo(DeliverySlipModel dsmodel) {
		
		DelivarySlipVo dsvo=new DelivarySlipVo();
		BeanUtils.copyProperties(dsmodel, dsvo);
		return dsvo;
		
	}

	

}
