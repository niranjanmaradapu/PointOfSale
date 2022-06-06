
package com.otsi.retail.newSale.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.newSale.Entity.ReturnSlip;

@Repository
public interface ReturnSlipRepo extends JpaRepository<ReturnSlip, Long> {

	ReturnSlip findByRtNo(String rtNumber);

	ReturnSlip findByMobileNumberAndStoreId(String mobileNumber, Long storeId);

	ReturnSlip findByRtNoAndStoreId(String returnReferenceNumber, Long storeId);

	ReturnSlip findByInvoiceNumberAndTaggedItems_BarCodeIn(String invoiceNumber, List<String> barcodesIn);



}
