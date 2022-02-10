package com.otsi.retail.promotions.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Entity
@Table(name = "promotion_store_mappings")
public class PromotionToStoreEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Long promoId;

	private Long storeId;

	private int priority;
	
	private LocalDate promotionStartDate;
	  
	private LocalDate promotionEndDate;
	
	private Boolean promotionStatus;
	 	 	
	private Long createdBy;

	@CreationTimestamp
	private LocalDate createdat;

	@UpdateTimestamp
	private LocalDate updatedat;

}
