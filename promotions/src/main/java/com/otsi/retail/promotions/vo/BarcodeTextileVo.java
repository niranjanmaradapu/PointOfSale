package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BarcodeTextileVo {

	private Long barcodeTextileId;
	private String barcode;
	private Long division;
	private Long section;
	private Long subSection;
	private Long category;
	private String batchNo;
	private String colour;
	private String attr_7;
	private String attr_8;
	private String attr_9;
	private String attr_10;
	private String attr_11;
	private String attr_12;
	private String attr_13;
	private String attr_14;
	private String attr_15;
	private String attr_16;
	private String attr_17;
	private String attr_18;
	private String attr_19;
	private String attr_20;
	private LocalDate fromDate;
	private LocalDate toDate;
	
   private ProductTextileVo productTextile;
   
   private CalculatedDiscountsVo calculatedDiscountsVo;
}
