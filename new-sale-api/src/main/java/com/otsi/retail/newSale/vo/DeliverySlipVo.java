package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.common.DSStatus;

import lombok.Data;

@Component
@Data
public class DeliverySlipVo {

	private List<BarcodeVo> barcode;
	
	private Long dsId;

	private int qty;
	
	private String dsNumber;

	private String type;

	private Long mrp;

	private Long promoDisc;

	private Long netAmount;

	private DSStatus status;

	private Long salesMan;

	private LocalDate createdDate;

	private LocalDate lastModified;
	
	private List<LineItemVo> lineItems;

	// private Long salesMan;

	// private String barcodes;

}
