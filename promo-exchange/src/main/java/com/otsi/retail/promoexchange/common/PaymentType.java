package com.otsi.retail.promoexchange.common;

public enum PaymentType {

	Cash(1, "Cash"), Card(2, "Card"),
	OtherPayments(3, "OtherPayments"), UPI(4, "UPI");

	private int num;

	private String type;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private PaymentType(int num, String type) {

		this.num = num;
		this.type = type;

	}

}
