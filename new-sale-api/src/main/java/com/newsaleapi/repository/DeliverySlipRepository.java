package com.newsaleapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newsaleapi.Entity.DeliverySlipEntity;
import com.newsaleapi.common.DSStatus;

@Repository
public interface DeliverySlipRepository extends JpaRepository<DeliverySlipEntity, Long> {

	List<DeliverySlipEntity> findByDsNumberIn(List<String> dlsList);

	DeliverySlipEntity findByDsNumber(String dsNumber);

	List<DeliverySlipEntity> findByDsId(Long dsId);

	List<DeliverySlipEntity> findByCreatedDateBetweenAndDsId(LocalDate dateFrom, LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreatedDateBetweenAndDsNumber(LocalDate dateFrom, LocalDate dateTo, String dsNumber);

	List<DeliverySlipEntity> findByCreatedDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByStatus(DSStatus status);

	List<DeliverySlipEntity> findByCreatedDateBetween(LocalDate dateFrom, LocalDate dateTo);

	// List<DeliverySlipEntity> findByDsNumberIn(List<DeliverySlipVo> dlSlips);

}