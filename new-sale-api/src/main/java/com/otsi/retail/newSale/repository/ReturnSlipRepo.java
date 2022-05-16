package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.ReturnSlip;

@Repository
public interface ReturnSlipRepo extends JpaRepository<ReturnSlip, Long> {

	List<ReturnSlip> findByCreatedDateBetweenAndStoreIdAndDomianIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long storeId, Long domainId);

	List<ReturnSlip> findByCreatedDateBetweenAndRtNoAndStoreIdAndDomianIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String rtNumber, Long storeId, Long domainId);

	List<ReturnSlip> findByCreatedDateBetweenAndTaggedItems_barCodeAndStoreIdAndDomianIdOrderByCreatedDateAsc(
			LocalDate dateFrom, LocalDate dateTo, String barcode, Long storeId, Long domainId);

	List<ReturnSlip> findByCreatedDateBetweenAndCreatedByAndStoreIdAndDomianIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String createdBy, Long storeId, Long domainId);

	List<ReturnSlip> findByCreatedDateBetweenAndRtStatusAndStoreIdAndDomianIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String rtStatus, Long storeId, Long domainId);

	List<ReturnSlip> findByRtNoAndStoreIdAndDomianIdOrderByCreatedDateAsc(String rtNumber, Long storeId, Long domainId);

	List<ReturnSlip> findByStoreIdAndDomianIdOrderByCreatedDateAsc(Long storeId, Long domainId);

	List<ReturnSlip> findByTaggedItems_barCodeAndStoreIdAndDomianIdOrderByCreatedDateAsc(String barcode, Long storeId,
			Long domainId);

	List<ReturnSlip> findByRtStatusAndStoreIdAndDomianIdOrderByCreatedDateAsc(String rtStatus, Long storeId,
			Long domainId);

	List<ReturnSlip> findByCreatedByAndStoreIdAndDomianIdOrderByCreatedDateAsc(String createdBy, Long storeId,
			Long domainId);

	ReturnSlip findByRtNo(String rtNumber);

	ReturnSlip findByMobileNumberAndStoreId(String mobileNumber, Long storeId);
	

}
