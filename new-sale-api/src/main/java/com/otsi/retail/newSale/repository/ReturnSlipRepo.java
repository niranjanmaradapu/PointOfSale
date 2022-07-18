
package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	Page<ReturnSlip> findByCreatedDateBetweenAndStoreIdOrderByCreatedDateAsc(LocalDateTime createdDatefrom, LocalDateTime createdDateTo,
			Long storeId, Pageable pageable);

	Page<ReturnSlip> findByCreatedDateBetweenAndRtNoAndStoreIdOrderByCreatedDateAsc(LocalDateTime createdDateTo,
			LocalDateTime createdDateTo2, String rtNumber, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByCreatedDateBetweenAndTaggedItems_barCodeAndStoreIdOrderByCreatedDateAsc(LocalDateTime createdDatefrom,
			LocalDateTime createdDateTo, String barcode, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByCreatedDateBetweenAndCreatedByAndStoreIdOrderByCreatedDateAsc(LocalDateTime createdDatefrom,
			LocalDateTime createdDateTo, Long createdBy, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByCreatedDateBetweenAndRtStatusAndStoreIdOrderByCreatedDateAsc(LocalDateTime createdDatefrom,
			LocalDateTime createdDateTo, ReturnSlipStatus rtStatus, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByRtNoAndStoreIdOrderByCreatedDateAsc(String rtNumber, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByStoreIdOrderByCreatedDateAsc(Long storeId, Pageable pageable);

	Page<ReturnSlip> findByTaggedItems_barCodeAndStoreIdOrderByCreatedDateAsc(String barcode, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByRtStatusAndStoreIdOrderByCreatedDateAsc(ReturnSlipStatus rtStatus, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByCreatedByAndStoreIdOrderByCreatedDateAsc(Long createdBy, Long storeId, Pageable pageable);

	Page<ReturnSlip> findByTaggedItems_barCodeAndRtNoAndStoreIdOrderByCreatedDateAsc(String barcode, String rtNumber,
			Long storeId, Pageable pageable);

}
