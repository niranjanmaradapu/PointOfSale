package com.otsi.retail.promoexchange.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promoexchange.Entity.CustomerDetailsEntity;

@Repository
public interface CustomerDetailsRepo extends JpaRepository<CustomerDetailsEntity, Long> {

	Optional<CustomerDetailsEntity> findByMobileNumber(String mobileNumber);

}
