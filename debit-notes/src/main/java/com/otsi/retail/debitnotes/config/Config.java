package com.otsi.retail.debitnotes.config;

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
	
	@Value("${getcustomerdetailsbymobilenumber_url}")
	private String customerByMobileNoUrl;

	@Value("${getNewsaleByCustomerId_url}")
	private String newsaleByCustomerId;


}
