package com.otsi.retail.promoexchange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {
	
	@Value("${getdeliveryslip_url}")
	private String getDeliveryslipWithDsnumber;

	@Value("${getreturnslip_url}")
	private String getListOfReturnSlipsUrl;
	
	@Value("${savecustomer_url}")
	private String url;
	
	@Value("${GET_DS_DETAILS_FROM_NEWSALE_URl}")
	private String getDsDetails;

}
