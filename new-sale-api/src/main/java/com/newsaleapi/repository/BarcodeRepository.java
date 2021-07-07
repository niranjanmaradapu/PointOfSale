package com.newsaleapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newsaleapi.Entity.BarcodeEntity;

@Repository
public interface BarcodeRepository extends JpaRepository<BarcodeEntity, Long> {

	BarcodeEntity findByBarcode(String barCode);

}
