/**
 * vo for PaymentAmountType
 */
package com.otsi.retail.newSale.vo;

import com.otsi.retail.newSale.common.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author vasavi
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentAmountTypeVo {

	private Long id;

	private PaymentType paymentType;

	private Long paymentAmount;
	
	private String payId;

	private NewSaleVo newsale;

}
