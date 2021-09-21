package com.otsi.retail.customerManagement.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class RetrnSlipDetailsVo {
	
	 private List<BarcodeVo>  barcode;
	 
	 private HsnDetailsVo  hsnCode;

	 
		/*
		 * private String qty;
		 * 
		 * private String section;
		 * 
		 * private Long mrp;
		 * 
		 * private String hsnCode;
		 * 
		 * private float sgst;
		 * 
		 * private float cgst;
		 * 
		 * private float igst;
		 */
	 
	 private String rtNumber;
	 
	 private LocalDate createdDate;
		
	
	

}