package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.Exceptions.DataNotFoundException;
import com.otsi.retail.newSale.Exceptions.DuplicateRecordException;
import com.otsi.retail.newSale.Exceptions.InvalidInputException;
import com.otsi.retail.newSale.Exceptions.RecordNotFoundException;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.LoyalityPointsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.ReturnSlipVo;
import com.otsi.retail.newSale.vo.SaleReportVo;
import com.otsi.retail.newSale.vo.SearchLoyaltyPointsVo;
import com.otsi.retail.newSale.vo.UserDataVo;

@Component
public interface NewSaleService {

	String saveNewSaleRequest(NewSaleVo vo) throws InvalidInputException;

	String saveBarcode(BarcodeVo vo) throws DuplicateRecordException;

	BarcodeVo getBarcodeDetails(String barCode, String smId) throws RecordNotFoundException;

	DeliverySlipVo getDeliverySlipDetails(String dsNumber) throws RecordNotFoundException;

	ListOfSaleBillsVo getListOfSaleBills(ListOfSaleBillsVo svo)
			throws RecordNotFoundException, JsonMappingException, JsonProcessingException;

	ListOfDeliverySlipVo getlistofDeliverySlips(ListOfDeliverySlipVo listOfDeliverySlipVo)
			throws RecordNotFoundException;

	List<DeliverySlipEntity> posDayClose();

	double getNewSaleWithHsn(double netAmt) throws JsonMappingException, JsonProcessingException, DataNotFoundException;

	List<NewSaleResponseVo> getNewsaleByCustomerId(Long customerId) throws DataNotFoundException;

	NewSaleVo updateNewSale(NewSaleResponseVo vo) throws RecordNotFoundException;

	List<ReturnSlipVo> getInvoicDetails(InvoiceRequestVo vo) throws RecordNotFoundException;

	CustomerVo getCustomerFromNewSale(String mobileNo) throws DataNotFoundException;

	String posClose(Boolean posclose);

	void tagCustomerToExisitingNewSale(String mobileNo, Long invoiceNo) throws CustomerNotFoundExcecption;

	String saveGiftVoucher(GiftVoucherVo vo) throws DuplicateRecordException;

	GiftVoucherVo getGiftVoucher(String gvNumber) throws InvalidInputException;

	String tagCustomerToGv(Long userId, Long gvId) throws InvalidInputException, DataNotFoundException;

	List<BarcodeVo> getAllBarcodes() throws DataNotFoundException;

	

	SaleReportVo getSaleReport(SaleReportVo srvo) throws RecordNotFoundException;

	List<Long> saveLineItems(List<LineItemVo> lineItems, Long domainId);

	String saveDeliverySlip(DeliverySlipVo vo) throws RecordNotFoundException;

	String editLineItem(LineItemVo lineItem) throws RecordNotFoundException;

	LineItemVo getLineItemByBarcode(String barCode, Long domainId) throws RecordNotFoundException;

	String deleteLineItem(String barCode, Long domainId) throws RecordNotFoundException;

	String getTaggedCustomerForInvoice(String mobileNo, String invoiceNo);

	String deleteDeliverySlipDetails(Long dsId);

	List<LineItemVo> getBarcodes(List<String> barCode, Long domainId) throws RecordNotFoundException;

	String paymentConfirmationFromRazorpay(String razorPayId, boolean payStatus);

	List<GiftVoucherVo> getListOfGiftvouchers() throws RecordNotFoundException;

	NewSaleVo getInvoiceDetails(String orderNumber) throws RecordNotFoundException;
	
	String saveLoyaltyPoints(LoyalityPointsVo loyalityVo) ;

	LoyalityPointsVo getLoyaltyPointsByLoyaltyId(Long loyaltyId) throws RecordNotFoundException;

	List<LoyalityPointsVo> getAllLoyaltyPoints() throws RecordNotFoundException;
	
	LoyalityPointsVo getLoyaltyPointsByUserId(Long userId) throws RecordNotFoundException;

	List<LoyalityPointsVo> searchLoyaltyPoints(SearchLoyaltyPointsVo vo) throws RecordNotFoundException;

	List<GiftVoucherVo> getGvByUserId(Long userId) throws RecordNotFoundException;

}
