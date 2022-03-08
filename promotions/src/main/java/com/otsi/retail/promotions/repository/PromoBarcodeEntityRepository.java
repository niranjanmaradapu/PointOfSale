package com.otsi.retail.promotions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.entity.PromoBarcodeEntity;

@Repository
public interface PromoBarcodeEntityRepository extends JpaRepository<PromoBarcodeEntity, Long> {

	PromoBarcodeEntity findByBarCode(String barCode);

}
