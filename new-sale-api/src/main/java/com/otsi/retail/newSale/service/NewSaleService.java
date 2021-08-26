package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;

@Component
public interface NewSaleService {

	ResponseEntity<?> saveNewSaleRequest(NewSaleVo vo);

	ResponseEntity<?> saveBarcode(BarcodeVo vo);

	ResponseEntity<?> getBarcodeDetails(String barCode);

	ResponseEntity<?> saveDeliverySlip(DeliverySlipVo vo);

	ResponseEntity<?> getDeliverySlipDetails(String dsNumber);

	ResponseEntity<?> getListOfSaleBills(ListOfSaleBillsVo svo);

	ResponseEntity<?> getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo);

	ResponseEntity<?> posDayClose();

	List<HsnDetailsVo> getNewSaleWithHsn() throws JsonMappingException, JsonProcessingException;

	List<NewSaleResponseVo> getNewsaleByCustomerId(Long customerId);

	NewSaleVo updateNewSale(NewSaleResponseVo vo);

}