/**
 * 
 */
package com.otsi.retail.newSale.Entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vasavi.yakkali
 *
 */
@Entity
@Table(name = "Day_Closure")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayClosure extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDateTime dayClose;

	private Long storeId;

}
