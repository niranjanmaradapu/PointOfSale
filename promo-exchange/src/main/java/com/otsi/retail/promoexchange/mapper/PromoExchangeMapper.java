package com.otsi.retail.promoexchange.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;
import com.otsi.retail.promoexchange.vo.BarcodeVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfDeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfReturnSlipsVo;
import com.otsi.retail.promoexchange.vo.ListOfSaleBillsVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;

@Component
public class PromoExchangeMapper {

	public List<PromoExchangeVo> mapEntityToVo(List<PromoExchangeEntity> dtos) {
		return dtos.stream().map(dto -> mapEntityToVo(dto)).collect(Collectors.toList());

	}

	public PromoExchangeVo mapEntityToVo(PromoExchangeEntity dto) {

		PromoExchangeVo nsvo = new PromoExchangeVo();
		nsvo.setInvoiceNumber(dto.getInvoiceNumber());
		nsvo.setBiller(dto.getBiller());
		nsvo.setBillNumber(dto.getBillNumber());
		nsvo.setCreatedDate(dto.getCreatedDate());
		nsvo.setTotalManualDisc(dto.getTotalManualDisc());
		nsvo.setApprovedBy(dto.getApprovedBy());
		nsvo.setReason(dto.getReason());
		nsvo.setOfflineNumber(dto.getOfflineNumber());
		nsvo.setGrossAmount(dto.getGrossAmount());
		nsvo.setNetPayableAmount(dto.getNetPayableAmount());
		nsvo.setRoundOff(dto.getRoundOff());
		nsvo.setBalanceAmount(dto.getBalanceAmount());
		nsvo.setPayType(dto.getPayType());
		nsvo.setRecievedAmount(dto.getRecievedAmount());
		nsvo.setTaxAmount(dto.getTaxAmount());
		nsvo.setCustmerId(dto.getCustmerId());
		return nsvo;
	}

}
