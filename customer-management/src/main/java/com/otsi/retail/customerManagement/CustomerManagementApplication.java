package com.otsi.retail.customerManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@EnableDiscoveryClient
@SpringBootApplication
public class CustomerManagementApplication {
	@Bean
	// @LoadBalanced
	public RestTemplate getRestResponse() {
		return new RestTemplate();
	}
	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApplication.class, args);
	}

}