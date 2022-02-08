package com.otsi.retail.promotions.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.Applicability;
import com.otsi.retail.promotions.common.PromoApplyType;
import com.otsi.retail.promotions.common.PromotionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@ToString
public class PromotionsVo {

	private Long promoId;

	private Long storeId;

	private Long domainId;

	private String promotionName;

	private String description;

	private String printNameOnBill;

	private Long createdBy;

	private Applicability applicability;

	private PromoApplyType promoApplyType;
	
	private int buyItemsFromPool;

	private Boolean isTaxExtra;

	private Boolean isActive;

	private PromotionType promoType;

	private LocalDate startDate;

	private LocalDate endDate;

	private String storeName;

	private LocalDate createdDate;

	private LocalDate lastModified;

	private int priority;

	private Boolean isForEdit;

	private List<PromotionPoolVo> poolVo;

	/** These below fields are store related fields **/

	private StoreVo storeVo;
	
	private List<BenfitVo> benfitVo;

}
