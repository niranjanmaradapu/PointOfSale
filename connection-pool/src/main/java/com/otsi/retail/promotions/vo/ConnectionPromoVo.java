package com.otsi.retail.promotions.vo;

import java.util.List;

import lombok.Data;

@Data
public class ConnectionPromoVo {
	
	private Long domainId;
	
	private List<PromotionsVo> promovo;
	

}
