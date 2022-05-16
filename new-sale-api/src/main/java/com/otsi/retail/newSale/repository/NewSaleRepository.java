package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.CustomerDetailsEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.common.OrderStatus;
import com.otsi.retail.newSale.vo.ReportVo;

@Repository
public interface NewSaleRepository extends JpaRepository<NewSaleEntity, Long> {

	List<NewSaleEntity> findByCreationDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, OrderStatus orderStatus);

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

	List<NewSaleEntity> findByUserIdInAndCreationDateBetween(List<Long> userIds, LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByOrderNumberAndCreationDateBetween(String invoiceNumber, LocalDate dateFrom,
			LocalDate dateTo);

	List<NewSaleEntity> findByCreatedByAndCreationDateBetween(String empId, LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByStatus(OrderStatus orderStatus);

	List<NewSaleEntity> findByStoreId(Long n);

	List<NewSaleEntity> findByStoreIdAndDomainId(Long storeId, Long domainId);

	List<NewSaleEntity> findByCreationDateBetweenAndStatusAndStoreIdAndDomainId(LocalDate dateFrom, LocalDate dateTo,
			OrderStatus billStatus,Long storeId, Long domainId);

	List<NewSaleEntity> findByUserIdInAndStoreIdAndDomainIdAndCreationDateBetween(List<Long> userIds,
			 Long storeId, Long domainId,LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByOrderNumberAndStoreIdAndDomainIdAndCreationDateBetween(String invoiceNumber,Long storeId,
			 Long domainId,LocalDate dateFrom, LocalDate dateTo);

	List<NewSaleEntity> findByCreatedByAndStoreIdAndDomainIdAndCreationDateBetween(String empId, Long storeId, Long domainId,LocalDate dateFrom,
			LocalDate dateTo);

	List<NewSaleEntity> findByCreationDateBetweenAndStoreIdAndDomainId(LocalDate dateFrom, LocalDate dateTo,
			Long storeId, Long domainId);

	List<NewSaleEntity> findByStatusAndStoreIdAndDomainId(OrderStatus billStatus, Long storeId, Long domainId);

	List<NewSaleEntity> findByUserIdInAndStoreIdAndDomainId(List<Long> userIds, Long storeId, Long domainId);

	List<NewSaleEntity> findByOrderNumberAndStoreIdAndDomainId(String invoiceNumber, Long storeId, Long domainId);

	List<NewSaleEntity> findByCreatedByAndStoreIdAndDomainId(String empId, Long storeId, Long domainId);

	List<NewSaleEntity> findByCreationDateBetweenAndDomainId(LocalDate dateFrom, LocalDate dateTo, Long domainId);

	List<NewSaleEntity> findByCreationDateBetweenAndDomainIdAndStoreId(LocalDate dateFrom, LocalDate dateTo,
			Long domainId, Long storeId);

	List<NewSaleEntity> findByDomainId(Long domainId);
@Query(value="select sum(sale_sum.net_value) As net_value,EXTRACT(MONTH FROM sale_sum.creation_date)  AS Month from order_table sale_sum where sale_sum.store_id=:storeId and sale_sum.domain_id=:domainId and sale_sum.creation_date>=:dateFrom and sale_sum.creation_date <= :dateto group by Month",nativeQuery=true)
	List<Object[]> getByStoreIdAndDomainIdAndCreationDateBetween(Long storeId, Long domainId, LocalDate dateFrom,LocalDate dateto);

List<NewSaleEntity> findByStoreIdAndDomainIdAndCreationDateBetween(Long storeId, Long domainId, LocalDate dateFrom,
		LocalDate dateTo);

List<NewSaleEntity> findByStoreIdAndDomainIdAndCreationDate(Long storeId, Long domainId, LocalDate date);
@Query(value="select store_id,sum(net_value) As net_value from order_table otable where domain_id=:domainId and creation_date >=:dateFrom and creation_date <=:dateTo group by store_id order by net_value desc limit 5",nativeQuery=true)
List<Object[]> findByDomainIdAndCreationDateBetween(Long domainId, LocalDate dateFrom, LocalDate dateTo);


}
