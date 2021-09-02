package com.otsi.retail.promoexchange.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promoexchange.Entity.BarcodeEntity;

@Repository
public interface BarcodeRepository extends JpaRepository<BarcodeEntity, Long> {

	BarcodeEntity findByBarcode(String barCode);

	List<BarcodeEntity> findByBarcodeIn(List<String> barcodeList);

}
