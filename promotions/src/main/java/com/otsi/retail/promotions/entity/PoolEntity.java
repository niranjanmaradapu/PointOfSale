package com.otsi.retail.promotions.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class PoolEntity extends BaseEntity {

	@Id
	@GeneratedValue
	private Long poolId;
	
	private Long domainId;

	private String poolName;
    
	@Enumerated(EnumType.STRING)
	private PoolType poolType;

	private Boolean isActive;
	
	@OneToMany(targetEntity = Pool_Rule.class, mappedBy = "poolEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Pool_Rule> pool_Rule;
	
	@ManyToMany(mappedBy = "poolEntity",cascade ={CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	private List<PromotionsEntity> promoEntity;
	
    //newly added
//	@OneToMany(targetEntity = BenfitEntity.class, mappedBy = "poolEntityy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	private List<BenfitEntity> benefitEntities;
	
	@ManyToMany(mappedBy = "poolEntities",cascade ={CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	private List<BenfitEntity> benefits;

}
