package com.otsi.retail.newSale.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.LineItemsEntity;

@Repository
public interface LineItemRepo extends JpaRepository<LineItemsEntity, Long> {

	List<LineItemsEntity> findByLineItemIdIn(List<Long> lineItems);

	LineItemsEntity findByLineItemId(Long lineItemId);
	
	List<LineItemsEntity> findByLineItemIdInAndDsEntityIsNull(List<Long> lineItems);

	List<LineItemsEntity> findByBarCode(String barCode);

	//List<LineItemsEntity> findByBarcodeIn(List<String> barCode);

	List<LineItemsEntity> findByBarCodeIn(List<String> barCode);

	List<LineItemsEntity> findByBarCodeAndStoreId(String barcode, Long storeId);

	List<LineItemsEntity> findByStoreId(Long storeId);

	List<LineItemsEntity> findBySection(Long b);

	List<LineItemsEntity> findBySectionAndStoreId(Long b, Long storeId);
}
