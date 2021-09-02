package com.otsi.retail.promoexchange.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promoexchange.common.DSStatus;

import lombok.Data;


@Data
public class ListOfDeliverySlipVo {

	private String dsNumber;
	
	private DSStatus status;
	
	private String barcode;

	private LocalDate dateFrom;

	private LocalDate dateTo;

	private Long toatalPromoDisc;

	private Long totalNetAmount;

	private Long totalGrossAmount;
	
	private List<DeliverySlipVo> deliverySlipVo;

}
