package com.otsi.retail.customerManagement.config;

import java.util.List;

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

}
