package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.common.Applicability;
import com.otsi.retail.connectionpool.common.PromoApplyType;
import com.otsi.retail.connectionpool.common.PromotionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This VO class contains all the inputs of Promotions
 * 
 * @author Manikanta Guptha
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PromotionsVo {

	private Long promoId;

	private String promoName;

	private String description;

	private String printNameOnBill;

	private Applicability applicability;

	private PromoApplyType promoApplyType;

	private Boolean isTaxExtra;

	private int buyItemsFromPool;

	private Boolean isActive;

	private PromotionType promoType;

	private LocalDate startDate;

	private LocalDate endDate;

	private LocalDate createdDate;

	private LocalDate lastModified;

	private Boolean isForEdit;

	private List<ConnectionPoolVo> poolVo;
	
	private List<StoreVo> storeVo;

}
