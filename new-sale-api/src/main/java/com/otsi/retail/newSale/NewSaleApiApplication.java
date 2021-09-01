package com.otsi.retail.newSale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.otsi.retail.newSale.vo.HsnDetailsVo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "new-sale-api", version = "1.0", description = "Documentation NewSale API v1.0"))
public class NewSaleApiApplication {


	@Bean
	// @LoadBalanced
	public RestTemplate getRestResponse() {
		return new RestTemplate();
	}

	@Bean
	public Docket swaggerPersonApi10() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.otsi.retail.newSale")).paths(PathSelectors.any()).build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("new-sale-api")
						.description("Documentation NewSale API v1.0").build());
	}

	public static List<HsnDetailsVo> vo = null;

	public static void main(String[] args) {
		SpringApplication.run(NewSaleApiApplication.class, args);
	}

}
