package com.otsi.retail.promotions.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "pos_promotion_store_mappings")
public class PromotionToStoreEntity extends BaseEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Long promoId;
	
	private String promotionName;
	
	private Long storeId;
	
	private Long clientId;
	
	private String storeName;

	private int priority;
	
	private LocalDate startDate;
	  	
	private LocalDate endDate;
	
	private Boolean promotionStatus;
	 	 
	
//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinTable(name = "promo_store", joinColumns = @JoinColumn(name = "sId"), inverseJoinColumns = @JoinColumn(name = "promoId"))
	
//	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = PromotionsEntity.class)
//	@JoinColumn(name = "promoId", referencedColumnName = "promoId")
//	private List<PromotionsEntity> promotions;

}
