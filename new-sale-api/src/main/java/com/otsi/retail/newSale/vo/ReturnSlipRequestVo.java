package com.otsi.retail.newSale.vo;

import java.util.List;
import java.util.Map;

import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.common.ReturnSlipStatus;

import lombok.Data;
@Data
public class ReturnSlipRequestVo {
	
	
	private Long rsId;
	private Long storeId;
	private String reason;
	private Long customerId;
	private String returnReference;
	private List<TaggedItems> barcodes;
	private String comments;
	private Long createdBy;
	private String mobileNumber;
	private Long totalAmount;
	private ReturnSlipStatus rtStatus;

}
