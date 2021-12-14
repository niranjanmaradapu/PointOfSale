package com.otsi.retail.customerManagement.repo;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.customerManagement.model.ReturnSlip;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;

/**
 * @author lakshmi
 *
 */
@Repository
public interface ReturnSlipRepo extends JpaRepository<ReturnSlip, Long> {
	List<ReturnSlip> findByCreatedDateBetweenAndRtNo(LocalDate dateFrom, LocalDate dateTo,
			String rtNumber);

	List<ReturnSlip> findByCreatedDateBetweenAndIsReviewed(LocalDate dateFrom, LocalDate dateTo,
			Boolean rtReviewStatus);

	List<ReturnSlip> findByCreatedDateBetweenAndRtStatus(LocalDate dateFrom, LocalDate dateTo,
			String rtStatus);

	//List<ReturnSlip> findByCreatedDateBetweenAndBarcode(LocalDate dateFrom, LocalDate dateTo,
	//		String barcode);

	List<ReturnSlip> findByCreatedDateBetween(LocalDate dateFrom, LocalDate dateTo);

	//ReturnSlip findByRtNo(String rtNumber);

	List<ReturnSlip> findByRtStatus(String rtStatus);

	//List<ReturnSlip> findByCreditNote(String creditNote);

	// List<listOfReturnSlipsModel> findByBarcode(List<Barcode> list);

	List<ReturnSlip> findByIsReviewed(Boolean rtReviewStatus);

	// List<listOfReturnSlipsModel> findById(int id);

	// List<listOfReturnSlipsModel> findByBarcodeIn(List<Barcode> barcode);

	//List<ReturnSlip> findByBarcode(String barcode);

	// List<listOfReturnSlipsModel> findByCreatedInfoBetweenAndId(LocalDate
	// dateFrom, LocalDate dateTo, int id);

	ReturnSlip findByRsId(int rsId);

//	List<ReturnSlip> findByCreatedDateBetweenAndCrNo(LocalDate dateFrom, LocalDate dateTo,
//			String creditNote);

	List<ReturnSlip> findByCreatedDateBetweenAndCrNo(LocalDate localDate, LocalDate dateTo, String creditNote);

	List<ReturnSlip> findByCrNo(String creditNote);

	

	List<ReturnSlip> findByTaggedItems_barCode(String barcode);

	List<ReturnSlip> findByCreatedDateBetweenAndTaggedItems_barCode(LocalDate dateFrom, LocalDate dateTo,
			String barcode);

	List<ReturnSlip> findByCreatedDateBetweenAndRtNoOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo,
			String rtNumber);

	List<ReturnSlip> findByCreatedDateBetweenAndCrNoOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo,
			String creditNote);

	List<ReturnSlip> findByCreatedDateBetweenAndRtStatusOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo,
			String rtStatus);

	List<ReturnSlip> findByCreatedDateBetweenAndIsReviewedOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo,
			Boolean rtReviewStatus);

	List<ReturnSlip> findByCreatedDateBetweenAndTaggedItems_barCodeOrderByCreatedDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String barcode);

	List<ReturnSlip> findByCreatedDateBetweenOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo);

	List<ReturnSlip> findByRtNoOrderByCreatedDateAsc(String rtNumber);

	List<ReturnSlip> findByTaggedItems_barCodeOrderByCreatedDateAsc(String barcode);

	List<ReturnSlip> findByIsReviewedOrderByCreatedDateAsc(Boolean rtReviewStatus);

	ReturnSlip findByRtNo(String rtNumber);

	List<ReturnSlip> findByCreatedDateBetweenAndCreatedByOrderByCreatedDateAsc(LocalDate dateFrom, LocalDate dateTo,
			String createdBy);

	List<ReturnSlip> findByCreatedByOrderByCreatedDateAsc(String createdBy);

	ReturnSlip findByRtNoAndRtStatus(String rtNo, int id);

}
