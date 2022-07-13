package com.otsi.retail.promotions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.entity.ColumnNameAndOperators;
import com.otsi.retail.promotions.vo.ColumnNameAndOperatorsVo;

@Repository
public interface ColumnNameAndOperatorRepository extends JpaRepository<ColumnNameAndOperators, Long> {

	ColumnNameAndOperators save(ColumnNameAndOperatorsVo columnNameAndOperatorsVo);

	//List<ColumnNameAndOperators>  findByColumnNameOrOperator(String columnName,String operator);

	List<ColumnNameAndOperators> findAllByColumnNameOrOperator(String columnName, String operator);

	List<ColumnNameAndOperators> findAllByColumnNameAndOperator(String columnName, String operator);

	
}