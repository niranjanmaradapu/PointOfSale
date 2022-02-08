package com.otsi.retail.promotions.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.common.PromotionStatus;
import com.otsi.retail.promotions.entity.PromotionsEntity;

@Repository
public interface PromotionRepo extends JpaRepository<PromotionsEntity, Long> {

	List<PromotionsEntity> findByIsActive(Boolean status);

	void deleteByPromoId(Long id);

	//PromotionsEntity findByPromoId(Long promoId);

	List<PromotionsEntity> findByPromotionName(String promoName);
	PromotionsEntity findByPromotionNameIs(String promoName);
	

	

	List<PromotionsEntity> findByPromotionNameAndIsActive(String promotionName, boolean promotionStatus);

	List<PromotionsEntity> findByStoreNameAndIsActive(String storeName, boolean promotionStatus);

	List<PromotionsEntity> findByStoreName(String storeName);

	List<PromotionsEntity> findByStoreNameAndPromotionName(String storeName, String promotionName);

	PromotionsEntity findByPromoId(Long promoId);

	boolean existsByStoreName(String storeName);
	//

	List<PromotionsEntity> findByStartDateAndEndDateAndStoreName(LocalDate startDate, LocalDate endDate,
			String storeName);

	List<PromotionsEntity> findByStartDateAndEndDateAndPromoIdAndStoreName(LocalDate startDate, LocalDate endDate,
			Long promoId, String storeName);

	List<PromotionsEntity> findByPromoIdIs(Long promoId);

	List<PromotionsEntity> findByPromoIdAndStoreName(Long promoId, String storeName);

	List<PromotionsEntity> findByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

	List<PromotionsEntity> findByStartDateAndEndDateAndPromoId(LocalDate startDate, LocalDate endDate, Long promoId);

	List<PromotionsEntity> findByDomainId(Long domainId);

	List<PromotionsEntity> findByIsActiveAndDomainId(Boolean status, Long domainId);

	List<PromotionsEntity> findByStartDateAndEndDateAndPromotionNameAndStoreNameAndIsActive(LocalDate startDate,
			LocalDate endDate, String promotionName, String storeName, Boolean isActive);

	List<PromotionsEntity> findByStartDateAndEndDateAndStoreNameAndIsActive(LocalDate startDate, LocalDate endDate,
			String storeName, Boolean isActive);

	List<PromotionsEntity> findByStartDateAndEndDateAndPromotionNameAndIsActive(LocalDate startDate, LocalDate endDate,
			String promotionName, Boolean isActive);

	List<PromotionsEntity> findByStartDateAndEndDateAndIsActive(LocalDate startDate, LocalDate endDate,
			Boolean isActive);

	

	

}
