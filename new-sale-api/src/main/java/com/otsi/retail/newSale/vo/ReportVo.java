package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportVo {
	
	private Month Month;
	private Long amount;
	private Long storeId;
	
	private String name;
	

}