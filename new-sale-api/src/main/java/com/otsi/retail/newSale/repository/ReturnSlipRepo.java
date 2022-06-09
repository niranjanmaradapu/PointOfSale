
package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.common.ReturnSlipStatus;

@Repository
public interface ReturnSlipRepo extends JpaRepository<ReturnSlip, Long> {

	ReturnSlip findByRtNo(String rtNumber);

	ReturnSlip findByMobileNumberAndStoreId(String mobileNumber, Long storeId);

	ReturnSlip findByRtNoAndStoreId(String returnReferenceNumber, Long storeId);

	ReturnSlip findByInvoiceNumberAndTaggedItems_BarCodeIn(String invoiceNumber, List<String> barcodesIn);

	List<ReturnSlip> findByCreatedDateBetweenAndStoreIdOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo,
			Long storeId);

	List<ReturnSlip> findByCreatedDateBetweenAndRtNoAndStoreIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String rtNumber, Long storeId);

	List<ReturnSlip> findByCreatedDateBetweenAndTaggedItems_barCodeAndStoreIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String barcode, Long storeId);

	List<ReturnSlip> findByCreatedDateBetweenAndCreatedByAndStoreIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long createdBy, Long storeId);

	List<ReturnSlip> findByCreatedDateBetweenAndRtStatusAndStoreIdOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, ReturnSlipStatus rtStatus, Long storeId);

	List<ReturnSlip> findByRtNoAndStoreIdOrderByCreatedDateAsc(String rtNumber, Long storeId);

	List<ReturnSlip> findByStoreIdOrderByCreatedDateAsc(Long storeId);

	List<ReturnSlip> findByTaggedItems_barCodeAndStoreIdOrderByCreatedDateAsc(String barcode, Long storeId);

	List<ReturnSlip> findByRtStatusAndStoreIdOrderByCreatedDateAsc(ReturnSlipStatus rtStatus, Long storeId);

	List<ReturnSlip> findByCreatedByAndStoreIdOrderByCreatedDateAsc(Long createdBy, Long storeId);



}
