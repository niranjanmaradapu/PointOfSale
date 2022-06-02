package com.otsi.retail.promotions.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.common.Applicability;
import com.otsi.retail.promotions.entity.PromotionsEntity;

@Repository
public interface PromotionRepo extends JpaRepository<PromotionsEntity, Long> {

	List<PromotionsEntity> findByIsActive(Boolean status);
	
//	@Query(value = "SELECT * FROM  PromotionsEntity p WHERE p.id IN (:promoIds)")
//	List<PromotionsEntity> getAllPromotions(@Param("promoIds") List<Long> promoIds);
	

	void deleteByPromoId(Long id);

	// PromotionsEntity findByPromoId(Long promoId);

	List<PromotionsEntity> findByPromotionName(String promoName);

	PromotionsEntity findByPromotionNameIs(String promoName);

	PromotionsEntity findByPromoId(Long promoId);
	

	// boolean existsByStoreName(String storeName);

	List<PromotionsEntity> findByPromoIdIs(Long promoId);

	List<PromotionsEntity> findByDomainId(Long domainId);

	List<PromotionsEntity> findByIsActiveAndDomainId(Boolean status, Long domainId);

	List<PromotionsEntity> findByPromotionStartDateAndPromotionEndDateAndPromotionNameAndIsActive(
			LocalDate promotionStartDate, LocalDate promotionEndDate, String promotionName, Boolean isActive);

	List<PromotionsEntity> findByPromotionStartDateAndPromotionEndDateAndIsActive(LocalDate promotionStartDate,
			LocalDate promotionEndDate, Boolean isActive);

	List<PromotionsEntity> findByPromotionStartDateAndPromotionEndDateAndPromoId(LocalDate promotionStartDate,
			LocalDate promotionEndDate, Long promoId);

	List<PromotionsEntity> findByPromotionStartDateAndPromotionEndDate(LocalDate promotionStartDate,
			LocalDate promotionEndDate);

	List<PromotionsEntity> findByPromoIdInAndApplicability(List<Long> promoids, Applicability applicability);

	List<PromotionsEntity> findByPromoIdInAndIsActive(List<Long> promoIds, Boolean true1);

	List<PromotionsEntity> findByApplicability(Applicability promotionforeachbarcode);

	List<PromotionsEntity> findByIsActiveAndApplicability(Boolean promotionStatus, Applicability promotionforeachbarcode);

	List<PromotionsEntity> findByStoreId(Long storeId);

	List<PromotionsEntity> findByClientId(Long clientId);

	List<PromotionsEntity> findByIsActiveAndDomainIdAndClientId(Boolean status, Long domainId, Long clientId);

	Page<PromotionsEntity> findByIsActiveAndApplicabilityAndClientId(Boolean isActive,
			Applicability promotionforeachbarcode, Long clientId, Pageable pageable);

	Page<PromotionsEntity> findByApplicabilityAndClientId(Applicability promotionforeachbarcode, Long clientId, Pageable pageable);

	Page<PromotionsEntity> findByIsActiveAndClientId(Boolean isActive, Long clientId, Pageable pageable);

	
}
