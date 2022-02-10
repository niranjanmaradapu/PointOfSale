package com.otsi.retail.customerManagement.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequestVo {

	private String invoiceNo;
	private String barCode;
	private String mobileNo;
	private LocalDate fromDate;
	private LocalDate toDate;
	
}