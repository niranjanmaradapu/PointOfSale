package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.common.PoolType;
import com.otsi.retail.connectionpool.common.PromotionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class searchPromotionsVo {
	private Long promoId;
	
	private String promotionName;
	
	private String storeName;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private boolean promotionStatus;
	
	

	
}
