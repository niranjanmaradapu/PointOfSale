package com.otsi.retail.connectionpool.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "stores")
public class StoresEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long storeId;
	
	private String storeName;
	
	private String storeDescription;
	
	private LocalDate createdDate;
	
	private LocalDate lastModifiedDate;
	
	
	/**
	 * @Many_To_Many mapping
	 */

	@ManyToMany(mappedBy = "storeEntity",cascade = CascadeType.ALL)
	private List<PromotionsEntity> promoEntity;
	
	
//	@ManyToMany(targetEntity = PromotionsEntity.class, cascade = {CascadeType.ALL},mappedBy = "storeEntity")
//	List<PromotionsEntity> promoEntity;
	
//	@OneToMany(mappedBy = "storeEntity",cascade = CascadeType.ALL)
//	@JsonIgnore
//	private List<PromoStoreEntity> promoStore = new ArrayList<>();
	
}
