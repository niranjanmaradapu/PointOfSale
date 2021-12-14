package com.otsi.retail.newSale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.newSale.Entity.LoyalityPointsEntity;

public interface LoyalityPointsRepo extends JpaRepository<LoyalityPointsEntity, Long> {

	LoyalityPointsEntity findByLoyaltyId(Long loyaltyId);

	LoyalityPointsEntity findByUserId(Long userId);

	List<LoyalityPointsEntity> findByInvoiceNumberAndMobileNumber(String invoiceNumber, String mobileNumber);

	List<LoyalityPointsEntity> findByInvoiceNumber(Object object);

	List<LoyalityPointsEntity> findByMobileNumber(Object object);

}
