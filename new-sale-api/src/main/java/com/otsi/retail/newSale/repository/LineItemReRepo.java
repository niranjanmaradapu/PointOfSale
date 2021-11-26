package com.otsi.retail.newSale.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.LineItemsReEntity;

@Repository
public interface LineItemReRepo extends JpaRepository<LineItemsReEntity, Long>{

	List<LineItemsReEntity> findByLineItemReIdIn(List<Long> lineItemIds);
	
	LineItemsReEntity findByLineItemReId(Long lineItemId);
	
	Optional<LineItemsReEntity> findByBarCode(String barCode);

	List<LineItemsReEntity> findByLineItemReIdInAndOrderIdIsNull(List<Long> lineItemIds);

	//List<LineItemsReEntity> findByBarcodeIn(List<String> barCode);

	List<LineItemsReEntity> findByBarCodeIn(List<String> barCode);
}
