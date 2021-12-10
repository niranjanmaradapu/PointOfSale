package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.List;

import com.otsi.retail.newSale.common.OrderStatus;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.common.PaymentType;
import com.otsi.retail.newSale.common.SaleNature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewSaleVo {

	private Long newsaleId;

	private CustomerVo customerDetails;

	private Long domainId;

	private Long storeId;
	
	private Long userId;

	private SaleNature natureOfSale;

	private OrderStatus status;

	private String invoiceNumber;

	// private String biller;

	private List<PaymentAmountTypeVo> paymentAmountType;

	private Long recievedAmount;

	private Long grossAmount;

	private Long totalPromoDisc;

	private Long totalManualDisc;

	private String discType;

	private String discApprovedBy;

	// private float roundOff;

	private Long netPayableAmount;

	private LocalDate createdDate;

	private Long taxAmount;

	private Long offlineNumber;

	private String approvedBy;

	private String biller;

	private String reason;

	private float TotaltaxableAmount;
	
	private Long totalMrp;

	private int totalqQty;
	
	private float totalCgst;
	
	private float totalSgst;
	
	private float totalIgst;

	private Long totalNetAmount;
	
	private String empId;

	private List<DeliverySlipVo> dlSlip;

	// private List<BarcodeVo> lineItems;

	private List<LineItemVo> lineItemsReVo;
	
	private String customerName;
	
	private String mobileNumber;

	// private List<HsnDetailsVo> HsnDetailsVo;

}
