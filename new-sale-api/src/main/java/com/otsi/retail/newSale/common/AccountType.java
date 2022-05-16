package com.otsi.retail.newSale.common;

/**
 * @author vasavi
 *
 */
public enum AccountType {

	None(0L, "none"), CREDIT(1L, "credit"), DEBIT(2L, "debit");

	private Long id;
	private String name;

	/**
	 * @param id
	 * @param name
	 */
	private AccountType(Long id, String name) {
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
