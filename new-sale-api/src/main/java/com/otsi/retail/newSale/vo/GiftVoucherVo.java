package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GiftVoucherVo {// Don't change these field names because these are mapped with entity object

	private Long gvId;

	private Long clientId;

	private String gvNumber;

	private String description;

	private Boolean isActivated;

	private LocalDate fromDate;

	private LocalDate toDate;

	private Long value;

	private LocalDate creationDate;

}
