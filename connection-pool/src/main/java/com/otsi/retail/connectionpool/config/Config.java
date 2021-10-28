package com.otsi.retail.connectionpool.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {
	
	@Value("${getAllStoresFromURM_url}")
	public String getAllStoresFromURM;

}
