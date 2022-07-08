package com.otsi.retail.newSale.common;

public enum PaymentType {

	None(0, "None"), Cash(1, "Cash"), Card(2, "Card"), RTSlip(3, "RTSlip"), PKTPENDING(4, "PKTPENDING"),
	PKTADVANCE(5, "PKTADVANCE"), PHRGVS(6, "PHRGVS"), LoyaltyPoint(7, "LoyaltyPoint"),
	OtherPayments(8, "OtherPayments"), GETQRCODE(9, "GETQRCODE"), UPI(10, "UPI"), GIFTVOUCHER(11,"GiftVoucher"),DEBIT(12,"DEBIT");

	private int id;
	private String type;

	private PaymentType(int id, String type) {

		this.id = id;
		this.type = type;
	}

	public int getNum() {
		return id;
	}

	public void setNum(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
