/**
 * vo for PaymentAmountType
 */
package com.otsi.retail.newSale.vo;

import com.otsi.retail.newSale.common.PaymentType;

/**
 * @author vasavi
 *
 */
public class PaymentAmountTypeVo {

	private Long id;

	private PaymentType paymentType;

	private Long paymentAmount;

	private NewSaleVo newsale;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the paymentAmount
	 */
	public Long getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * @return the newsale
	 */
	public NewSaleVo getNewsale() {
		return newsale;
	}

	/**
	 * @param newsale the newsale to set
	 */
	public void setNewsale(NewSaleVo newsale) {
		this.newsale = newsale;
	}

}
