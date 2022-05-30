package com.otsi.retail.promotions.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "column_names_operators")
public class ColumnNameAndOperators {
	
	@Id
	@GeneratedValue
	Long Id;
	Long domainId;
	String columnName;
	String operator;

}
