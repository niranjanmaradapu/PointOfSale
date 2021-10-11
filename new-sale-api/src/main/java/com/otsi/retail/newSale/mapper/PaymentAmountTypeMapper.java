/**
 * 
 */
package com.otsi.retail.newSale.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.vo.PaymentAmountTypeVo;

/**
 * @author vasavi
 *
 */
@Component
public class PaymentAmountTypeMapper {

	// private NewSaleMapper newSaleMapper;

	/*
	 * EntityToVo
	 */
	public PaymentAmountTypeVo EntityToVo(PaymentAmountType dto) {
		PaymentAmountTypeVo vo = new PaymentAmountTypeVo();
		vo.setPaymentAmount(dto.getPaymentAmount());
		//vo.setPaymentType(dto.getPaymentType());
		// vo.setNewsale();
		return vo;

	}

	/*
	 * list of EntityToVo to convert dto's to vo's
	 */
	public List<PaymentAmountTypeVo> EntityToVo(List<PaymentAmountType> dtos) {
		return dtos.stream().map(dto -> EntityToVo(dto)).collect(Collectors.toList());

	}

	/*
	 * 
	 * VoToEntity
	 * 
	 */
	public PaymentAmountType VoToEntity(PaymentAmountTypeVo vo) {
		PaymentAmountType dto = new PaymentAmountType();
		dto.setPaymentAmount(vo.getPaymentAmount());
		//dto.setPaymentType(vo.getPaymentType());
		// vo.setNewsale();
		return dto;

	}
	/*
	 * list of VoToEntity to convert vo's to dto's
	 */

	public List<PaymentAmountType> VoToEntity(List<PaymentAmountTypeVo> vos) {
		return vos.stream().map(vo -> VoToEntity(vo)).collect(Collectors.toList());

	}

}
