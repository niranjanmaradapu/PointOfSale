package com.otsi.retail.promotions.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "promo_store")
public class PromotionToStoreEntity {
	
	@Id
	private Long promoId;
	
	private Long storeId;
}
