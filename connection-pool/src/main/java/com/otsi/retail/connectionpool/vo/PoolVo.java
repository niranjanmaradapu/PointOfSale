package com.otsi.retail.connectionpool.vo;

import java.util.List;

import lombok.Data;

@Data
public class PoolVo {
	
private Long domainId;
	
private List<ConnectionPoolVo> poolvo;

}
