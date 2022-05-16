/**
 * 
 */
package com.otsi.retail.newSale.vo;

import lombok.Data;

/**
 * @author ashok
 *
 */
@Data
public class TaggedItemsVo {
	
	private Long taggedItemId;
	private String barCode;
	private int qty;

}
