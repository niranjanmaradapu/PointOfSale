package com.otsi.kalamandhir.vo;

import java.time.LocalDate;
import java.util.List;

import com.otsi.kalamandhir.model.TaggedItems;

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
	private long invoiceNo;
	private String reason;
	private String createdBy;
	private boolean iSReviewed;
	private String reviewedBy;
	private LocalDate createdDate;
	private LocalDate modifiedDate;
	private long amount;
	private boolean isUserTagged;
	private String mobileNumber;
}
