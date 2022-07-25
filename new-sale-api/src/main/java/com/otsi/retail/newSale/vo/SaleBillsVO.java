package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.common.OrderStatus;

import lombok.Data;

@Data
@Component
public class SaleBillsVO {

	private LocalDate dateFrom;

	private LocalDate dateTo;
	
	private Long storeId;
	
	private Long domainId;

	private LocalDate createdDate;

	private OrderStatus billStatus;

	private String custMobileNumber;

	private String invoiceNumber;

	private Long EmpId;

	private Long totalAmount;

	private Long totalDiscount;

	private Page<NewSaleVo> newSale;

	// private List<BarcodeVo> barcodeVo;

	// private List<HsnDetailsVo> HsnDetailsVo;

}
