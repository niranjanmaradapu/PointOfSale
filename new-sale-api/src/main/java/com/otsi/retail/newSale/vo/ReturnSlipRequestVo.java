package com.otsi.retail.newSale.vo;

import java.util.List;
import java.util.Map;

import com.otsi.retail.newSale.Entity.TaggedItems;

import lombok.Data;
@Data
public class ReturnSlipRequestVo {
	
	
	
	private Long storeId;
	private String returnReference;
	private String barcode;
	private Boolean isreturned;
	private List<TaggedItems> barcodes;
	private Long returnedAmount;
	private String invoiceNumber;

}
