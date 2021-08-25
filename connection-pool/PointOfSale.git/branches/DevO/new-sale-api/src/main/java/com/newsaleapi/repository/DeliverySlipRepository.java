package com.newsaleapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newsaleapi.Entity.DeliverySlipEntity;

@Repository
public interface DeliverySlipRepository extends JpaRepository<DeliverySlipEntity, Long> {

}
