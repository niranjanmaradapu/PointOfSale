/**
 * 
 */
package com.otsi.retail.newSale.vo;

import lombok.Data;

/**
 * @author Sudheer.Swamy
 *
 */
@Data
public class UpdateCreditRequest {

	private Long amount;
	private String mobileNumber;
	private Long storeId;
	private String creditDebit;

}
