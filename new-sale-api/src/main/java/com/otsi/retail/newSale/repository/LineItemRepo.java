package com.otsi.retail.newSale.repository;

import com.otsi.retail.newSale.Entity.LineItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemRepo extends JpaRepository<LineItemsEntity,Long> {
}
