package com.otsi.retail.newSale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.LineItemsReEntity;

@Repository
public interface LineItemReRepo extends JpaRepository<LineItemsReEntity, Long>{

	List<LineItemsReEntity> findByLineItemReIdIn(List<Long> lineItemIds);
}
