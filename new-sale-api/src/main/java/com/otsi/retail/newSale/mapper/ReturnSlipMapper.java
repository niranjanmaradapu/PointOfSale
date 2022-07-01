package com.otsi.retail.newSale.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;

@Component
public class ReturnSlipMapper {

	public Page<ListOfReturnSlipsVo> mapReturnEntityToVo(Page<ReturnSlip> dtos) {
		Page<ListOfReturnSlipsVo> lvo = null;
		
		Page<ListOfReturnSlipsVo> ListOfReturnSlipsVoPage = dtos.map(returnSlip -> {
			return mapToReturnSlipVO(returnSlip);
		});
		Long totalAmount = dtos.stream().mapToLong(a -> a.getAmount()).sum();
		ListOfReturnSlipsVoPage.stream().forEach(x -> x.setTotalAmount(totalAmount));
		return ListOfReturnSlipsVoPage;
		
	}

	private ListOfReturnSlipsVo mapToReturnSlipVO(ReturnSlip returnSlip) {
		
		
		
			ListOfReturnSlipsVo vo = new ListOfReturnSlipsVo();

			vo.setRtNumber(returnSlip.getRtNo());
			vo.setAmount(returnSlip.getAmount());
			vo.setBarcodes(returnSlip.getTaggedItems());
			vo.setRsId(returnSlip.getRsId());
			vo.setStoreId(returnSlip.getStoreId());
			vo.setCreatedBy(returnSlip.getCreatedBy());
			vo.setCreatedInfo(returnSlip.getCreatedDate());
			
			
		
		
		return vo;
	}

	/*public ReturnSlipRequestVo convertDtoToVo(PaymentAmountType returnslip) {
		ReturnSlipRequestVo returnSlipVo = new ReturnSlipRequestVo();

		returnSlipVo.setReturnedAmount(returnslip.getPaymentAmount());
		returnSlipVo.setBarcode(returnslip.getBarcode());
		returnSlipVo.setReturnReference(returnslip.getReturnReference());
		returnSlipVo.setIsreturned(returnslip.getIsreturned());
		returnSlipVo.setStoreId(returnslip.getStoreId());
		return returnSlipVo;
	}

	public PaymentAmountType convertVoToEntity(ReturnSlipRequestVo returnSlipRequestVo) {
		PaymentAmountType orderTransaction = new PaymentAmountType();
		orderTransaction.setIsreturned(returnSlipRequestVo.getIsreturned());
		// orderTransaction.setPaymentAmount(returnSlipRequestVo.getReturnedAmount());
		orderTransaction.setReturnReference(returnSlipRequestVo.getReturnReference());
		orderTransaction.setStoreId(returnSlipRequestVo.getStoreId());

		return orderTransaction;
	}

	public ReturnSlipRequestVo convertEntityToVo(PaymentAmountType paymentAmountType) {

		ReturnSlipRequestVo returnSlipRequestVo = new ReturnSlipRequestVo();
		returnSlipRequestVo.setIsreturned(paymentAmountType.getIsreturned());
		returnSlipRequestVo.setReturnedAmount(paymentAmountType.getPaymentAmount());
		returnSlipRequestVo.setReturnReference(paymentAmountType.getReturnReference());
		returnSlipRequestVo.setStoreId(paymentAmountType.getStoreId());

		return returnSlipRequestVo;
	}

	public List<ReturnSlipRequestVo> EntityToVo(List<PaymentAmountType> paymentAmountTypelist) {
		return paymentAmountTypelist.stream().map(paymentAmountType -> convertEntityToVo(paymentAmountType))
				.collect(Collectors.toList());
	}*/

	public ReturnSlipRequestVo convertReturnSlipEntityToVo(ReturnSlip returnSlip) {
		
		ReturnSlipRequestVo returnSlipRequestVo = new ReturnSlipRequestVo();
		returnSlipRequestVo.setRsId(returnSlip.getRsId());
		returnSlipRequestVo.setBarcodes(returnSlip.getTaggedItems());
		returnSlipRequestVo.setCreatedBy(returnSlip.getCreatedBy());
        returnSlipRequestVo.setMobileNumber(returnSlip.getMobileNumber());	
        returnSlipRequestVo.setReturnReference(returnSlip.getRtNo());
        returnSlipRequestVo.setStoreId(returnSlip.getStoreId());
        returnSlipRequestVo.setTotalAmount(returnSlip.getAmount());
        returnSlipRequestVo.setRtStatus(returnSlip.getRtStatus());
        returnSlipRequestVo.setReason(returnSlip.getReason());
        returnSlipRequestVo.setComments(returnSlip.getSettelmentInfo());
		return returnSlipRequestVo;
		
		// TODO Auto-generated method stub
		
	}

}
