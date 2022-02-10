package com.otsi.retail.customerManagement.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.otsi.retail.customerManagement.vo.HsnDetailsVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

	public List<HsnDetailsVo> vo ;
	
	@Value("${NEW_SALE_GET_INVOICEDETAILS_URL}")
	private String invoiceDetails;
	
	@Value("${GET_CUSTOMER_FROM_NEWSALE_URL}")
    private String  customerDetails;
	
	@Value("${TAG_CUSTOMER_TO_INVOICE}")
	private String tagCustomerToInvoice;
	
	@Value("${getbarcodes_url}")
	private String getbarcodesUrl;
	
	@Value("${getNewSaleWithHsn_url}")
	private String HsnUrl;
	

}
