package com.otsi.retail.newSale.Exceptions;

public class BusinessException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int RECORD_NOT_FOUND_STATUSCODE= 404;
	public static final String RNF_DESCRIPTION = "record not found";
	
	private String message;
	private int statusCode;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public BusinessException(String message, int statusCode) {
		super(message);
		this.message = message;
		this.statusCode = statusCode;
	}
	

}
