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

	Optional<LineItemsEntity> findByBarCode(String barCode);
}
