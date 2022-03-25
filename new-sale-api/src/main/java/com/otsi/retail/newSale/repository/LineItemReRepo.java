package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.LineItemsReEntity;

@Repository
public interface LineItemReRepo extends JpaRepository<LineItemsReEntity, Long>{

	List<LineItemsReEntity> findByLineItemReIdIn(List<Long> lineItemIds);
	
	LineItemsReEntity findByLineItemReId(Long lineItemId);
	
	List<LineItemsReEntity> findByBarCode(String barCode);

	List<LineItemsReEntity> findByLineItemReIdInAndOrderIdIsNull(List<Long> lineItemIds);

	//List<LineItemsReEntity> findByBarcodeIn(List<String> barCode);

	List<LineItemsReEntity> findByBarCodeIn(List<String> barCode);

	List<LineItemsReEntity> findByUserId(Long u);

	List<LineItemsReEntity> findByStoreId(Long storeId);
	/*@Query(value="select b.user_id,sum(b.net_val) as net_v from(select t1.store_id,t1.user_id,t1.bar_code,t1.line_item_re_id,t2.order_id,t2.net_value as net_val from lineItems_re t1 join order_table t2 on t1.order_id =t2.order_id where t1.store_id= :storeId) b group by b.user_id order by net_v desc limit 5",nativeQuery=true) 
	List<Object[]> getByStoreId(Long storeId);*/


	List<LineItemsReEntity> findBySection(Long b);

	List<LineItemsReEntity> findBySectionAndStoreId(Long b, Long storeId);
@Query(value="select lineItem.user_id,sum(net_value) as net_value from (select line_re.store_id,line_re.user_id,line_re.creation_date,line_re.bar_code,line_re.line_item_re_id,odr.order_id,odr.net_value from lineitems_re line_re join order_table odr on line_re.order_id =odr.order_id where line_re.store_id= :storeId and line_re.creation_date >=:fromDate and line_re.creation_date <= :toDate) lineItem  group by lineItem.user_id order by net_value desc limit 5",nativeQuery=true)
	List<Object[]> getByStoreIdAndCreationDateBetween(Long storeId, LocalDate fromDate, LocalDate toDate);
	@Query(value="select lineItem.section,sum(net_value) as net_value from (select line_re.store_id,line_re.section,line_re.creation_date,line_re.line_item_re_id,odr.order_id,odr.net_value from lineitems_re line_re join order_table odr on line_re.order_id =odr.order_id where line_re.store_id= 2879 and line_re.creation_date >='2022-01-11' and line_re.creation_date <= '2022-03-18')lineItem group by lineItem.section",nativeQuery=true)
List<Object[]> findByStoreIdAndCreation_dateBetween(Long storeId, LocalDate fromDate, LocalDate toDate);
	
}
