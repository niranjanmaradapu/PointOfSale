package com.otsi.retail.connectionpool.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long promoId;

	private Long domainId;

	private String promotionName;

	private String description;

	private String printNameOnBill;

	private String createdBy;

	private Applicability applicability;

	private PromoApplyType promoApplyType;

	private int buyItemsFromPool;

	private Boolean isTaxExtra;

	private Boolean isActive;

	private PromotionType promoType;

	private LocalDate startDate;

	private LocalDate endDate;

	private String storeName;

	@GeneratedValue
	private int priority;
    
	@CreationTimestamp
	private LocalDate createdDate;
    
	@UpdateTimestamp
	private LocalDate lastModified;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "pool_promo", joinColumns = @JoinColumn(name = "promoId"), inverseJoinColumns = @JoinColumn(name = "poolId"))
	private List<PoolEntity> poolEntity;
	
	@OneToMany(targetEntity = BenfitEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "promo_benfit", joinColumns = @JoinColumn(name = "promoId"), inverseJoinColumns = @JoinColumn(name = "benfitId"))
	private List<BenfitEntity> benfitEntity;
    
	//one promotion have one benefit only
    // @OneToOne(cascade =CascadeType.MERGE)
    // @JoinColumn(name = "benfit_id")
//	private BenfitEntity benfitEntity;

}
