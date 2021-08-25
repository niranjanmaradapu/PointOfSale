package com.customer.CustomerDetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
//@EnableSwagger2
//@OpenAPIDefinition(info = @Info(title = "customer-service", version = "1.0", description = "Documentation"))
public class CustomerDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerDetailsApplication.class, args);
	}
	/*
	 * @Bean public Docket swaggerPersonApi10() { return new
	 * Docket(DocumentationType.SWAGGER_2) .select()
	 * .apis(RequestHandlerSelectors.basePackage(
	 * "com.customer.CustomerDetails.controller")) .paths(PathSelectors.any())
	 * .build() .apiInfo(new
	 * ApiInfoBuilder().version("1.0").title("customer-service").description(
	 * "Documentation").build()); }
	 */

}
