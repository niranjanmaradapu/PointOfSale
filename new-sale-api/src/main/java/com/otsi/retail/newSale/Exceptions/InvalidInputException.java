package com.otsi.retail.newSale.Exceptions;

public class InvalidInputException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private String msg;

	

	/**
	 * @param msg
	 */
	public InvalidInputException(String msg) {
		super();
		this.msg = msg;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}