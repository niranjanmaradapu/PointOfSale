package com.otsi.retail.connectionpool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.connectionpool.common.PoolType;
import com.otsi.retail.connectionpool.entity.PoolEntity;


@Repository
public interface PoolRepo extends JpaRepository<PoolEntity, Long> {

	List<PoolEntity> findByIsActive(Boolean isActive);

	List<PoolEntity> findByPoolIdInAndIsActive(List<Long> poolIds, Boolean true1);

	Optional<PoolEntity> findByPoolId(Long poolId);

	List<PoolEntity> findByCreatedByAndPoolTypeAndIsActive(String createdBy, PoolType poolType, Boolean isActive);

	List<PoolEntity> findByCreatedByAndIsActive(String createdBy, Boolean isActive);

	List<PoolEntity> findByPoolTypeAndIsActive(PoolType poolType, Boolean isActive);

	List<PoolEntity> findByCreatedBy(String createdBy);

	List<PoolEntity> findByPoolType(PoolType poolType);

	List<PoolEntity> findByCreatedByAndPoolType(String createdBy, PoolType poolType);

	List<PoolEntity> findByIsActiveAndDomainId(Boolean flag, Long domainId);

	List<PoolEntity> findByDomainId(Long domainId);


}
