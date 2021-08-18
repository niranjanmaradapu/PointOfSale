package com.otsi.mapping.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.otsi.mapping.model.StoreModel;

//purpose : for accessing database
@Repository
public interface StoreRepository extends JpaRepository<StoreModel, Long> {

	StoreModel findByStoreName(String storeName);
	

}
