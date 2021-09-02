package com.otsi.retail.promoexchange.vo;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarcodeVo {

	@NotBlank
	private String barcode;

	private String itemDesc;

	private int qty;

	private Long mrp;

	private Long promoDisc;

	private Long netAmount;

	private Long salesMan;

	private LocalDateTime createdDate;

	private LocalDateTime lastModified;

}
