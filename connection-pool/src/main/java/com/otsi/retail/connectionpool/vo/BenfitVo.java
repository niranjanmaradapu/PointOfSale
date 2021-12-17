/**
 * 
 */
package com.otsi.retail.connectionpool.vo;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenfitVo {

	private Long benfitId;

	private BenfitType benfitType;

	private DiscountType discountType;

	private String discount;

	private PercentageDiscountOn percentageDiscountOn;

}
