package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;

@Repository
public interface NewSaleRepository extends JpaRepository<NewSaleEntity, Long> {

	List<NewSaleEntity> findByCreationDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, String billStatus);

	Optional<NewSaleEntity> findByOrderId(Long newsaleId);

	List<NewSaleEntity> findByOrderNumber(String billNumber);

	List<NewSaleEntity> findByOrderNumber(Long invoiceNumber);

	List<NewSaleEntity> findByCreationDateBetween(LocalDate dateFrom, LocalDate dateTo);
	
	List<NewSaleEntity> findByUserId(Long userId);

	List<NewSaleEntity> findByCustomerDetailsMobileNumberAndCreationDateBetween(String mobileNo, LocalDate fromDate, LocalDate toDate);

	Optional<CustomerDetailsEntity> findByCustomerDetailsMobileNumber(String mobileNo);

	List<NewSaleEntity> findByCreatedBy(String empId);

	List<NewSaleEntity> findByCreationDateBetweenAndStoreId(LocalDate localDate, LocalDate localDate2, long id);

	List<NewSaleEntity> findByUserId(LongStream userId);

	List<NewSaleEntity> findByUserIdIn(List<Long> userIds);


}
