package com.newsaleapi.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newsaleapi.common.DSStatus;

import lombok.Data;

@Component
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
