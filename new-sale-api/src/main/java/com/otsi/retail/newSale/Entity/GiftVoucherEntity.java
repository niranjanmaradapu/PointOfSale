package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gift_voucher")
public class GiftVoucherEntity {

	@Id
	@GeneratedValue
	private Long gvId;

	private Long userId;

	private String gvNumber;

	private String description;

	private Boolean isTagged;

	private LocalDate expiryDate;

	private Long totalAmount;

	private Long leftOverAmount;

	private LocalDate createdDate;

}
