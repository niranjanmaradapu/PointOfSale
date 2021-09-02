package com.otsi.retail.promoexchange.vo;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ListOfReturnSlipsVo {

	private LocalDate dateFrom;

	private LocalDate dateTo;

	private String barcode;

	private String rtStatus;

	private String rtNumber;

	private String creditNote;
	private String createdBy;
	private Boolean rtReviewStatus;

	// private List<Barcode> barcode;

	private long amount;
	
	private Long recievedAmount;

	private LocalDate createdInfo;

	private String settelmentInfo;

	private List<TaggedItems> barcodes;
		
}
