package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.common.DSStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DeliverySlipVo {

	private List<BarcodeVo> barcode;
	
	private Long dsId;
	
	private Long storeId;

	private int qty;
	
	private String dsNumber;

	private String type;

	private Long mrp;
	
	private Long grossAmount;

	private Long promoDisc;

	private Long netAmount;

	private DSStatus status;

	private Long salesMan;

	private LocalDateTime createdDate;

	private LocalDateTime lastModifiedDate;
	
	private List<LineItemVo> lineItems;

	// private Long salesMan;

	// private String barcodes;

}
