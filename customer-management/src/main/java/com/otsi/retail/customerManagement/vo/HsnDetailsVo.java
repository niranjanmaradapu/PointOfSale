/**
 * vo for hsnDetails
 */
package com.otsi.retail.customerManagement.vo;

import java.util.List;


/**
 * @author vasavi
 *
 */
public class HsnDetailsVo {
	
	private long id;
	
	private String hsnCode;

	private String description;

	private String taxAppliesOn;

	private boolean isSlabBased;

	private TaxVo taxVo;

	private List<SlabVo> slabVos;

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
	 * @return the hsnCode
	 */
	public String getHsnCode() {
		return hsnCode;
	}

	/**
	 * @param hsnCode the hsnCode to set
	 */
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the taxAppliesOn
	 */
	public String getTaxAppliesOn() {
		return taxAppliesOn;
	}

	/**
	 * @param taxAppliesOn the taxAppliesOn to set
	 */
	public void setTaxAppliesOn(String taxAppliesOn) {
		this.taxAppliesOn = taxAppliesOn;
	}

	/**
	 * @return the isSlabBased
	 */
	public boolean isSlabBased() {
		return isSlabBased;
	}

	/**
	 * @param isSlabBased the isSlabBased to set
	 */
	public void setSlabBased(boolean isSlabBased) {
		this.isSlabBased = isSlabBased;
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

	/**
	 * @return the slabVos
	 */
	public List<SlabVo> getSlabVos() {
		return slabVos;
	}

	/**
	 * @param slabVos the slabVos to set
	 */
	public void setSlabVos(List<SlabVo> slabVos) {
		this.slabVos = slabVos;
	}

	@Override
	public String toString() {
		return "HsnDetailsVo [id=" + id + ", hsnCode=" + hsnCode + ", description=" + description + ", taxAppliesOn="
				+ taxAppliesOn + ", isSlabBased=" + isSlabBased + ", taxVo=" + taxVo + ", slabVos=" + slabVos + "]";
	}

	

}
