package com.otsi.retail.promotions.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_pool_rule")
public class Pool_Rule {

	@Id
	@GeneratedValue
	private Long id;
	
	private String ruleType;
	
	private Long ruleNumber;
	
	private LocalDate createdAt;

	private LocalDate updatedAt;
	
	@ManyToOne
	@JoinColumn(name = "pool_Id")
	private PoolEntity poolEntity;
	
	@OneToMany(targetEntity =Condition.class,mappedBy = "poolRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Condition> conditions;
	
}
