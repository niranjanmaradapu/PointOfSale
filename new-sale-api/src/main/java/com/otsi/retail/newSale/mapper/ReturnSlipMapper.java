package com.otsi.retail.newSale.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.PaymentAmountType;
import com.otsi.retail.newSale.Entity.ReturnSlip;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.ReturnSlipRequestVo;

@Component
public class ReturnSlipMapper {

	public List<ListOfReturnSlipsVo> mapReturnEntityToVo(List<ReturnSlip> dtos) {
		List<ListOfReturnSlipsVo> lvo = new ArrayList<>();

		dtos.stream().forEach(dto -> {
			ListOfReturnSlipsVo vo = new ListOfReturnSlipsVo();

			vo.setRtNumber(dto.getRtNo());
			vo.setAmount(dto.getAmount());
			vo.setBarcodes(dto.getTaggedItems());
			vo.setRsId(dto.getRsId());
			vo.setStoreId(dto.getStoreId());
			// vo.setCreatedInfo(dto.getCreatedDate());
			vo.setCreatedBy(dto.getCreatedBy());
			lvo.add(vo);
		});
		Long totalAmount = dtos.stream().mapToLong(a -> a.getAmount()).sum();
		lvo.stream().forEach(x -> x.setTotalAmount(totalAmount));

		return lvo;
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
