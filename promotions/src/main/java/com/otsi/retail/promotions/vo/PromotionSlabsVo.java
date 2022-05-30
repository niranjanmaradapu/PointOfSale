package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PromotionSlabsVo {

	private Long id;

	private float toSlab;

	private float fromSlab;

	private LocalDate createdat;

	private LocalDate updatedat;
	
	private BenefitVo benfitVo;

}
