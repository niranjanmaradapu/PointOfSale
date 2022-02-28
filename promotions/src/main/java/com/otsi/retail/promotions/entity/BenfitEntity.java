/**
 * 
 */
package com.otsi.retail.promotions.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;
import com.otsi.retail.promotions.common.DiscountSubTypes;

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
public class BenfitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long benfitId;

	private BenfitType benfitType;

	private DiscountType discountType;

	private String discount;

	private DiscountSubTypes discountSubTypes;
	
    private Long numOfItemsFromGetPool;
	
	private Long numOfItemsFromBuyPool;
	
	private ItemValue itemValue;
	
	@CreationTimestamp
	private LocalDate createdAt;
	
	@UpdateTimestamp
	private LocalDate updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "promoId")
	private PromotionsEntity promotionEntity;
	
	@OneToOne(mappedBy = "benfitEntity")
	private PromotionSlabsEntity promotionSlabsEntity;
	

	
}
