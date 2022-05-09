/**
 * 
 */
package com.otsi.retail.promotions.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountSubTypes;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sudheer.Swamy
 *
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_promotion_benfits")
public class BenfitEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long benfitId;

	@Enumerated(EnumType.STRING)
	private BenfitType benfitType;

	@Enumerated(EnumType.STRING)
	private DiscountType discountType;

	private String discount;

	@Enumerated(EnumType.STRING)
	private DiscountSubTypes discountSubTypes;

	private Long numOfItemsFromGetPool;

	private Long numOfItemsFromBuyPool;

	@Enumerated(EnumType.STRING)
	private ItemValue itemValue;
    
	private Long poolId;
	
	private String poolName;

	@ManyToOne
	@JoinColumn(name = "promoId")
	private PromotionsEntity promotionEntity;

	@OneToOne(mappedBy = "benfitEntity")
	private PromotionSlabsEntity promotionSlabsEntity;

	// newly added
//	@ManyToOne
//	@JoinColumn(name = "poolId")
//	private PoolEntity poolEntityy;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinTable(name = "pool_benefit", joinColumns = @JoinColumn(name = "benefitId"), inverseJoinColumns = @JoinColumn(name = "poolId"))
	private List<PoolEntity> poolEntities;

}
