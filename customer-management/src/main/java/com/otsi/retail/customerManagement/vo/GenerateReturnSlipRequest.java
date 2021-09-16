package com.otsi.retail.customerManagement.vo;

import java.time.LocalDate;
import java.util.List;

import com.otsi.retail.customerManagement.model.TaggedItems;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateReturnSlipRequest {

	private List<TaggedItems> barcodes;
	private String rtNumber;
	private String crNumber;
	private String invoiceNo;
	private String reason;
	private String createdBy;
	private Boolean iSReviewed;
	private String reviewedBy;
	private LocalDate createdDate;
	private LocalDate modifiedDate;
	private long amount;
	private Boolean isUserTagged;
	private String mobileNumber;
}