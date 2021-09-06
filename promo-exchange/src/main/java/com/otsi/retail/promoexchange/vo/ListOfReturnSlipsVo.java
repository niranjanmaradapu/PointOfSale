package com.otsi.retail.promoexchange.vo;


import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ListOfReturnSlipsVo {

	private Date dateFrom;

	private Date dateTo;

	private String barcode;

	private String rtStatus;

	private String rtNumber;

	private String creditNote;
	private String createdBy;
	private Boolean rtReviewStatus;

	// private List<Barcode> barcode;

	private long amount;

	private Long recievedAmount;

	private Date createdInfo;

	private String settelmentInfo;

	private List<TaggedItems> barcodes;

}
