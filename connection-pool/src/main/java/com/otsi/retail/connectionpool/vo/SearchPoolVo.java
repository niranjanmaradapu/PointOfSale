/**
 * 
 */
package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;

import com.otsi.retail.connectionpool.common.PoolType;

import lombok.Data;

/**
 * @author Sudheer
 *
 */
@Data
public class SearchPoolVo {
	
	private Long poolId;
	
	private String poolName;

	private PoolType poolType;
	
	private String createdBy;

	private LocalDate createdDate;

	private Boolean isActive;
	
	

}
