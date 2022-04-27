package com.otsi.retail.promotions.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.common.PoolType;
import com.otsi.retail.promotions.entity.PoolEntity;


@Repository
public interface PoolRepo extends JpaRepository<PoolEntity, Long> {

	List<PoolEntity> findByIsActive(Boolean isActive);

	List<PoolEntity> findByPoolIdInAndIsActive(List<Long> poolIds, Boolean true1);

	Optional<PoolEntity> findByPoolId(Long poolId);

	List<PoolEntity> findByPoolTypeAndIsActive(PoolType poolType, Boolean isActive);


	List<PoolEntity> findByPoolType(PoolType poolType);

	List<PoolEntity> findByCreatedByAndPoolType(Long createdBy, PoolType poolType);

	List<PoolEntity> findByIsActiveAndDomainId(Boolean flag, Long domainId);

	List<PoolEntity> findByDomainId(Long domainId);

	List<PoolEntity> findByCreatedByAndPoolTypeAndIsActive(Long createdBy, PoolType poolType, Boolean isActive);

	List<PoolEntity> findByCreatedByAndIsActive(Long createdBy, Boolean isActive);

	List<PoolEntity> findByCreatedBy(Long createdBy);

	List<PoolEntity> findByStoreId(Long storeId);

	List<PoolEntity> findByClientId(Long clientId);

	List<PoolEntity> findByIsActiveAndDomainIdAndStoreIdAndClientId(Boolean flag, Long domainId, Long storeId,
			Long clientId);



}
