package com.otsi.retail.promotions.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.otsi.retail.promotions.common.ColumnName;
import com.otsi.retail.promotions.common.Operator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_pool_rule_conditions")
public class Condition extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
    
	@Enumerated(EnumType.STRING)
	private ColumnName columnName;
    
	@Enumerated(EnumType.STRING)
	private Operator operatorSymbol;

	@ElementCollection
	@Column(name = "givenValues")
	private List<String> givenValues;

	@ManyToOne
	@JoinColumn(name = "poolRuleId")
	private Pool_Rule poolRule;

}