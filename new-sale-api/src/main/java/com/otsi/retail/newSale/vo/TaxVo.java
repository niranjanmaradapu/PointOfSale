/**
 * vo for Tax
 */
package com.otsi.retail.newSale.vo;

import lombok.Data;

/**
 * @author vasavi
 *
 */
@Data
public class TaxVo {
	
	private long id;
	private String taxLabel;
	private float sgst;
	private float cgst;
	private float igst;
	private float cess;

}

