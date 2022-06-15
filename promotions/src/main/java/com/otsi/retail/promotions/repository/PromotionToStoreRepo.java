package com.otsi.retail.promotions.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.otsi.retail.promotions.entity.PromotionToStoreEntity;



public interface PromotionToStoreRepo  extends JpaRepository<PromotionToStoreEntity, Long>{

	PromotionToStoreEntity findByPromoId(Long promoId);
	
	//List<Long> findPromosByStore(Long storeId);
    
	@Query(value="select * from pos_promotion_store_mappings as p where p.store_id=:storeId and p.promotion_status=:promoStatus order by p.priority asc",nativeQuery=true)
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

	Page<PromotionToStoreEntity> findByStartDateAndEndDateAndPromoId(LocalDate startDate, LocalDate endDate,
			Long promoId, Pageable pageable);

	Page<PromotionToStoreEntity> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate, Pageable pageable);

	Page<PromotionToStoreEntity> findByPromoIdIs(Long promoId, Pageable pageable);

	Page<PromotionToStoreEntity> findByStartDateAndEndDateAndStoreName(LocalDate startDate, LocalDate endDate, String storeName, Pageable pageable);

	Page<PromotionToStoreEntity> findByStartDateAndEndDateAndPromoIdAndStoreName(LocalDate startDate, LocalDate endDate,
			Long promoId, String storeName, Pageable pageable);

	Page<PromotionToStoreEntity> findByStartDateAndEndDateAndPromotionStatusAndClientId(LocalDate startDate,
			LocalDate endDate, Boolean promotionStatus, Long clientId, Pageable pageable);

	Page<PromotionToStoreEntity> findByPromotionNameAndClientId(String promotionName, Long clientId, Pageable pageable);

	Page<PromotionToStoreEntity> findByPromotionStatusAndClientId(Boolean promotionStatus, Long clientId,
			Pageable pageable);

	Page<PromotionToStoreEntity> findByStoreNameAndClientId(String storeName, Long clientId, Pageable pageable);

	Page<PromotionToStoreEntity> findByStoreName(String storeName, Pageable pageable);

	
}
