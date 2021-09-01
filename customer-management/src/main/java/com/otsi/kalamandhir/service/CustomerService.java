/**
 * service for Customer
 */
package com.otsi.kalamandhir.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otsi.kalamandhir.vo.CustomerDetailsVo;
import com.otsi.kalamandhir.vo.GenerateReturnSlipRequest;
import com.otsi.kalamandhir.vo.InvoiceRequestVo;
import com.otsi.kalamandhir.vo.ListOfReturnSlipsVo;
import com.otsi.kalamandhir.vo.NewSaleList;

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
