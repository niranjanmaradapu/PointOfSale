package com.otsi.retail.connectionpool.vo;

import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.common.ColumnName;
import com.otsi.retail.connectionpool.common.Operator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleVo {

	private String columnName;

	private Operator operatorSymbol;

	private Long givenValue;

	private Long ruleId;

}
