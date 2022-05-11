/**
 * 
 */
package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
public class GiftVoucherSearchVo {
	
	private String gvNumber;
	
	private LocalDate fromDate;

	private LocalDate toDate;

}
