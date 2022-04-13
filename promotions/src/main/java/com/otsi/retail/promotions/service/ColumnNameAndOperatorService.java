package com.otsi.retail.promotions.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.ColumnNameAndOperators;
import com.otsi.retail.promotions.vo.ColumnNameAndOperatorsVo;

@Component
public interface ColumnNameAndOperatorService {
	
	ColumnNameAndOperatorsVo save(ColumnNameAndOperatorsVo columnNameAndOperatorsVo);
	
	List<ColumnNameAndOperators> getListofColumnNames();
	
	ColumnNameAndOperators getColumnNameById(Long Id);
	
	String deleteColumnNameId(Long Id);
 
	List<ColumnNameAndOperators> getAnyMatchingData(String columnName, String operator);
	

}
