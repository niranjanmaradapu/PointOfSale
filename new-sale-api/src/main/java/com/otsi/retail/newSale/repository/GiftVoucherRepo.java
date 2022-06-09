package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.GiftVoucherEntity;

@Repository
public interface GiftVoucherRepo extends JpaRepository<GiftVoucherEntity, Long> {

	Optional<GiftVoucherEntity> findByGvNumber(String gvNumber);

	//List<GiftVoucherEntity> findByUserIdAndExpiryDateGreaterThanEqual(Long userId, LocalDate now);

	List<GiftVoucherEntity> findByGvNumberIn(List<String> gvsList);

	Optional<GiftVoucherEntity> findByGvNumberAndClientId(String gvNumber, Long clientId);
	
	//@Modifying
	//@Query(value = "Select gv from GiftVoucherEntity gv where gv.gvNumber like '%gvNumber%'",nativeQuery = true)
	//public List<GiftVoucherEntity> searchByGvNumberOrFromDateLike(@Param("gvNumber") String gvNumber);

	@Query("from GiftVoucherEntity s where DATE(s.fromDate) = :fromDate")
	List<GiftVoucherEntity> findByfromDateLike(@Param("fromDate")LocalDate fromDate);

	List<GiftVoucherEntity> findByGvNumberLike(String gvNumber);
	
	@Query("from GiftVoucherEntity s where DATE(s.toDate) = :toDate")
	List<GiftVoucherEntity> findBytoDateLike(@Param("toDate")LocalDate toDate);

	List<GiftVoucherEntity> findByFromDateAndToDateAndGvNumber(LocalDate fromDate, LocalDate toDate, String gvNumber);

	List<GiftVoucherEntity> findByFromDateAndToDate(LocalDate fromDate, LocalDate toDate);

	List<GiftVoucherEntity> findByFromDateAndGvNumber(LocalDate fromDate, String gvNumber);

	List<GiftVoucherEntity> findByIsActivated(Boolean isActivated);

	List<GiftVoucherEntity> findByFromDate(LocalDate fromDate);

	List<GiftVoucherEntity> findByToDate(LocalDate toDate);
	
	
}
