package com.otsi.kalamandhir.vo;

import java.time.LocalDate;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.kalamandhir.model.Barcode;
import com.otsi.kalamandhir.model.TaggedItems;

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
