/**
 * 
 */
package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.otsi.retail.newSale.Entity.DayClosure;

/**
 * @author vasavi.yakkali
 *
 */
@Repository
public interface DayClosureRepository extends JpaRepository<DayClosure, Long> {

	DayClosure findByDayClose(LocalDateTime dayClose);

	DayClosure findByStoreId(Long storeId);

	DayClosure findByStoreIdAndDayClose(Long storeId, LocalDate createdDate1);

	DayClosure findByStoreIdAndDayClose(Long storeId, LocalDateTime createdDate);

}
