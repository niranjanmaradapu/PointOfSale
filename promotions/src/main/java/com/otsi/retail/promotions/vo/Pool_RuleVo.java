package com.otsi.retail.promotions.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pool_RuleVo {
	
	private Long id;
	
	private String ruleType;
	
	private Long ruleNumber;

	private Long createdBy;

	private Long modifiedBy;
	
	private Boolean isForEdit;
	
	private LocalDate lastModified;
	
	private List<ConditionVo> conditionVos;

}
