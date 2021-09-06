package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftVoucherVo {

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
