package com.otsi.retail.newSale.vo;

import com.otsi.retail.newSale.common.AccountStatus;
import com.otsi.retail.newSale.common.AccountType;
import com.otsi.retail.newSale.common.PaymentStatus;
import com.otsi.retail.newSale.common.PaymentType;

import lombok.Data;
@Data
public class LedgerLogBookVo extends BaseEntityVo{
	
	private Long ledgerLogBookId;

	private AccountType transactionType;
	
	private AccountType accountType;

	private String comments;

	private Long storeId;

	private Long customerId;
	
	private Long amount;

	private Long accountingBookId;

	private AccountStatus status;

	private PaymentType paymentType;
	
	private PaymentStatus paymentStatus;
	
	private String referenceNumber;
	
	private String returnSlipNumber;
	
	private Boolean isReturned;
	
	private String mobileNumber;

}
