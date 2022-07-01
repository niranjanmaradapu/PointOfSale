package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.TaggedItems;
import com.otsi.retail.newSale.common.ReturnSlipStatus;

import lombok.Data;

/**
 * @author lakshmi
 *
 */
@Component
@Data
public class ListOfReturnSlipsVo {
	
	private Long rsId;

	private LocalDate dateFrom;

	private LocalDate dateTo;

	private Long domainId;
	
	private Long storeId;

	private String barcode;

	private ReturnSlipStatus rtStatus;

	private String rtNumber;

	private String creditNote;
	private Long createdBy;
	private Boolean rtReviewStatus;

	// private List<Barcode> barcode;

	private long amount;

	private Long recievedAmount;

	private LocalDateTime createdInfo;

	private String settelmentInfo;

	private Long totalAmount;
	private List<TaggedItems> barcodes;



}
