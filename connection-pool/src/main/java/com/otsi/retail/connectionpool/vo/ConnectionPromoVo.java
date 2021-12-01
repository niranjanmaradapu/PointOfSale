package com.otsi.retail.connectionpool.vo;

import java.util.List;

import lombok.Data;

@Data
public class ConnectionPromoVo {
	
	private Long domainId;
	
	private List<PromotionsVo> promovo;
	

}
