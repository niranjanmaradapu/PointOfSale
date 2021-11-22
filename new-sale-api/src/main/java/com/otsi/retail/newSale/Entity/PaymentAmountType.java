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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vasavi
 *
 */
@Data
@Entity
@Table(name = "order_transaction")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAmountType {

	@Id
	@GeneratedValue // (strategy = GenerationType.AUTO)
	private Long id;

	// @Enumerated(EnumType.ORDINAL)
	private String paymentType;

	private Long paymentAmount;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private NewSaleEntity orderId;

	// private Long orderId;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Long newSale;
	private String razorPayId;

	private Boolean razorPayStatus;
}
