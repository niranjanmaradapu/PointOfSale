package com.otsi.retail.promotions.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.otsi.retail.promotions.common.Applicability;
import com.otsi.retail.promotions.common.PromoApplyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_promotion_master")
public class PromotionsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long promoId;

	private Long domainId;

	private String promotionName;

	private String description;

	private String printNameOnBill;

	private Long createdBy;
    
	@Enumerated(EnumType.STRING)
	private Applicability applicability;
    
	@Enumerated(EnumType.STRING)
	private PromoApplyType promoApplyType;

	private int buyItemsFromPool;

	private Boolean isTaxExtra;

	private Boolean isActive;

	private LocalDate promotionStartDate;

	private LocalDate promotionEndDate;
    
	@CreationTimestamp
	private LocalDate createdDate;
    
	@UpdateTimestamp
	private LocalDate lastModified;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "pool_promo", joinColumns = @JoinColumn(name = "promoId"), inverseJoinColumns = @JoinColumn(name = "poolId"))
	private List<PoolEntity> poolEntity;
	
	@OneToMany(targetEntity = BenfitEntity.class, mappedBy = "promotionEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<BenfitEntity> benfitEntity;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "promoId")
	private List<PromotionSlabsEntity> promotionSlabEntity;
	
//	@ManyToMany(mappedBy = "promotions",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private List<PromotionToStoreEntity> promoStores;
	
//	@ManyToOne
//	private PromotionToStoreEntity promoStores;
	
}
