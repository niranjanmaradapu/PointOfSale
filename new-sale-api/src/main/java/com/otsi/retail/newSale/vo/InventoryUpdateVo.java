package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class InventoryUpdateVo {

	private int quantity;

	private Long lineItemId;

	private String barCode;

}
