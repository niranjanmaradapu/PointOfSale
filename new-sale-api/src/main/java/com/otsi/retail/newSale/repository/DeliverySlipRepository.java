package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.common.DSStatus;

@Repository
public interface DeliverySlipRepository extends JpaRepository<DeliverySlipEntity, Long> {

	List<DeliverySlipEntity> findByDsNumberIn(List<String> dlsList);

	DeliverySlipEntity findByDsNumber(String dsNumber);

	DeliverySlipEntity findByDsId(Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsId(LocalDate dateFrom, LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumber(LocalDate dateFrom, LocalDate dateTo,
			String dsNumber);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByStatus(DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetween(LocalDate dateFrom, LocalDate dateTo);

	List<DeliverySlipEntity> findByStatusAndCreationDate(DSStatus completed, LocalDate now);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatusOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByDsNumberInOrderByCreationDateAsc(List<String> dsList);

	List<DeliverySlipEntity> findByStatusOrderByCreationDateAsc(DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetweenOrderByCreationDateAsc(LocalDate dateFrom, LocalDate dateTo);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdAndDsNumberAndStatusOrderByCreationDateAsc(
			LocalDate dateFrom, LocalDate dateTo, Long dsId, String dsNumber, DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumberOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String dsNumber);

	List<DeliverySlipEntity> findByDsNumberInAndOrderIsNull(List<String> dlsList);

	List<DeliverySlipEntity> findByStoreId(Long storeId);

	List<DeliverySlipEntity> findByUserId(Long u);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdAndDsNumberAndStatusAndStoreIdOrderByCreationDateAsc(
			LocalDate dateFrom, LocalDate dateTo, Long dsId, String dsNumber, DSStatus status, Long storeId);

	DeliverySlipEntity findByDsIdAndStoreId(Long dsId, Long storeId);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long dsId,Long storeId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumberAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String dsNumber,Long storeId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatusAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, DSStatus status,Long storeId);

	List<DeliverySlipEntity> findByDsNumberInAndStoreIdOrderByCreationDateAsc(List<String> dsList, Long storeId);

	List<DeliverySlipEntity> findByStatusAndStoreIdOrderByCreationDateAsc(DSStatus status, Long storeId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long storeId);


}
