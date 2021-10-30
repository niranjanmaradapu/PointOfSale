package com.otsi.retail.newSale.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.common.DSStatus;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.LineItemVo;

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
		entity.setCreationDate(LocalDateTime.now());
		entity.setLastModified(LocalDateTime.now());
		entity.setStatus(DSStatus.Pending);

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
		vo.setSalesMan(ent.getSalesMan());

		List<BarcodeEntity> barEnt = ent.getBarcodes();

		List<BarcodeVo> listBarVo = new ArrayList<>();

		barEnt.stream().forEach(x -> {
			BarcodeVo barvo = new BarcodeVo();
			barvo.setBarcode(x.getBarcode());
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

	public List<DeliverySlipVo> convertDsEntityListToVoList(List<DeliverySlipEntity> dlSlip) {
		List<DeliverySlipVo> dsSlipList = new ArrayList<>();
		return dlSlip.stream().map(dto -> convertDsEntityToVo(dto)).collect(Collectors.toList());
	}

	public DeliverySlipVo convertDsEntityToDsVo(DeliverySlipEntity dsEntity) {

		DeliverySlipVo vo = new DeliverySlipVo();

		vo.setDsNumber(dsEntity.getDsNumber());
		vo.setSalesMan(dsEntity.getSalesMan());
		vo.setCreatedDate(dsEntity.getCreationDate().toLocalDate());
		vo.setLastModified(dsEntity.getLastModified());

		List<LineItemVo> lineItems = new ArrayList<>();

		dsEntity.getLineItems().stream().forEach(x -> {

			LineItemVo lineVo = new LineItemVo();
			// BeanUtils.copyProperties(x, lineVo);

			lineVo.setBarCode(x.getBarCode());
			lineVo.setQuantity(x.getQuantity());
			lineVo.setNetValue(x.getNetValue());
			lineVo.setLineItemId(x.getLineItemId());
			lineVo.setItemPrice(x.getItemPrice());
			lineVo.setGrossValue(x.getGrossValue());
			lineVo.setDiscount(x.getDiscount());
			lineVo.setCreationDate(x.getCreationDate());
			lineVo.setLastModified(x.getLastModified());

			lineItems.add(lineVo);
		});
		vo.setLineItems(lineItems);
		return vo;
	}

}
