package com.otsi.retail.promotions.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
public class Condition {

	@Id
	@GeneratedValue
	private Long id;

	private ColumnName columnName;

	private Operator operatorSymbol;

	@ElementCollection
	@Column(name = "givenValues")
	private List<String> givenValues;

	private LocalDate createdAt;

	private LocalDate updatedAt;

	@ManyToOne
	@JoinColumn(name = "poolRuleId")
	private Pool_Rule poolRule;

}
