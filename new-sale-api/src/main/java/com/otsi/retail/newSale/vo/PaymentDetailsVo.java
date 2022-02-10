package com.otsi.retail.newSale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDetailsVo {

	private String newsaleOrder;

	private String razorPayId;

	private Long amount;

	private String payType;

}
