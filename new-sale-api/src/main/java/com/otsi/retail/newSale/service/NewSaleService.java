package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleList;
import com.otsi.retail.newSale.vo.NewSaleVo;

@Component
public interface NewSaleService {

	String saveNewSaleRequest(NewSaleVo vo);

	String saveBarcode(BarcodeVo vo);

	BarcodeVo getBarcodeDetails(String barCode, String smId) throws Exception;

	String saveDeliverySlip(DeliverySlipVo vo,String enumName);

	DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws Exception;

	ListOfSaleBillsVo getListOfSaleBills(ListOfSaleBillsVo svo) throws Exception;

	ListOfDeliverySlipVo getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo) throws Exception;

	String posDayClose();

	double getNewSaleWithHsn(double netAmt) throws JsonMappingException, JsonProcessingException;

	List<NewSaleResponseVo> getNewsaleByCustomerId(Long customerId);

	NewSaleVo updateNewSale(NewSaleResponseVo vo);
	NewSaleList getInvoicDetails(InvoiceRequestVo vo) throws Exception;

	CustomerVo getCustomerFromNewSale(String mobileNo) throws Exception;

	String posClose(Boolean posclose);

	void tagCustomerToExisitingNewSale(String mobileNo, Long invoiceNo) throws CustomerNotFoundExcecption;

	String saveGiftVoucher(GiftVoucherVo vo);

	GiftVoucherVo getGiftVoucher(String gvNumber) throws Exception;

	String tagCustomerToGv(Long userId, Long gvId);

	List<BarcodeVo> getAllBarcodes() throws Exception;

	List<BarcodeVo> getBarcodes(List<String> barCode) throws Exception;




}
