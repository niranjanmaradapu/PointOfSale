package com.otsi.retail.promotions.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.PoolType;
import com.otsi.retail.promotions.entity.BenfitEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionPoolVo {

	private String poolName;

	private Long poolId;
	
	private Long domainId;

	private PoolType poolType;

	private List<Pool_RuleVo> pool_RuleVo;
	
	private Long createdBy;
	
	private Long modifiedBy;

	private LocalDate createdDate;

	private LocalDate lastModified;

	private Boolean isActive;

	private Boolean isForEdit;
	
	private List<BenfitEntity> benefits;

}
