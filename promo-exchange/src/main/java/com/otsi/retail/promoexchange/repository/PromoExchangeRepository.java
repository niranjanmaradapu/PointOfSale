package com.otsi.retail.promoexchange.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;

@Repository
public interface PromoExchangeRepository extends JpaRepository<PromoExchangeEntity, Long> {

	List<PromoExchangeEntity> findByCreatedDateBetweenAndBillStatus(LocalDate dateFrom, LocalDate dateTo, String billStatus);

	List<PromoExchangeEntity> findByPromoExchangeId(Long promoExchangeId);

	List<PromoExchangeEntity> findByBillNumber(String billNumber);

	List<PromoExchangeEntity> findByInvoiceNumber(Long invoiceNumber);

	List<PromoExchangeEntity> findByCreatedDateBetween(LocalDate dateFrom, LocalDate dateTo);

}
