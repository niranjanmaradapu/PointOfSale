/**
 * 
 */
package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sudheer.Swamy
 *
 */
@Entity
@Data
@Table(name = "loyalty_points")
@AllArgsConstructor
@NoArgsConstructor
public class LoyalityPointsEntity {

	@Id
	@GeneratedValue
	private Long loyaltyId;

	private Long userId;

	private Long domainId;

	private String mobileNumber;
	
	private String invoiceNumber;
	
	private Long invoiceAmount;
	
	private Long loyaltyPoints;

	@CreationTimestamp
	private LocalDate createdDate;
  
	private LocalDate expiredDate;

}
