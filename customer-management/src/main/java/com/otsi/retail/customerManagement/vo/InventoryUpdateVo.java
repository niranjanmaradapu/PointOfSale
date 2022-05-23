package com.otsi.retail.customerManagement.vo;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ToString
public class InventoryUpdateVo {

	private int quantity;

	private Long lineItemId;

	private String barCode;
	
	private Long storeId;
	
	private Long domainId;

}
