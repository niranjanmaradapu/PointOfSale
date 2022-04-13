package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LineItemVo {

	private Long lineItemId;

	private Long domainId;

	private Long storeId;

	private String barCode;

	private Long section;
	
	private Long subSection;

	private Long division;

	private Long userId;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private String hsnCode;

	private Long actualValue;

	private Long taxValue;

	private Long cgst;

	private Long sgst;

	private Long discount;
	
	private String description;

	private Long netValue;

	private LocalDate creationDate;

	private LocalDate lastModified;

}
