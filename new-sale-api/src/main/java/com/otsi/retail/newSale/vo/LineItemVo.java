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

	private String barCode;
	
	private String section;
	
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

	private Long netValue;

	private LocalDate creationDate;

	private LocalDate lastModified;
	
	private HsnDetailsVo HsnDetailsVo;


}
