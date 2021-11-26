package com.otsi.retail.customerManagement.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LineItemVo {

	private Long lineItemId;

	private Long domainId;
	
	private String section;

	private String barCode;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private Long discount;

	private Long netValue;

	private Date creationDate;

	private Date lastModified;
	
	private HsnDetailsVo HsnDetailsVo;


}
