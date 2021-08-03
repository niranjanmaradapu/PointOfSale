package com.newsaleapi.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.newsaleapi.Entity.BarcodeEntity;
import com.newsaleapi.Entity.DeliverySlipEntity;
import com.newsaleapi.vo.BarcodeVo;
import com.newsaleapi.vo.DeliverySlipVo;

@Component
public class DeliverySlipMapper {

	public DeliverySlipEntity convertDsVoToEntity(DeliverySlipVo vo) {

		DeliverySlipEntity entity = new DeliverySlipEntity();
		List<BarcodeVo> barVo = vo.getBarcode();

		entity.setMrp(barVo.stream().mapToLong(i -> i.getMrp()).sum());
		entity.setPromoDisc(barVo.stream().mapToLong(i -> i.getPromoDisc()).sum());
		entity.setNetAmount(barVo.stream().mapToLong(i -> i.getNetAmount()).sum());
		entity.setSalesMan(vo.getSalesMan());
		entity.setQty(vo.getQty());
		entity.setType(vo.getType());
		entity.setCreatedDate(LocalDate.now());
		entity.setLastModified(LocalDateTime.now());
		entity.setStatus("Pending");

		return entity;
	}

	public DeliverySlipVo convertDsEntityToVo(DeliverySlipEntity ent) {

		DeliverySlipVo vo = new DeliverySlipVo();

		vo.setDsNumber(ent.getDsNumber());
		vo.setMrp(ent.getMrp());
		vo.setNetAmount(ent.getNetAmount());
		vo.setPromoDisc(ent.getPromoDisc());
		vo.setQty(ent.getQty());
		vo.setType(ent.getType());

		List<BarcodeEntity> barEnt = ent.getBarcodes();

		List<BarcodeVo> listBarVo = new ArrayList<>();

		barEnt.stream().forEach(x -> {
			BarcodeVo barvo = new BarcodeVo();

			barvo.setSalesMan(x.getSalesMan());
			barvo.setQty(x.getQty());
			barvo.setPromoDisc(x.getPromoDisc());
			barvo.setNetAmount(x.getNetAmount());
			barvo.setMrp(x.getMrp());
			barvo.setItemDesc(x.getItemDesc());

			listBarVo.add(barvo);
		});
		vo.setBarcode(listBarVo);

		return vo;
	}

}
