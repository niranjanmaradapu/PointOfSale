package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.GiftVoucherEntity;

@Repository
public interface GiftVoucherRepo extends JpaRepository<GiftVoucherEntity, Long> {

	Optional<GiftVoucherEntity> findByGvNumber(String gvNumber);

	//List<GiftVoucherEntity> findByUserIdAndExpiryDateGreaterThanEqual(Long userId, LocalDate now);

	List<GiftVoucherEntity> findByGvNumberIn(List<String> gvsList);

	Optional<GiftVoucherEntity> findByGvNumberAndClientId(String gvNumber, Long clientId);

}
