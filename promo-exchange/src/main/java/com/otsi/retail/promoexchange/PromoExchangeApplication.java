package com.otsi.retail.promoexchange;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
public class PromoExchangeApplication {
	
	@Bean
	//@LoadBalanced
	public RestTemplate getRestResponse() {
		return new RestTemplate();
	}

	public static void main(String[] args) throws RestClientException, IOException {
		
	 SpringApplication.run(PromoExchangeApplication.class, args);
		
		
	}
	
	

}
