package com.otsi.retail.customerManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
//@EnableSwagger2
public class CustomerManagementApplication {
	@Bean
	// @LoadBalanced
	public RestTemplate getRestResponse() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApplication.class, args);
	}
//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2).select()
//				.apis(RequestHandlerSelectors.basePackage("com.otsi.retail")).build();
//	}

}
