package com.otsi.retail.customerManagement.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.otsi.retail.customerManagement.model.Barcode;
import com.otsi.retail.customerManagement.model.ReturnSlip;
import com.otsi.retail.customerManagement.model.TaggedItems;
import com.otsi.retail.customerManagement.utils.ReturnSlipStatus;
import com.otsi.retail.customerManagement.vo.BarcodeVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;

/**
 * @author lakshmi
 *
 */

@Component
public class ReturnSlipMapper {

	
	
	/*
	 * public List<ListOfReturnSlipsVo> mapEntityToVo(List<ReturnSlip> dtos) {
	 * return dtos.stream().map(dto ->
	 * mapEntityToVo(dto)).collect(Collectors.toList());
	 * 
	 * }
	 * 
	 * private ListOfReturnSlipsVo mapEntityToVo(ReturnSlip dto) {
	 * 
	 * ListOfReturnSlipsVo vo=new ListOfReturnSlipsVo();
	 * vo.setCreditNote(dto.getCrNo()); vo.setRtNumber(dto.getRtNo());
	 * vo.setAmount(dto.getAmount()); vo.setBarcodes(dto.getTaggedItems());
	 * vo.setRtReviewStatus(dto.getIsReviewed());
	 * vo.setCreatedInfo(dto.getCreatedDate()); vo.setCreatedBy(dto.getCreatedBy());
	 * return vo; }
	 */
	public List<ListOfReturnSlipsVo> mapReturnEntityToVo(List<ReturnSlip> dtos) {
		List<ListOfReturnSlipsVo> lvo = new ArrayList<>();

		dtos.stream().forEach(dto -> {
			ListOfReturnSlipsVo vo = new ListOfReturnSlipsVo();
			// ListOfReturnSlipsVo vo= new ListOfReturnSlipsVo();
			//vo.setCreditNote(dto.getCrNo());
			vo.setRtNumber(dto.getRtNo());
			vo.setAmount(dto.getAmount());
			vo.setBarcodes(dto.getTaggedItems());
			//vo.setRtReviewStatus(dto.getIsReviewed());
			vo.setCreatedInfo(dto.getCreatedDate());
			vo.setCreatedBy(dto.getCreatedBy());
			lvo.add(vo);
		});
		Long totalAmount = dtos.stream().mapToLong(a -> a.getAmount()).sum();
		lvo.stream().forEach(x -> x.setTotalAmount(totalAmount));

		return lvo;
	}
	

}
