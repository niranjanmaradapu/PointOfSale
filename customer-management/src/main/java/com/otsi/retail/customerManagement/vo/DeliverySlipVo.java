package com.otsi.retail.customerManagement.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.otsi.retail.customerManagement.utils.DSStatus;

import lombok.Data;

@Data
public class DeliverySlipVo {

	private List<BarcodeVo> barcode;

	private int qty;
	
	private String dsNumber;

	private String type;

	private Long mrp;

	private Long promoDisc;

	private Long netAmount;

	private DSStatus status;

	private Long salesMan;

	private LocalDate createdDate;

	private LocalDateTime lastModified;

}
