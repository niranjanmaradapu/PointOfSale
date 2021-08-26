/**
 * 
 */
package com.otsi.retail.newSale.Entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.otsi.retail.newSale.common.PaymentType;

import lombok.Data;

/**
 * @author vasavi
 *
 */
@Data
@Entity
@Table(name = "payment_amount_type")
public class PaymentAmountType {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @Enumerated(EnumType.ORDINAL)
	private PaymentType paymentType;

	private Long paymentAmount;

	@ManyToOne
	@JoinColumn(name = "newsale_id")
	private NewSaleEntity newsaleId;
	

}

