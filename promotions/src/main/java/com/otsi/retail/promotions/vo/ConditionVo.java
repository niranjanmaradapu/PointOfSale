package com.otsi.retail.promotions.vo;

import java.util.List;

import com.google.inject.spi.PrivateElements;
import com.otsi.retail.promotions.common.ColumnName;
import com.otsi.retail.promotions.common.Operator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionVo {
	
	private Long id;

	private ColumnName columnName;

	private Operator operatorSymbol;

	private List<String> givenValues;
	
	private Pool_RuleVo poolRuleVo;

}
