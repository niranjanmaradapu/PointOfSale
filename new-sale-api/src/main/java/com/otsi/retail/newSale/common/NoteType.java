package com.otsi.retail.newSale.common;

public enum NoteType {

	Normal(0, "Normal"), Debit(1, "Debit"), Credit(2, "Credit");

	private int id;
	private String name;

	NoteType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
