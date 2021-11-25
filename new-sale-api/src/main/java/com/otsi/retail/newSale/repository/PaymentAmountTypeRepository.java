package com.otsi.retail.newSale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.PaymentAmountType;
@Repository
public interface PaymentAmountTypeRepository extends JpaRepository<PaymentAmountType, Long> {

	PaymentAmountType findByRazorPayId(String razorPayId);

}

