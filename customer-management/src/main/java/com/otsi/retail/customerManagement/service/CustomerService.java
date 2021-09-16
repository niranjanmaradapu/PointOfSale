/**
 * service for Customer
 */
package com.otsi.retail.customerManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otsi.retail.customerManagement.vo.CustomerDetailsVo;
import com.otsi.retail.customerManagement.vo.GenerateReturnSlipRequest;
import com.otsi.retail.customerManagement.vo.InvoiceRequestVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;
import com.otsi.retail.customerManagement.vo.NewSaleList;

/**
 * @author vasavi
 */
@Service
public interface CustomerService {


	List<ListOfReturnSlipsVo> getListOfReturnSlips(ListOfReturnSlipsVo vo);

	NewSaleList getInvoiceDetailsFromNewSale(InvoiceRequestVo searchVo) throws Exception;

	String createReturnSlip(GenerateReturnSlipRequest request) throws Exception;

	CustomerDetailsVo getCustomerFDetailsFromInvoice(String mobileNo) throws Exception;

	List<ListOfReturnSlipsVo> getAllListOfReturnSlips();
}
