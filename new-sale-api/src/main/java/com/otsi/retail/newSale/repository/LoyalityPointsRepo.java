package com.otsi.retail.newSale.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.newSale.Entity.LoyalityPointsEntity;

public interface LoyalityPointsRepo extends JpaRepository<LoyalityPointsEntity, Long> {

	LoyalityPointsEntity findByLoyaltyId(Long loyaltyId);

	LoyalityPointsEntity findByUserId(Long userId);

}
