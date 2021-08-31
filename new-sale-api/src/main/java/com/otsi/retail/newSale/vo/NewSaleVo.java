package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.common.PaymentType;
import com.otsi.retail.newSale.common.SaleNature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class NewSaleVo {

	private Long newsaleId;

	private CustomerVo customerDetails;

	private SaleNature natureOfSale;

	private Long invoiceNumber;

	private String biller;
	
	private List<PaymentAmountTypeVo> paymentAmountType;

	private Long recievedAmount;

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

	private List<DeliverySlipVo> dlSlip;

}
