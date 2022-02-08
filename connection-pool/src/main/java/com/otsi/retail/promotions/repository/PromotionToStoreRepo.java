package com.otsi.retail.promotions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.promotions.entity.PromotionToStoreEntity;

public interface PromotionToStoreRepo  extends JpaRepository<PromotionToStoreEntity, Long>{

}
