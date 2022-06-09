package com.otsi.retail.promotions.vo;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PromotionToStoreVo {
	
	private Long id;
    
	private List<PromotionsVo> promotions;
	
	private List<StoreVo> stores;
	
	private Long clientId;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private Boolean promotionStatus;
	
	private int priority;
	
	private Long createdBy;

}
