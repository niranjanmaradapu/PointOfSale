package com.otsi.retail.promotions.vo;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ColumnNameAndOperatorsVo {
	
	Long Id;
	Long domainId;
	String columnName;
	String operator;

}
