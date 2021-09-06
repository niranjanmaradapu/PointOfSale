package com.otsi.retail.newSale.common;

public enum UserDataAVEnum {

	GSTNUMBER(1, "gstNumber"), PANNUMBER(1, "panNumber"), DOB(2, "dob");

	private int id;
	private String eName;

	private UserDataAVEnum(int id, String eName) {

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
