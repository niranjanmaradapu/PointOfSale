package com.otsi.retail.newSale.repository;

import java.lang.annotation.Native;
import java.time.LocalDate;
import java.util.List;

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

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsId(LocalDate dateFrom, LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumber(LocalDate dateFrom, LocalDate dateTo,
			String dsNumber);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatus(LocalDate dateFrom, LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByStatus(DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetween(LocalDate dateFrom, LocalDate dateTo);

	List<DeliverySlipEntity> findByStatusAndCreationDate(DSStatus completed, LocalDate now);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long dsId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatusOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, DSStatus status);

	List<DeliverySlipEntity> findByDsNumberInOrderByCreationDateAsc(List<String> dsList);

	List<DeliverySlipEntity> findByStatusOrderByCreationDateAsc(DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetweenOrderByCreationDateAsc(LocalDate dateFrom, LocalDate dateTo);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdAndDsNumberAndStatusOrderByCreationDateAsc(
			LocalDate dateFrom, LocalDate dateTo, Long dsId, String dsNumber, DSStatus status);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumberOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String dsNumber);

	List<DeliverySlipEntity> findByDsNumberInAndOrderIsNull(List<String> dlsList);
	
	
	@Query(value="select dl_summary.user_id,sum(net_value) as net_value from (select dl_slip.store_id,dl_slip.user_id,dl_slip.creation_date,dl_slip.ds_number,dl_slip.ds_id,odr.order_id,odr.net_value from delivery_slip dl_slip join order_table odr on dl_slip.order_id =odr.order_id where dl_slip.store_id= :storeId and dl_slip.creation_date >= :fromDate and dl_slip.creation_date <= :toDate and dl_slip.user_id is not null and dl_slip.order_id is not null) dl_summary  group by dl_summary.user_id order by net_value desc limit 5",nativeQuery = true)
	List<Object[]> getByStoreIdAndCreationDateBetween(Long storeId,LocalDate fromDate,LocalDate toDate);
	
    List<DeliverySlipEntity> findByStoreId(Long storeId);
	List<DeliverySlipEntity> findByUserId(Long u);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdAndDsNumberAndStatusAndStoreIdOrderByCreationDateAsc(
			LocalDate dateFrom, LocalDate dateTo, Long dsId, String dsNumber, DSStatus status, Long storeId);

	DeliverySlipEntity findByDsIdAndStoreId(Long dsId, Long storeId);

	DeliverySlipEntity findByCreationDateBetweenAndDsIdAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long dsId,Long storeId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndDsNumberAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, String dsNumber,Long storeId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStatusAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, DSStatus status,Long storeId);

	List<DeliverySlipEntity> findByDsNumberInAndStoreIdOrderByCreationDateAsc(List<String> dsList, Long storeId);

	List<DeliverySlipEntity> findByStatusAndStoreIdOrderByCreationDateAsc(DSStatus status, Long storeId);

	List<DeliverySlipEntity> findByCreationDateBetweenAndStoreIdOrderByCreationDateAsc(LocalDate dateFrom,
			LocalDate dateTo, Long storeId);

	void save(List<DeliverySlipEntity> dsList);

	List<DeliverySlipEntity> findByStatusAndCreationDateAndStoreId(DSStatus pending, LocalDate now, Long storeId);
	
//	List<DeliverySlipEntity> getByYearAndMonthAndStoreId(int year, int month, Long storeId);
	


}
