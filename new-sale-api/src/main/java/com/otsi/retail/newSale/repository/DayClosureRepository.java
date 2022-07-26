/**
 * 
 */
package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

	@Query(value = "select * from day_closure order by day_close desc limit 1 ",nativeQuery = true)
	DayClosure findByStoreIdOrderByDayCloseDesc(Long storeId);

}
