/**
 * vo for Barcode
 */
package com.otsi.kalamandhir.vo;

/**
 * @author vasavi
 *
 */
public class BarcodeVo {

	private long id;

	private long barcodeId;

	private long invoice;

	private long amount;

	private long quantity;

	private long rtnQuantity;

	private String reason;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the barcodeId
	 */
	public long getBarcodeId() {
		return barcodeId;
	}

	/**
	 * @param barcodeId the barcodeId to set
	 */
	public void setBarcodeId(long barcodeId) {
		this.barcodeId = barcodeId;
	}

	/**
	 * @return the invoice
	 */
	public long getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(long invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * @return the quantity
	 */
	public long getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the rtnQuantity
	 */
	public long getRtnQuantity() {
		return rtnQuantity;
	}

	/**
	 * @param rtnQuantity the rtnQuantity to set
	 */
	public void setRtnQuantity(long rtnQuantity) {
		this.rtnQuantity = rtnQuantity;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

}
