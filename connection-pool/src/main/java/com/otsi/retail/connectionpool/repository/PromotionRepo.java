package com.otsi.retail.connectionpool.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.connectionpool.common.PromotionStatus;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;

@Repository
public interface PromotionRepo extends JpaRepository<PromotionsEntity, Long> {

	List<PromotionsEntity> findByIsActive(Boolean status);

	void deleteByPromoId(Long id);

	//PromotionsEntity findByPromoId(Long promoId);

	List<PromotionsEntity> findByPromoName(String promoName);
	PromotionsEntity findByPromoNameIs(String promoName);
	

	List<PromotionsEntity> findByStartDateAndEndDateAndPromoNameAndStoreNameAndIsActive(LocalDate startDate,
			LocalDate endDate, String promotionName, String storeName, boolean promotionStatus);

	List<PromotionsEntity> findByStartDateAndEndDateAndStoreNameAndIsActive(LocalDate startDate, LocalDate endDate,
			String storeName, boolean promotionStatus);

	List<PromotionsEntity> findByStartDateAndEndDateAndPromoNameAndIsActive(LocalDate startDate, LocalDate endDate,
			String promotionName, boolean promotionStatus);

	List<PromotionsEntity> findByStartDateAndEndDateAndIsActive(LocalDate startDate, LocalDate endDate,
			boolean promotionStatus);

	List<PromotionsEntity> findByPromoNameAndIsActive(String promotionName, boolean promotionStatus);

	List<PromotionsEntity> findByStoreNameAndIsActive(String storeName, boolean promotionStatus);

	List<PromotionsEntity> findByStoreName(String storeName);

	List<PromotionsEntity> findByStoreNameAndPromoName(String storeName, String promotionName);

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

	

	

}
