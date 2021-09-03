package com.otsi.kalamandhir.repo;

import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.kalamandhir.model.ReturnSlip;

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

	List<ReturnSlip> findByRtNo(String rtNumber);

	List<ReturnSlip> findByRtStatus(String rtStatus);

	//List<ReturnSlip> findByCreditNote(String creditNote);

	// List<listOfReturnSlipsModel> findByBarcode(List<Barcode> list);

	List<ReturnSlip> findByIsReviewed(Boolean rtReviewStatus);

	// List<listOfReturnSlipsModel> findById(int id);

	// List<listOfReturnSlipsModel> findByBarcodeIn(List<Barcode> barcode);

	//List<ReturnSlip> findByBarcode(String barcode);

	// List<listOfReturnSlipsModel> findByCreatedInfoBetweenAndId(LocalDate
	// dateFrom, LocalDate dateTo, int id);

	List<ReturnSlip> findByRsId(int rsId);

//	List<ReturnSlip> findByCreatedDateBetweenAndCrNo(LocalDate dateFrom, LocalDate dateTo,
//			String creditNote);

	List<ReturnSlip> findByCreatedDateBetweenAndCrNo(LocalDate dateFrom, LocalDate dateTo, String creditNote);

	List<ReturnSlip> findByCrNo(String creditNote);

	List<ReturnSlip> findByCreatedDateBetweenAndTaggedItems_barCode(LocalDate dateFrom, LocalDate dateTo,
			String barcode);

	List<ReturnSlip> findByTaggedItems_barCode(String barcode);

}
