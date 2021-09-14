package com.otsi.kalamandhir.vo;

import java.time.LocalDate;
import java.util.List;

import com.otsi.kalamandhir.utils.PaymentType;
import com.otsi.kalamandhir.utils.SaleNature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewSaleVo {

	private CustomerDetailsVo customerDetailsVo;

    private SaleNature natureOfSale;
    
	private String invoiceNumber;
	
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

	private List<DeliverySlipVo> dlSlip;
	
}
