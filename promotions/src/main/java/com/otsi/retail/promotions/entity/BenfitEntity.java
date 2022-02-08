/**
 * 
 */
package com.otsi.retail.promotions.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;
import com.otsi.retail.promotions.common.PercentageDiscountOn;

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
@Table(name = "benfit_entity")
public class BenfitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long benfitId;

	private BenfitType benfitType;

	private DiscountType discountType;

	private String discount;

	private PercentageDiscountOn percentageDiscountOn;
	
    private Long numOfItemsFromGetPool;
	
	private Long numOfItemsFromBuyPool;
	
	private ItemValue itemValue;
	
	private Long poolId;
	
	private String poolName;
	
	private float toSlab;
	
	private float fromSlab;
	
	@ManyToOne
	@JoinColumn(name = "promoId")
	private PromotionsEntity promotionEntity;

}
