package com.otsi.retail.customerManagement.vo;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class BarcodeVo {

	@NotBlank
	private String barcode;

	private String itemDesc;
	
	private String section;


	private int qty;

	private Long mrp;

	private Long promoDisc;

	private Long netAmount;

	private Long salesMan;

	private Date createdDate;

	private LocalDateTime lastModified;

}
