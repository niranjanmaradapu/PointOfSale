package com.otsi.retail.promotions.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.Applicability;
import com.otsi.retail.promotions.common.PromoApplyType;

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

	private LocalDate promotionStartDate;

	private LocalDate promotionEndDate;

	private LocalDate createdDate;

	private LocalDate lastModified;

	private int priority;

	private Boolean isForEdit;

	private List<PromotionPoolVo> poolVo;

	/** These below fields are store related fields **/

	private StoreVo storeVo;
	
	private List<BenefitVo> benfitVo;
	
	private List<PromotionSlabsVo> promotionSlabVo;
	
	private List<PromotionToStoreVo> promoStores;

}
