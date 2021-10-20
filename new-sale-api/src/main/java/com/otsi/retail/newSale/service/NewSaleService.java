package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleList;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.SaleReportVo;

@Component
public interface NewSaleService {

	String saveNewSaleRequest(NewSaleVo vo);

	String saveBarcode(BarcodeVo vo) throws DuplicateRecordException;

	BarcodeVo getBarcodeDetails(String barCode, String smId) throws RecordNotFoundException;

	String saveDeliverySlip(DeliverySlipVo vo,String enumName) throws RecordNotFoundException;

	DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws RecordNotFoundException;

	ListOfSaleBillsVo getListOfSaleBills(ListOfSaleBillsVo svo) throws RecordNotFoundException, JsonMappingException, JsonProcessingException;

	ListOfDeliverySlipVo getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo) throws RecordNotFoundException;

	String posDayClose();

	double getNewSaleWithHsn(double netAmt) throws JsonMappingException, JsonProcessingException,DataNotFoundException;

	List<NewSaleResponseVo> getNewsaleByCustomerId(Long customerId) throws DataNotFoundException;

	NewSaleVo updateNewSale(NewSaleResponseVo vo) throws RecordNotFoundException;
	NewSaleList getInvoicDetails(InvoiceRequestVo vo) throws  RecordNotFoundException;

	CustomerVo getCustomerFromNewSale(String mobileNo) throws DataNotFoundException;

	String posClose(Boolean posclose);

	void tagCustomerToExisitingNewSale(String mobileNo, Long invoiceNo) throws CustomerNotFoundExcecption;

	String saveGiftVoucher(GiftVoucherVo vo) throws DuplicateRecordException;

	GiftVoucherVo getGiftVoucher(String gvNumber) throws InvalidInputException;

	String tagCustomerToGv(Long userId, Long gvId) throws InvalidInputException,DataNotFoundException;

	List<BarcodeVo> getAllBarcodes() throws DataNotFoundException;

	List<BarcodeVo> getBarcodes(List<String> barCode) throws RecordNotFoundException;

	SaleReportVo getSaleReport(SaleReportVo srvo) throws RecordNotFoundException;

	Long saveLineItems(LineItemVo lineItem);




}
