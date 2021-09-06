package com.otsi.retail.newSale.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.GiftVoucherEntity;

@Repository
public interface GiftVoucherRepo extends JpaRepository<GiftVoucherEntity, Long> {

	Optional<GiftVoucherEntity> findByGvNumber(String gvNumber);

}
