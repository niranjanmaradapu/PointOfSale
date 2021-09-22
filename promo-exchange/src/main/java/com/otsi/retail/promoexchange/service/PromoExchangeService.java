package com.otsi.retail.promoexchange.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;
import com.otsi.retail.promoexchange.vo.BarcodeVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfDeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfReturnSlipsVo;
import com.otsi.retail.promoexchange.vo.ListOfSaleBillsVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;


@Service
public interface PromoExchangeService {

	
	String savePromoItemExchangeRequest(PromoExchangeVo vo);
	
	DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws Exception;
	
	List<ListOfReturnSlipsVo> getListOfRetunSlips()
			throws JsonMappingException, JsonProcessingException;

	List<PromoExchangeVo> getListOfSaleBills();

	List<PromoExchangeVo> getSaleBillByBillNumber(String billNumber);
    
	
}
