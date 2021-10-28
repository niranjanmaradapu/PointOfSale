package com.otsi.retail.newSale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LineItemVo {

	private Long lineItemId;

	private Long domainId;

	private Long productId;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private Long discount;

	private Long netValue;

	private LocalDateTime creationDate;

	private LocalDateTime lastModified;

}