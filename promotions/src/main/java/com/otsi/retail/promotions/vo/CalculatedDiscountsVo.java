package com.otsi.retail.promotions.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalculatedDiscountsVo {

	private String calculatedDiscount;
	private String calculatedDiscountDetails;
	private boolean isDiscountAvailable;
	

}
