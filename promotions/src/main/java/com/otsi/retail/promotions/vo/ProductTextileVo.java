package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import com.otsi.retail.promotions.common.ProductStatus;

import lombok.Data;

@Data
public class ProductTextileVo {

	private Long productTextileId;
	private Long barcodeTextileId;
	private String parentBarcode;
	private float costPrice;
	private float itemMrp;
	private float itemRsp;
	private Long storeId;
	private String empId;
	private int qty;
	private String promoLabel;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private String uom;
	private Long hsnMasterId;
	private Long domainId;
	private String originalBarcode;
	private LocalDate originalBarcodeCreatedAt;
	private int createForLocation;
	private float valueAdditionCp;
	private String itemCode;
	private String itemSku;
	private ProductStatus status;
	private String attr_22;
	private String attr_23;
	private String attr_24;
	private String attr_25;
	private float value;

}
