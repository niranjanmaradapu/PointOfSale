package com.otsi.retail.promotions.vo;

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
	
	private Long promoToStoreId;
	
	private Long storeId;
	
	private String promotionName;
	
	private String storeName;
	
	private String description;
	
	private int priority;
	
	private Long promotionsCount;
	
	private LocalDate promotionStartDate;
	
	private LocalDate promotionEndDate;
	
	private Boolean isActive;
	
}
