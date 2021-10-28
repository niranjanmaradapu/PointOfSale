package com.otsi.retail.connectionpool.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.otsi.retail.connectionpool.common.Applicability;
import com.otsi.retail.connectionpool.common.PromoApplyType;
import com.otsi.retail.connectionpool.common.PromotionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "promotion")
public class PromotionsEntity {

	@Id
	@GeneratedValue
	private Long promoId;

	private String promoName;

	private String description;

	private String printNameOnBill;

	private Applicability applicability;

	private PromoApplyType promoApplyType;

	private Boolean isTaxExtra;

	private int buyItemsFromPool;

	private Boolean isActive;
	
	/** These below 3 fields are store related fields **/
	
     private PromotionType promoType;
	
	private LocalDate startDate;
	
	private LocalDate endDate;
	
	private String storeName;

	private LocalDate createdDate;

	private LocalDate lastModified;
	
	

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "pool_promo", joinColumns = @JoinColumn(name = "promoId"), inverseJoinColumns = @JoinColumn(name = "poolId"))
	private List<PoolEntity> poolEntity;
	
	
	/**
	 * @Many_To_Many mapping For Stores
	 */
	
	/*
	 * @ManyToMany(cascade = { CascadeType.ALL})
	 * 
	 * @JoinTable(name = "promo_store", joinColumns = @JoinColumn(name = "promoId"),
	 * inverseJoinColumns = @JoinColumn(name = "storeId")) private
	 * List<StoresEntity> storeEntity;
	 */

}
