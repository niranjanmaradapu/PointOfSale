package com.otsi.kalamandhir.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequestVo {

	private long invoiceNo;
	private String barCode;
	private String mobileNo;
	private LocalDate fromDate;
	private LocalDate toDate;
	
}