package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;

@Component
public interface ReturnslipService {

	/*List<ListOfReturnSlipsVo> getListOfReturnSlips(ListOfReturnSlipsVo vo);

	String deleteReturnSlips(int rsId);

	String createReturnSlip(GenerateReturnSlipRequest request) throws JsonProcessingException;

	String updateReturnSlip(String rtNumber, GenerateReturnSlipRequest request) throws Exception;*/

	ReturnSlipRequestVo getReturnSlip(String mobileNumber, Long storeId);

	List<ReturnSlipRequestVo> createReturnSlip(ReturnSlipRequestVo returnSlipRequestVo);

}
