/*
 * vo for Slab
*/
package com.otsi.retail.customerManagement.vo;

/**
 * @author vasavi
 *
 */
public class SlabVo {

	private long id;
	private double priceFrom;
	private double priceTo;
	private TaxVo taxVo;

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
	 * @return the priceFrom
	 */
	public double getPriceFrom() {
		return priceFrom;
	}

	/**
	 * @param priceFrom the priceFrom to set
	 */
	public void setPriceFrom(double priceFrom) {
		this.priceFrom = priceFrom;
	}

	/**
	 * @return the priceTo
	 */
	public double getPriceTo() {
		return priceTo;
	}

	/**
	 * @param priceTo the priceTo to set
	 */
	public void setPriceTo(double priceTo) {
		this.priceTo = priceTo;
	}

	/**
	 * @return the taxVo
	 */
	public TaxVo getTaxVo() {
		return taxVo;
	}

	/**
	 * @param taxVo the taxVo to set
	 */
	public void setTaxVo(TaxVo taxVo) {
		this.taxVo = taxVo;
	}

}

