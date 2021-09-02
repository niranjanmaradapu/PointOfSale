package com.otsi.retail.promoexchange.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.otsi.retail.promoexchange.Entity.BarcodeEntity;
import com.otsi.retail.promoexchange.Entity.DeliverySlipEntity;
import com.otsi.retail.promoexchange.Entity.PromoExchangeEntity;
import com.otsi.retail.promoexchange.vo.BarcodeVo;
import com.otsi.retail.promoexchange.vo.DeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfDeliverySlipVo;
import com.otsi.retail.promoexchange.vo.ListOfReturnSlipsVo;
import com.otsi.retail.promoexchange.vo.ListOfSaleBillsVo;
import com.otsi.retail.promoexchange.vo.PromoExchangeVo;

@Component
public class PromoExchangeMapper {

	public BarcodeEntity convertBarcodeVoToEntity(BarcodeVo vo) {

		BarcodeEntity entity = new BarcodeEntity();

		/* BeanUtils.copyProperties(vo, entity); */

		entity.setBarcode(vo.getBarcode());
		entity.setItemDesc(vo.getItemDesc());
		entity.setMrp(vo.getMrp());
		entity.setNetAmount(vo.getNetAmount());
		entity.setPromoDisc(vo.getPromoDisc());
		entity.setQty(vo.getQty());
		entity.setSalesMan(vo.getSalesMan());

		entity.setCreatedDate(LocalDateTime.now());

		return entity;
	}

	public BarcodeVo convertBarcodeEntityToVo(BarcodeEntity entity) {

		BarcodeVo vo = new BarcodeVo();

		/* BeanUtils.copyProperties(entity, vo); */

		vo.setBarcode(entity.getBarcode());
		vo.setItemDesc(entity.getItemDesc());
		vo.setMrp(entity.getMrp());
		vo.setNetAmount(entity.getNetAmount());
		vo.setPromoDisc(entity.getPromoDisc());
		vo.setQty(entity.getQty());
		vo.setSalesMan(entity.getSalesMan());

		return vo;
	}

	public DeliverySlipEntity convertDSVoToEntity(DeliverySlipVo vo) {

		return null;
	}

	public ListOfSaleBillsVo convertlistSalesEntityToVo(List<PromoExchangeEntity> saleDetails) {

		ListOfSaleBillsVo lsvo = new ListOfSaleBillsVo();
		List<PromoExchangeVo> sVoList = new ArrayList<>();

		saleDetails.stream().forEach(x -> {

			PromoExchangeVo nsvo = new PromoExchangeVo();

			// BeanUtils.copyProperties(x, nsvo);

			nsvo.setInvoiceNumber(x.getInvoiceNumber());
			nsvo.setBiller(x.getBiller());
			nsvo.setCreatedDate(x.getCreatedDate());
			nsvo.setTotalManualDisc(x.getTotalManualDisc());
			nsvo.setApprovedBy(x.getApprovedBy());
			nsvo.setReason(x.getReason());
			nsvo.setOfflineNumber(x.getOfflineNumber());
			
            
			sVoList.add(nsvo);

		});
		lsvo.setAmount(saleDetails.stream().mapToLong(i -> i.getNetPayableAmount()).sum());

		lsvo.setPromoExchangeVo(sVoList);
		return lsvo;
	}

	public ListOfDeliverySlipVo convertListDSToVo(List<DeliverySlipEntity> dsDetails) {

		ListOfDeliverySlipVo vo = new ListOfDeliverySlipVo();

		List<DeliverySlipVo> dsVoList = new ArrayList<>();

		dsDetails.stream().forEach(x -> {

			DeliverySlipVo dsvo = new DeliverySlipVo();

			BeanUtils.copyProperties(x, dsvo);

			dsVoList.add(dsvo);

		});

		vo.setToatalPromoDisc(dsDetails.stream().mapToLong(i -> i.getPromoDisc()).sum());
		vo.setTotalNetAmount(dsDetails.stream().mapToLong(i -> i.getNetAmount()).sum());
		vo.setTotalGrossAmount(dsDetails.stream().mapToLong(i -> i.getMrp()).sum());
		vo.setDeliverySlipVo(dsVoList);

		return vo;
	}
	
	/*
	 * public PromoExchangeVo EntityToVo(PromoExchangeEntity dto) { PromoExchangeVo
	 * vo=new PromoExchangeVo(); List<ListOfReturnSlipsVo> returnSlipVos=new
	 * ArrayList<>(); vo.setGrossAmount(dto.getGrossAmount());
	 * 
	 * vo.getReturnSlips().forEach(r->{ ListOfReturnSlipsVo returnslipVo=new
	 * ListOfReturnSlipsVo(); returnslipVo.setRecievedAmount(r.getAmount());
	 * returnSlipVos.add(returnslipVo); });
	 * 
	 * vo.setReturnSlips(returnSlipVos);
	 * 
	 * 
	 * vo.setBalanceAmount(dto.getNetPayableAmount() - dto.getRecievedAmount());
	 * 
	 * vo.setInvoiceNumber(dto.getInvoiceNumber()); return vo;
	 * 
	 * 
	 * }
	 */

}
