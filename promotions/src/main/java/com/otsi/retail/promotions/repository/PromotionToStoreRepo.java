package com.otsi.retail.promotions.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.otsi.retail.promotions.entity.PromotionToStoreEntity;



public interface PromotionToStoreRepo  extends JpaRepository<PromotionToStoreEntity, Long>{

	PromotionToStoreEntity findByPromoId(Long promoId);
	
	//List<Long> findPromosByStore(Long storeId);
    
	@Query(value="select * from pos_promotion_store_mappings as p where p.store_id=:storeId and p.promotion_status=:promoStatus order by p.priority asc limit 1",nativeQuery=true)
	List<PromotionToStoreEntity> findByStoreIdAndPromotionStatus(Long storeId, Boolean promoStatus);

	List<PromotionToStoreEntity> findByPromotionStatus(Boolean isActive);

	List<PromotionToStoreEntity> findByPromotionName(String promotionName);

	List<PromotionToStoreEntity> findByStoreName(String storeName);

	boolean existsByStoreName(String storeName);

	List<PromotionToStoreEntity> findByStartDateAndEndDateAndPromotionStatusAndClientId(LocalDate startDate,
			LocalDate endDate, Boolean promotionStatus, Long clientId);

	List<PromotionToStoreEntity> findByPromotionNameAndClientId(String promotionName, Long clientId);

	List<PromotionToStoreEntity> findByStoreNameAndClientId(String storeName, Long clientId);

	List<PromotionToStoreEntity> findByPromotionStatusAndClientId(Boolean promotionStatus, Long clientId);

	List<PromotionToStoreEntity> findByStartDateAndEndDateAndPromoId(LocalDate startDate, LocalDate endDate,
			Long promoId);

	List<PromotionToStoreEntity> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

	List<PromotionToStoreEntity> findByPromoIdIs(Long promoId);

	List<PromotionToStoreEntity> findByStartDateAndEndDateAndStoreName(LocalDate startDate, LocalDate endDate, String storeName);

	List<PromotionToStoreEntity> findByStartDateAndEndDateAndPromoIdAndStoreName(LocalDate startDate, LocalDate endDate,
			Long promoId, String storeName);

	
}
