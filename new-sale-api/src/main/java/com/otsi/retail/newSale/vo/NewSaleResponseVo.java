package com.otsi.retail.newSale.vo;

import java.util.List;

import lombok.Data;

@Data
public class NewSaleResponseVo {
	
	private Long newsaleId;
	
	private Long customerId;
	
	private String customerName;
	
	private String invoiceNumber;
	
	private Long amount;
	
	private List<PaymentAmountTypeVo> paymentAmountTypeId;
	
	private String mobileNumber;

}

