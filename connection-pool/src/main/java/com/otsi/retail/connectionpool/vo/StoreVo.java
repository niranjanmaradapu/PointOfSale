package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class StoreVo {
	
	private Long storeId;
	
	private String storeName;
	
	private String storeDescription;
	
	private LocalDate createdDate;
	
	private LocalDate lastModifiedDate;
	
	


}
