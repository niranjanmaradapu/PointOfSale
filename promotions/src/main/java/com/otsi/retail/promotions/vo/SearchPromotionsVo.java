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
	
	//primary key of promoStoreMapping table
	private Long id;
	
	private Long storeId;
	
	private String promotionName;
	
	private String storeName;
	
	private String description;
	
	private int priority;
	
	private Long promotionsCount;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private Boolean promotionStatus;
	
}
