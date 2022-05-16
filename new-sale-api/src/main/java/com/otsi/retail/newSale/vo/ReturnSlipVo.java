package com.otsi.retail.newSale.vo;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.common.ReturnSlipStatus;

import lombok.Data;

@Data
public class ReturnSlipVo {

	
	private String barcode;
	private int quantity;
	private Long netValue;
	private int rsId;
	private String rtNo;
	private String crNo;
	private Long storeId;
	private String settelmentInfo;
	private Long amount;
	private String mobileNumber;
	private String customerName;
	private Boolean isReviewed;
	private ReturnSlipStatus rtStatus;
	private long domianId;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private String reviewedBy;
	private Long createdBy;
	private List<TaggedItems> taggedItems;
}
