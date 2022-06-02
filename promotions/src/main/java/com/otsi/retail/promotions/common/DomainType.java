package com.otsi.retail.promotions.common;

public enum DomainType {

	None(0L, "none"), Textile(1L, "Textile"), Retail(2L, "Retail");

	private Long id;
	private String name;

	/**
	 * @param id
	 * @param name
	 */
	private DomainType(Long id, String name) {
		this.id = id;
		this.name = name;
	}

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
