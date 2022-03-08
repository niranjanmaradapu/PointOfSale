package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LineItemVo {

	private Long lineItemId;

	private Long domainId;

	private String barCode;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private Long discount;

	private Long netValue;

	private LocalDate creationDate;

	private LocalDate lastModified;

}
