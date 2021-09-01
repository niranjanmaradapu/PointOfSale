package com.otsi.kalamandhir.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


import com.otsi.kalamandhir.model.Barcode;

import com.otsi.kalamandhir.model.ReturnSlip;
import com.otsi.kalamandhir.model.TaggedItems;
import com.otsi.kalamandhir.utils.ReturnSlipStatus;
import com.otsi.kalamandhir.vo.BarcodeVo;
import com.otsi.kalamandhir.vo.ListOfReturnSlipsVo;

/**
 * @author lakshmi
 *
 */

@Component
public class ReturnSlipMapper {

	
	
	public List<ListOfReturnSlipsVo> mapEntityToVo(List<ReturnSlip> dtos) {
		return dtos.stream().map(dto -> mapEntityToVo(dto)).collect(Collectors.toList());

	}

	private ListOfReturnSlipsVo mapEntityToVo(ReturnSlip dto) {
		
		ListOfReturnSlipsVo vo=new ListOfReturnSlipsVo();
        vo.setCreditNote(dto.getCrNo());
        vo.setRtNumber(dto.getRtNo());
        vo.setAmount(dto.getAmount());
        vo.setBarcodes(dto.getTaggedItems());
        vo.setRtReviewStatus(dto.getIsReviewed());
        vo.setCreatedInfo(dto.getCreatedDate());
        vo.setCreatedBy(dto.getCreatedBy());
		return vo;
	}

}
