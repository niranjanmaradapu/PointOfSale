/**
 * 
 */
package com.otsi.retail.connectionpool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.connectionpool.entity.StoresEntity;

/**
 * @author Sudheer
 *
 */
@Repository
public interface StoreRepo extends JpaRepository<StoresEntity, Long> {

	List<StoresEntity> findByStoreIdIn(List<Long> storeId);

	List<StoresEntity> findByStoreNameIn(List<String> storeName);

	Optional<StoresEntity> findByStoreName(String storeName);

}
