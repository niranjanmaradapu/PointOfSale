package com.otsi.retail.customerManagement.vo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.customerManagement.model.Barcode;
import com.otsi.retail.customerManagement.model.TaggedItems;

import lombok.Data;

/**
 * @author lakshmi
 *
 */
@Component
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

	private LocalDate createdInfo;

	private String settelmentInfo;

	private List<TaggedItems> barcodes;

}
