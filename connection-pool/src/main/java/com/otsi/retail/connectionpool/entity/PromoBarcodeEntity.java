package com.otsi.retail.connectionpool.entity;

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
@Table(name = "promo_barcode")
public class PromoBarcodeEntity {

	@Id
	@GeneratedValue
	private Long promoBarcodeId;

	private Long promoId;

	private String barCode;

}
