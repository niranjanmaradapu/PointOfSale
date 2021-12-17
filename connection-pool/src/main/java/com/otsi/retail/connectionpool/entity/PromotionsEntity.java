package com.otsi.retail.connectionpool.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.otsi.retail.connectionpool.common.Applicability;
import com.otsi.retail.connectionpool.common.BenfitType;
import com.otsi.retail.connectionpool.common.DiscountType;
import com.otsi.retail.connectionpool.common.FixedAmountOn;
import com.otsi.retail.connectionpool.common.PercentageDiscountOn;
import com.otsi.retail.connectionpool.common.PromoApplyType;
import com.otsi.retail.connectionpool.common.PromotionType;
import com.otsi.retail.connectionpool.common.RupeesDiscountOn;

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
    
	//one promotion have one benefit only
	@OneToOne(cascade =CascadeType.MERGE)
	@JoinColumn(name = "benfit_id")
	private BenfitEntity benfitEntity;

}
