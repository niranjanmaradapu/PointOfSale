package com.otsi.retail.newSale.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.otsi.retail.newSale.vo.HsnDetailsVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

	public List<HsnDetailsVo> vo ;
	
	@Value("${savecustomer_url}")
	private String url;

	@Value("${getNewSaleWithHsn_url}")
	private String HsnUrl;
	
	@Value("${getreturnslip_url}")
    private String getListOfReturnSlips;
	
	@Value("${getCustomerDetailsFromURM_url}")
	private String getCustomerDetails;

}
