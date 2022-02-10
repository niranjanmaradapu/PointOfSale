package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author lakshmi
 *
 */
@Component
@Data
public class ListOfReturnSlipsVo {
	
	private int rsId;

	private LocalDate dateFrom;

	private LocalDate dateTo;

	private Long domainId;
	
	private Long storeId;

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

	private Long totalAmount;

	private List<TaggedItems> barcodes;

}
