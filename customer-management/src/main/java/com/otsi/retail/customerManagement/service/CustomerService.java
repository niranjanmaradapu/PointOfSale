/**
 * service for Customer
 */
package com.otsi.retail.customerManagement.service;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.otsi.retail.customerManagement.model.ReturnSlip;
import com.otsi.retail.customerManagement.vo.CustomerDetailsVo;
import com.otsi.retail.customerManagement.vo.GenerateReturnSlipRequest;
import com.otsi.retail.customerManagement.vo.HsnDetailsVo;
import com.otsi.retail.customerManagement.vo.InvoiceRequestVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;
import com.otsi.retail.customerManagement.vo.NewSaleList;
import com.otsi.retail.customerManagement.vo.RetrnSlipDetailsVo;

/**
 * @author vasavi
 */
@Service
public interface CustomerService {


	List<ListOfReturnSlipsVo> getListOfReturnSlips(ListOfReturnSlipsVo vo);

	List<ReturnSlipVo> getInvoiceDetailsFromNewSale(InvoiceRequestVo searchVo) throws Exception;

	String createReturnSlip(GenerateReturnSlipRequest request) throws Exception;

	CustomerDetailsVo getCustomerFDetailsFromInvoice(String mobileNo) throws Exception;

	List<ListOfReturnSlipsVo> getAllListOfReturnSlips(Long storeId, Long domainId);
HsnDetailsVo getHsnDetails(double netAmt) throws JsonMappingException, JsonProcessingException;

	

	RetrnSlipDetailsVo ReturnSlipsDeatils(String RtNumber) throws JsonMappingException, JsonProcessingException, URISyntaxException;

	String updateReturnSlip(String rtNumber, GenerateReturnSlipRequest request);

	String deleteReturnSlips(int rsId);

	ReturnSlip getReturnSlipByrtNo(String rtNo) throws Exception;;
}
