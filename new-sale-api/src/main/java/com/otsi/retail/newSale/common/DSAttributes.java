/**
 * 
 */
package com.otsi.retail.newSale.common;

/**
 * @author vasavi
 *
 */
public enum DSAttributes {
	
	PIECES(1, "pieces"), METERS(2, "meters");

	private int id;
	private String name;

	/**
	 * @param id
	 * @param name
	 */
	private DSAttributes(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
