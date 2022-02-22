package com.otsi.retail.promotions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.promotions.entity.PromotionToStoreEntity;



public interface PromotionToStoreRepo  extends JpaRepository<PromotionToStoreEntity, Long>{

	PromotionToStoreEntity findByPromoId(Long promoId);
	
	//List<Long> findPromosByStore(Long storeId);

	List<PromotionToStoreEntity> findByStoreIdAndPromotionStatus(Long storeId, Boolean promoStatus);
	

}
