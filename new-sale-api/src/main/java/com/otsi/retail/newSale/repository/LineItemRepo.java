package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.LineItemsEntity;

@Repository
public interface LineItemRepo extends JpaRepository<LineItemsEntity, Long> {

	List<LineItemsEntity> findByLineItemIdIn(List<Long> lineItems);

	LineItemsEntity findByLineItemId(Long lineItemId);
	
	List<LineItemsEntity> findByLineItemIdInAndDsEntityIsNull(List<Long> lineItems);

	List<LineItemsEntity> findByBarCode(String barCode);

	//List<LineItemsEntity> findByBarcodeIn(List<String> barCode);

	List<LineItemsEntity> findByBarCodeIn(List<String> barCode);

	List<LineItemsEntity> findByBarCodeAndStoreId(String barcode, Long storeId);

	List<LineItemsEntity> findByStoreId(Long storeId);

	List<LineItemsEntity> findBySection(Long b);

	List<LineItemsEntity> findBySectionAndStoreId(Long b, Long storeId);
	
	@Query(value="select lineItem.section,sum(net_value) as net_value from (select line_re.store_id,line_re.section,line_re.creation_date,line_re.line_item_id,odr.order_id,odr.net_value from line_items line_re join delivery_slip dsSlip on line_re.ds_id = dsSlip.ds_id join order_table odr on dsSlip.order_id =odr.order_id where line_re.store_id= :storeId and line_re.creation_date >= :fromDate and line_re.creation_date <= :toDate and line_re.section is not null)lineItem group by lineItem.section ",nativeQuery=true)
	List<Object[]> findByStoreIdAndCreation_dateBetween(Long storeId, LocalDate fromDate, LocalDate toDate);
}
