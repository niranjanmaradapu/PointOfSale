package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import com.otsi.retail.promotions.common.ProductStatus;

import lombok.Data;

@Data
public class ProductTextileVo {

	private Long productTextileId;

	private String name;

	private String barcode;

	private Long division;

	private Long section;

	private Long subSection;

	private Long category;

	private String batchNo;

	private String colour;

	private String parentBarcode;
    
	private float costPrice;
    
	//listing price
	private float itemMrp;

	private String empId;

	private Long storeId;

	private Long domainId;

	private LocalDate fromDate;

	private LocalDate toDate;

	private String uom;

	private int qty;

	private String hsnCode;

	private LocalDate originalBarcodeCreatedAt;

	private ProductStatus status;

	private float value;
	
	private CalculatedDiscountsVo calculatedDiscountsVo;
}
