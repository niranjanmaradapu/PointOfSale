package com.otsi.retail.newSale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ToString
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

	private Float cgst;

	private Float sgst;
	
	private Float igst;
	
	private Float cess;

	private Long manualDiscount;
	
	private Long promoDiscount;
	
	private Long salesManId;

	private Long netValue;

	private LocalDateTime createdDate;

	private LocalDateTime lastModifiedDate;

	private HsnDetailsVo HsnDetailsVo;

}
