package com.otsi.retail.promotions.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_promotion_slabs")
public class PromotionSlabsEntity extends BaseEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
    private float toSlab;
	
	private float fromSlab;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "benfitId", referencedColumnName = "benfitId")
	private BenfitEntity benfitEntity;
	
}
