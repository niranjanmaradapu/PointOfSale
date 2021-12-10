/**
 * 
 */
package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SearchLoyaltyPointsVo {
	
	private String mobileNumber;
	private String invoiceNumber;

}
