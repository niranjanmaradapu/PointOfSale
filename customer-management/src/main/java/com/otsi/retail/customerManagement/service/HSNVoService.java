package com.otsi.retail.customerManagement.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Singleton;
import com.otsi.retail.customerManagement.config.Config;
import com.otsi.retail.customerManagement.gatewayresponse.GateWayResponse;
import com.otsi.retail.customerManagement.vo.HsnDetailsVo;



@Component
public class HSNVoService {

	@Autowired
	private RestTemplate template;

	@Autowired
	private Config config;

	@Value("${getNewSaleWithHsn.url}")
	private String HsnUrl;

	public List<HsnDetailsVo> getHsn() {
		

		if (config.getVo() == null) {

			//System.out.println("hits");
			
			ResponseEntity<?> hsnResponse = template.exchange(HsnUrl, HttpMethod.GET, null, GateWayResponse.class);
			ObjectMapper mapper = new ObjectMapper();

			GateWayResponse<?> gatewayResponse = mapper.convertValue(hsnResponse.getBody(), GateWayResponse.class);

			List<HsnDetailsVo> vo1 = mapper.convertValue(gatewayResponse.getResult(),
					new TypeReference<List<HsnDetailsVo>>() {
					});
			config.setVo(vo1);
		}
		return config.getVo();

	}

}
