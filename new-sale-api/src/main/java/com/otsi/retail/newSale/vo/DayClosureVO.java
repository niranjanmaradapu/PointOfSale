/**
 * 
 */
package com.otsi.retail.newSale.vo;

import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

/**
 * @author vasavi.yakkali
 *
 */

@Data
public class DayClosureVO {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDateTime dayClose;

	private Long storeId;

	private Long createdBy;

	private LocalDateTime createdDate;

	private Long modifiedBy;

	private LocalDateTime lastModifiedDate;

}
