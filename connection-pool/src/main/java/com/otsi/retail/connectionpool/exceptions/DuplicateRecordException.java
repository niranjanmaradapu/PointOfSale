package com.otsi.retail.connectionpool.exceptions;

/**
 * DuplicateRecordException is thrown while given values are exists in db.
 * @author vasavi
 *
 */
public class DuplicateRecordException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String msg;

	/**
	 * @param msg
	 */
	public DuplicateRecordException(String msg) {
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



