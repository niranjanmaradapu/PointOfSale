package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoyalityPointsVo {

	private Long loyaltyId;
	
	private Long userId;
	
	private Long domainId;
	
	private String mobileNumber;
	
	private String customerName;
	
	private String invoiceNumber;
	
	private Long invoiceAmount;
	
	private Long loyaltyPoints;
	
	private int numberOfMonths;
	
	private LocalDate invoiceCreatedDate;
	
	private LocalDate createdDate;
	
	private LocalDate expiredDate;
	

}
