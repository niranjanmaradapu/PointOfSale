package com.otsi.retail.newSale.repository;

import java.lang.annotation.Native;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.common.DSStatus;

@Repository
public interface DeliverySlipRepository extends JpaRepository<DeliverySlipEntity, Long> {

	List<DeliverySlipEntity> findByDsNumberIn(List<String> dlsList);

	DeliverySlipEntity findByDsNumber(String dsNumber);

	DeliverySlipEntity findByDsId(Long dsId);

	List<DeliverySlipEntity> findByCreatedDateBetweenAndDsId(LocalDate dateFrom, LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreatedDateBetweenAndDsNumber(LocalDate dateFrom, LocalDate dateTo, String dsNumber);

	List<DeliverySlipEntity> findByCreatedDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByStatus(DSStatus status);

	List<DeliverySlipEntity> findByCreatedDateBetween(LocalDate dateFrom, LocalDate dateTo);

	List<DeliverySlipEntity> findByStatusAndCreatedDate(DSStatus completed, LocalDate now);

	/*
	 * DeliverySlipEntity
	 * findByCreatedDateBetweenAndDsIdOrderByCreationDateAsc(LocalDate dateFrom,
	 * LocalDate dateTo, Long dsId);
	 * 
	 * List<DeliverySlipEntity>
	 * findByCreatedDateBetweenAndStatusOrderByCreationDateAsc(LocalDate dateFrom,
	 * LocalDate dateTo, DSStatus status);
	 * 
	 * List<DeliverySlipEntity> findByDsNumberInOrderByCreatedDateAsc(List<String>
	 * dsList);
	 * 
	 * List<DeliverySlipEntity> findByStatusOrderByCreatedDateAsc(DSStatus status);
	 * 
	 * List<DeliverySlipEntity>
	 * findByCreatedDateBetweenOrderByCreationDateAsc(LocalDate dateFrom, LocalDate
	 * dateTo);
	 * 
	 * DeliverySlipEntity
	 * findByCreatedDateBetweenAndDsIdAndDsNumberAndStatusOrderByCreationDateAsc(
	 * LocalDate dateFrom, LocalDate dateTo, Long dsId, String dsNumber, DSStatus
	 * status);
	 * 
	 * List<DeliverySlipEntity>
	 * findByCreatedDateBetweenAndDsNumberOrderByCreationDateAsc(LocalDate dateFrom,
	 * LocalDate dateTo, String dsNumber);
	 */

	List<DeliverySlipEntity> findByDsNumberInAndOrderIsNull(List<String> dlsList);

	@Query(value = "select dl_summary.user_id,sum(net_value) as net_value from (select dl_slip.store_id,dl_slip.user_id,dl_slip.creation_date,dl_slip.ds_number,dl_slip.ds_id,odr.order_id,odr.net_value from delivery_slip dl_slip join order_table odr on dl_slip.order_id =odr.order_id where dl_slip.store_id= :storeId and dl_slip.creation_date >= :fromDate and dl_slip.creation_date <= :toDate and dl_slip.user_id is not null and dl_slip.order_id is not null) dl_summary  group by dl_summary.user_id order by net_value desc limit 5", nativeQuery = true)
	List<Object[]> getByStoreIdAndCreatedDateBetween(Long storeId, LocalDate fromDate, LocalDate toDate);

	List<DeliverySlipEntity> findByStoreId(Long storeId);

	List<DeliverySlipEntity> findByUserId(Long u);

	Page<DeliverySlipEntity> findByCreatedDateBetweenAndDsNumberAndStoreIdOrderByCreatedDateDesc(
			LocalDateTime createdDatefrom, LocalDateTime createdDateTo, String dsNumber, Long storeId,
			Pageable pageable);

	Page<DeliverySlipEntity> findByCreatedDateBetweenAndStatusAndStoreIdOrderByCreatedDateDesc(
			LocalDateTime createdDatefrom, LocalDateTime createdDateTo, DSStatus status, Long storeId,
			Pageable pageable);

	Page<DeliverySlipEntity> findByDsNumberInAndStoreIdOrderByCreatedDateDesc(List<String> dsList, Long storeId,
			Pageable pageable);

	Page<DeliverySlipEntity> findByStatusAndStoreIdOrderByCreatedDateDesc(DSStatus status, Long storeId,
			Pageable pageable);

	Page<DeliverySlipEntity> findByCreatedDateBetweenAndStoreIdOrderByCreatedDateDesc(LocalDateTime createdDatefrom,
			LocalDateTime createdDateTo, Long storeId, Pageable pageable);

	void save(List<DeliverySlipEntity> dsList);

	List<DeliverySlipEntity> findByStatusAndCreatedDateAndStoreId(DSStatus pending, LocalDateTime localDateTime,
			Long storeId);

	Page<DeliverySlipEntity> findByCreatedDateBetweenAndDsIdInAndDsNumberAndStatusAndStoreIdOrderByCreatedDateDesc(
			LocalDateTime createdDatefrom, LocalDateTime createdDateTo, List<Long> dsIds, String dsNumber,
			DSStatus status, Long storeId, Pageable pageable);

	Page<DeliverySlipEntity> findByDsIdInAndStoreIdOrderByCreatedDateDesc(List<Long> dsIds, Long storeId,
			Pageable pageable);

	Page<DeliverySlipEntity> findByCreatedDateBetweenAndDsIdInAndStoreIdOrderByCreatedDateDesc(
			LocalDateTime createdDatefrom, LocalDateTime createdDateTo, List<Long> dsIds, Long storeId,
			Pageable pageable);

	List<DeliverySlipEntity> findByStatusAndCreatedDateBetweenAndStoreId(DSStatus pending,
			LocalDateTime createdDatefrom, LocalDateTime createdDateTo, Long storeId);

	Page<DeliverySlipEntity> findByDsIdInAndDsNumberInAndStoreIdOrderByCreatedDateDesc(List<Long> dsIds,
			List<String> dsList, Long storeId, Pageable pageable);

}
