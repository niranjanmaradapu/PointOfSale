/**
 * 
 */
package com.otsi.retail.connectionpool.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.otsi.retail.connectionpool.common.BenfitType;
import com.otsi.retail.connectionpool.common.DiscountType;
import com.otsi.retail.connectionpool.common.FixedAmountOn;
import com.otsi.retail.connectionpool.common.PercentageDiscountOn;
import com.otsi.retail.connectionpool.common.RupeesDiscountOn;

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

	// one benefit have one promotion only
	@OneToOne(mappedBy = "benfitEntity")
	private PromotionsEntity promotionEntity;

}
