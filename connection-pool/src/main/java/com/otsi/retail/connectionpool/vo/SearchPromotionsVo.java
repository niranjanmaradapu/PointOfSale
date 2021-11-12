package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;

import javax.persistence.Id;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPromotionsVo {
	
	private Long promoId;
	
	private String promotionName;
	
	private String description;
	
	private int priority;
	
	private Long promotionsCount;
	
	private String storeName;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private boolean promotionStatus;
	
	

	
}
