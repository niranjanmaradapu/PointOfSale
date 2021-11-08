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

	List<DeliverySlipEntity> findByDsId(Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsId(LocalDate dateFrom, LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumber(LocalDate dateFrom, LocalDate dateTo,
			String dsNumber);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByStatus(DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetween(LocalDate dateFrom, LocalDate dateTo);

	List<DeliverySlipEntity> findByStatusAndCreationDate(DSStatus completed, LocalDate now);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatusOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByDsNumberInOrderByCreationDateAsc(List<String> dsList);

	List<DeliverySlipEntity> findByStatusOrderByCreationDateAsc(DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetweenOrderByCreationDateAsc(LocalDate dateFrom, LocalDate dateTo);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsIdAndDsNumberAndStatusOrderByCreationDateAsc(
			LocalDate dateFrom, LocalDate dateTo, Long dsId, String dsNumber, DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumberOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String dsNumber);

	List<DeliverySlipEntity> findByDsNumberInAndOrderIsNull(List<String> dlsList);

}
