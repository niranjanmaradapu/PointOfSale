package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateReturnSlipRequest {
	
	private List<TaggedItemsVo> barcodes;
	private String rtNumber;
	private String crNumber;
	private Long storeId;
	private String invoiceNo;
	private String reason;
	private Long createdBy;
	private Boolean iSReviewed;
	private String reviewedBy;
	private Long customerId;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private long amount;
	private Boolean isUserTagged;
	private String mobileNumber;
	 private String customerName;
	 private Long totalAmount;
	 private Long domianId;
	 private int qty;

}
