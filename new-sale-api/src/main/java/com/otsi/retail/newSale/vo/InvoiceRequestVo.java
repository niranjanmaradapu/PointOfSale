package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceRequestVo {

	private String invoiceNo;
	private String barCode;
	private int domianId;
	private String mobileNo;
	private LocalDate fromDate;
	private LocalDate toDate;
}


