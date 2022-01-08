package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.common.OrderStatus;

import lombok.Data;

@Data
@Component
public class ListOfSaleBillsVo {

	private LocalDate dateFrom;

	private LocalDate dateTo;
	
	private Long storeId;
	
	private Long domainId;

	private LocalDate createdDate;

	private OrderStatus billStatus;

	private String custMobileNumber;

	private String invoiceNumber;

	private String EmpId;

	private Long totalAmount;

	private Long totalDiscount;

	private List<NewSaleVo> newSaleVo;

	// private List<BarcodeVo> barcodeVo;

	// private List<HsnDetailsVo> HsnDetailsVo;

}
