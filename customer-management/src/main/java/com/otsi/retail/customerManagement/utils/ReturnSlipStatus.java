package com.otsi.retail.customerManagement.utils;

public enum ReturnSlipStatus {

	PENDING(1, "pending"), SETTELED(2, "settled"), CANCLED(3, "cancled");

	private int id;
	private String value;

	ReturnSlipStatus(int id, String string) {
		this.id = id;
		this.value = value;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
