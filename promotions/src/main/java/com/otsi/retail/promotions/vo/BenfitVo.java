/**
 * 
 */
package com.otsi.retail.promotions.vo;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenfitVo {

	private Long benfitId;

	private BenfitType benfitType;

	private DiscountType discountType;

	private String discount;

	private PercentageDiscountOn percentageDiscountOn;
	
    private Long numOfItemsFromBuyPool;
	
	private ItemValue itemValue;
	
	private Long numOfItemsFromGetPool;
	
    private Long poolId;
	
	private String poolName;
	
    private float toSlab;
	
	private float fromSlab;

}
