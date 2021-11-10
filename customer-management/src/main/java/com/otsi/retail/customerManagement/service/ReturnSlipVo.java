package com.otsi.retail.customerManagement.service;


import lombok.Data;

@Data
public class ReturnSlipVo {

	
	private String barcode;
	private int quantity;
	private Long netValue;
}
