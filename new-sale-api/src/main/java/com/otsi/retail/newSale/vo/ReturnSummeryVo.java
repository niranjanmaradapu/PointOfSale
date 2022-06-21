package com.otsi.retail.newSale.vo;

import lombok.Data;

@Data
public class ReturnSummeryVo {
	
private Long TotalMrp;
	
	private Long billValue;
	
	private Long totalDiscount;
	
	
	  private Long totalTaxableAmount;
	  
	  private Long totalTaxAmount;
	  
	  private Float totalSgst;
	  
	  private Float totalCgst;
	  
	  private Float totalIgst;
	  private Float totalCess;
	  
	  private String taxDescription;
	  
	 private Long storeId;
	

}
