package com.otsi.retail.promoexchange.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promoexchange.common.PaymentType;

import lombok.Data;

@Data
@Component
public class PromoExchangeVo {

	private CustomerVo customerDetails;
    
	private Long invoiceNumber;
	
	private String biller;

	private PaymentType payType;

	private String custmerId;

	private Long grossAmount;

	private Long totalPromoDisc;

	private Long totalManualDisc;

	private float roundOff;

	private Long netPayableAmount;
	
	private LocalDate createdDate;

	private Long taxAmount;
    
	private Long offlineNumber;
	
	private String approvedBy;
	
	private String reason;
	
	private List<ListOfReturnSlipsVo> returnSlips;

	private List<DeliverySlipVo> dlSlip;

	private Long recievedAmount;
	
	private Long balanceAmount;

}
