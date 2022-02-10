package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.common.ColumnName;
import com.otsi.retail.promotions.common.Operator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pool_RuleVo {
	
	private Long id;

	private String columnName;

	private Operator operatorSymbol;

	private Long givenValue;
	
	private String ruleType;
	
	private Long ruleNumber;

	private Long createdBy;

	private Long modifiedBy;
	
	private Boolean isForEdit;
	
	private LocalDate lastModified;

}
