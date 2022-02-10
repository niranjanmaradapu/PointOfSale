package com.otsi.retail.promotions.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "promotion_slabs")
public class PromotionSlabsEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
    private float toSlab;
	
	private float fromSlab;
	
	private Long promoId;
	
	@CreationTimestamp
	private LocalDate createdat;
	
	@UpdateTimestamp
	private LocalDate updatedat;
	
	
}
