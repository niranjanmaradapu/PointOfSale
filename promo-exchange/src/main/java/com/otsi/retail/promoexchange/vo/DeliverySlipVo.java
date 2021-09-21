package com.otsi.retail.promoexchange.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.otsi.retail.promoexchange.common.DSStatus;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
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

	private Date createdDate;

	private Date lastModified;

	// private Long salesMan;

	// private String barcodes;

}
