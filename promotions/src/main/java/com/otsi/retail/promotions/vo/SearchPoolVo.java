/**
 * 
 */
package com.otsi.retail.promotions.vo;

import java.time.LocalDate;

import com.otsi.retail.promotions.common.PoolType;

import lombok.Data;

/**
 * @author Sudheer
 *
 */
@Data
public class SearchPoolVo {
	
	private Long poolId;
	
	private Long clientId;
	
	private String poolName;

	private PoolType poolType;
	
	private Long createdBy;

	private LocalDate createdDate;

	private Boolean isActive;
	
	

}
