package com.otsi.retail.promotions.entity;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_promotion_slabs")
public class PromotionSlabsEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
    private float toSlab;
	
	private float fromSlab;
	
	@CreationTimestamp
	private LocalDate createdat;
	
	@UpdateTimestamp
	private LocalDate updatedat;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "benfitId", referencedColumnName = "benfitId")
	private BenfitEntity benfitEntity;
	
}
