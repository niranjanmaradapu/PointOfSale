package com.otsi.retail.newSale.common;

public enum DomainData {

	RE(1, "Retail"), TE(2, "Textile"), DS(3, "Departmental Store");

	private int id;
	private String eName;

	private DomainData(int id, String eName) {

		this.id = id;
		this.eName = eName;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

}