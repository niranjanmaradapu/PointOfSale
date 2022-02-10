package com.otsi.retail.promotions.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.otsi.retail.promotions.common.Operator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pool_rule")
public class Pool_Rule {

	@Id
	@GeneratedValue
	private Long id;

	private String columnName;

	private Operator operatorSymbol;

	private Long givenValue;
	
	private String ruleType;
	
	private Long ruleNumber;
	
	private LocalDate createdat;

	private LocalDate updatedat;
	
	@ManyToOne
	@JoinColumn(name = "pool_Id")
	private PoolEntity poolEntity;

}
