package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.common.OrderStatus;

@Repository
public interface NewSaleRepository extends JpaRepository<NewSaleEntity, Long> {

	List<NewSaleEntity> findByCreatedDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, OrderStatus orderStatus);

	Optional<NewSaleEntity> findByOrderId(Long newsaleId);

	NewSaleEntity findByOrderNumber(String billNumber);

	List<NewSaleEntity> findByOrderNumber(Long invoiceNumber);

	List<NewSaleEntity> findByCreatedDateBetween(LocalDate dateFrom, LocalDate dateTo);
	
	List<NewSaleEntity> findByUserId(Long userId);

	List<NewSaleEntity> findByCustomerDetailsMobileNumberAndCreatedDateBetween(String mobileNo, LocalDate fromDate, LocalDate toDate);

	Optional<CustomerDetailsEntity> findByCustomerDetailsMobileNumber(String mobileNo);

	List<NewSaleEntity> findByCreatedBy(Long empId);

	List<NewSaleEntity> findByCreatedDateBetweenAndStoreId(LocalDateTime createdDatefrom, LocalDateTime createdDatefrom1, long id);

	List<NewSaleEntity> findByUserId(LongStream userId);

	List<NewSaleEntity> findByUserIdIn(List<Long> userIds);

	List<NewSaleEntity> findByUserIdInAndCreatedDateBetween(List<Long> userIds, LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByOrderNumberAndCreatedDateBetween(String invoiceNumber, LocalDate dateFrom,
			LocalDate dateTo);

	List<NewSaleEntity> findByCreatedByAndCreatedDateBetween(String empId, LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByStatus(OrderStatus orderStatus);

	List<NewSaleEntity> findByStoreId(Long n);

	List<NewSaleEntity> findByStoreIdAndDomainId(Long storeId, Long domainId);

	List<NewSaleEntity> findByCreatedDateBetweenAndStatusAndStoreIdAndDomainId(LocalDate dateFrom, LocalDate dateTo,
			OrderStatus billStatus,Long storeId, Long domainId);

	List<NewSaleEntity> findByUserIdInAndStoreIdAndDomainIdAndCreatedDateBetween(List<Long> userIds,
			 Long storeId, Long domainId,LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByOrderNumberAndStoreIdAndDomainIdAndCreatedDateBetween(String invoiceNumber,Long storeId,
			 Long domainId,LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByCreatedByAndStoreIdAndDomainIdAndCreatedDateBetween(Long empId, Long storeId, Long domainId,LocalDate dateFrom,
			LocalDate dateTo);

	List<NewSaleEntity> findByCreatedDateBetweenAndStoreIdAndDomainId(LocalDate dateFrom, LocalDate dateTo,
			Long storeId, Long domainId);

	List<NewSaleEntity> findByStatusAndStoreIdAndDomainId(OrderStatus billStatus, Long storeId, Long domainId);

	List<NewSaleEntity> findByUserIdInAndStoreIdAndDomainId(List<Long> userIds, Long storeId, Long domainId);

	List<NewSaleEntity> findByOrderNumberAndStoreIdAndDomainId(String invoiceNumber, Long storeId, Long domainId);

	List<NewSaleEntity> findByCreatedByAndStoreIdAndDomainId(Long empId, Long storeId, Long domainId);

	List<NewSaleEntity> findByCreatedDateBetweenAndDomainId(LocalDate dateFrom, LocalDate dateTo, Long domainId);

	List<NewSaleEntity> findByCreatedDateBetweenAndDomainIdAndStoreId(LocalDate dateFrom, LocalDate dateTo,
			Long domainId, Long storeId);

	List<NewSaleEntity> findByDomainId(Long domainId);
@Query(value="select sum(sale_sum.net_value) As net_value,EXTRACT(MONTH FROM sale_sum.creation_date)  AS Month from order_table sale_sum where sale_sum.store_id=:storeId and sale_sum.domain_id=:domainId and sale_sum.creation_date>=:dateFrom and sale_sum.creation_date <= :dateto group by Month",nativeQuery=true)
	List<Object[]> getByStoreIdAndDomainIdAndCreatedDateBetween(Long storeId, Long domainId, LocalDate dateFrom,LocalDate dateto);

List<NewSaleEntity> findByStoreIdAndDomainIdAndCreatedDateBetween(Long storeId, Long domainId, LocalDate dateFrom,
		LocalDate dateTo);

List<NewSaleEntity> findByStoreIdAndDomainIdAndCreatedDate(Long storeId, Long domainId, LocalDate date);
@Query(value="select store_id,sum(net_value) As net_value from order_table otable where domain_id=:domainId and creation_date >=:dateFrom and creation_date <=:dateTo group by store_id order by net_value desc limit 5",nativeQuery=true)
List<Object[]> findByDomainIdAndCreatedDateBetween(Long domainId, LocalDate dateFrom, LocalDate dateTo);


//newsaleReport queries

Page<NewSaleEntity> findByCreatedDateBetweenAndStoreId(LocalDateTime fromTime, LocalDateTime fromTimeMax,
		Long storeId, Pageable pageable);

Page<NewSaleEntity> findByOrderNumberAndStoreId(String invoiceNumber, Long storeId, Pageable pageable);

Page<NewSaleEntity> findByCreatedDateBetweenAndStatusAndStoreId(LocalDateTime fromTime, LocalDateTime toTime,
		OrderStatus billStatus, Long storeId, Pageable pageable);

Page<NewSaleEntity> findByUserIdAndStoreIdAndCreatedDateBetween(Long id, Long storeId, LocalDateTime fromTime,
		LocalDateTime toTime, Pageable pageable);

Page<NewSaleEntity> findByCreatedDateBetweenAndStoreIdOrderByCreatedDateDesc(LocalDateTime fromTime,
		LocalDateTime toTime, Long storeId, Pageable pageable);

Page<NewSaleEntity> findByCreatedByAndStoreIdAndCreatedDateBetween(Long empId, Long storeId, LocalDateTime fromTime,
		LocalDateTime toTime, Pageable pageable);

Page<NewSaleEntity> findByCreatedByAndStoreId(Long empId, Long storeId, Pageable pageable);



}
