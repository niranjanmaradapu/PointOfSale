/**
 * 
 */
package com.otsi.retail.connectionpool.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.otsi.retail.connectionpool.common.BenfitType;
import com.otsi.retail.connectionpool.common.DiscountType;
import com.otsi.retail.connectionpool.common.ItemValue;
import com.otsi.retail.connectionpool.common.PercentageDiscountOn;

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

	// one benefit have one promotion only
    // @OneToOne(mappedBy = "benfitEntity")
    // private PromotionsEntity promotionEntity;
	
	@ManyToOne
	@JoinColumn(name = "promoId")
	private PromotionsEntity promotionEntity;

}
