package com.otsi.retail.promotions.vo;

import java.util.List;

import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.ItemValue;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.common.DiscountSubTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitVo {

	private Long benfitId;
	private BenfitType benfitType;
	private DiscountType discountType;
	private String discount;
	private ItemValue itemValue;
	private Long numOfItemsFromBuyPool;
	private Long numOfItemsFromGetPool;
	private DiscountSubTypes discountSubType;
	private List<PoolEntity> poolEntities;

}
