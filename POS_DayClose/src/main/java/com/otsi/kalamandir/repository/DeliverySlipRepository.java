package com.otsi.kalamandir.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.otsi.kalamandir.model.DeliverySlipModel;
import com.otsi.kalamandir.vo.DelivarySlipVo;
//This is interface and extends from jpaRepository for accessing the data from Database
@Repository
public interface DeliverySlipRepository extends JpaRepository<DeliverySlipModel, Integer> {

	DeliverySlipModel save(DelivarySlipVo dsvo);

	List<DeliverySlipModel> findByIsOpenAndCloseDate(Boolean true1, LocalDate date);

}
