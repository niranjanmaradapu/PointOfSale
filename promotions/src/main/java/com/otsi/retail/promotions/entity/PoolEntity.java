package com.otsi.retail.promotions.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.otsi.retail.promotions.common.PoolType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_pool_master")
public class PoolEntity {

	@Id
	@GeneratedValue
	private Long poolId;
	
	private Long domainId;

	private String poolName;

	private PoolType poolType;
	
	private Long createdBy;
	
	private Long modifiedBy;

	private LocalDate createdDate;

	private LocalDate lastModified;

	private Boolean isActive;
	
	@OneToMany(targetEntity = Pool_Rule.class, mappedBy = "poolEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Pool_Rule> pool_Rule;
	
	@ManyToMany(mappedBy = "poolEntity",cascade ={CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	private List<PromotionsEntity> promoEntity;

}
