package com.otsi.retail.newSale.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.PaymentAmountTypeVo;

@Component
public class NewSaleMapper {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private PaymentAmountTypeMapper paymentAmountTypeMapper;

	@Autowired
	private DeliverySlipMapper deliverySlipMapper;

	public BarcodeEntity convertBarcodeVoToEntity(BarcodeVo vo) {

		BarcodeEntity entity = new BarcodeEntity();

		BeanUtils.copyProperties(vo, entity);

		/*
		 * entity.setBarcode(vo.getBarcode()); entity.setItemDesc(vo.getItemDesc());
		 * entity.setMrp(vo.getMrp()); entity.setNetAmount(vo.getNetAmount());
		 * entity.setPromoDisc(vo.getPromoDisc()); entity.setQty(vo.getQty());
		 * entity.setSalesMan(vo.getSalesMan());
		 */
		entity.setCreatedDate(LocalDateTime.now());

		return entity;
	}

	public BarcodeVo convertBarcodeEntityToVo(BarcodeEntity entity) {

		BarcodeVo vo = new BarcodeVo();

		BeanUtils.copyProperties(entity, vo);

		/*
		 * vo.setBarcode(entity.getBarcode()); vo.setItemDesc(entity.getItemDesc());
		 * vo.setMrp(entity.getMrp()); vo.setNetAmount(entity.getNetAmount());
		 * vo.setPromoDisc(entity.getPromoDisc()); vo.setQty(entity.getQty());
		 * vo.setSalesMan(entity.getSalesMan());
		 */

		return vo;
	}

	public DeliverySlipEntity convertDSVoToEntity(DeliverySlipVo vo) {

		return null;
	}

	public ListOfSaleBillsVo convertlistSalesEntityToVo(List<NewSaleEntity> saleDetails) {

		ListOfSaleBillsVo lsvo = new ListOfSaleBillsVo();
		List<NewSaleVo> sVoList = new ArrayList<>();

		saleDetails.stream().forEach(x -> {

			NewSaleVo nsvo = new NewSaleVo();

			// BeanUtils.copyProperties(x, nsvo);

			nsvo.setInvoiceNumber(x.getBillNumber());
			nsvo.setBiller(x.getBiller());
			nsvo.setCreatedDate(x.getCreatedDate());
			nsvo.setTotalManualDisc(x.getTotalManualDisc());
			nsvo.setApprovedBy(x.getApprovedBy());
			nsvo.setReason(x.getReason());
			nsvo.setOfflineNumber(x.getOfflineNumber());

			sVoList.add(nsvo);

		});
		lsvo.setAmount(saleDetails.stream().mapToLong(i -> i.getNetPayableAmount()).sum());

		lsvo.setNewSaleVo(sVoList);
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

	public NewSaleVo entityToVo(NewSaleEntity dto) {
		NewSaleVo vo = new NewSaleVo();
		vo.setNewsaleId(dto.getNewsaleId());
		vo.setInvoiceNumber(dto.getBillNumber());
		vo.setNetPayableAmount(dto.getNetPayableAmount());
		vo.setRecievedAmount(dto.getRecievedAmount());
		vo.setCustomerDetails(customerMapper.convertEntityToVo(dto.getCustomerDetails()));
		return vo;
	}

	/*
	 * to convert list dto's to vo's
	 */

	public List<NewSaleVo> entityToVo(List<NewSaleEntity> dtos) {
		return dtos.stream().map(dto -> entityToVo(dto)).collect(Collectors.toList());

	}

	public List<NewSaleResponseVo> entityToResVo(List<NewSaleEntity> dtos) {
		return dtos.stream().map(dto -> entityToResVo(dto)).collect(Collectors.toList());

	}

	public NewSaleResponseVo entityToResVo(NewSaleEntity dto) {
		NewSaleResponseVo vo = new NewSaleResponseVo();
		List<PaymentAmountTypeVo> payVos = new ArrayList<>();
		vo.setCustomerId(dto.getCustomerDetails().getCustomerId());
		vo.setCustomerName(dto.getCustomerDetails().getName());
		vo.setMobileNumber(dto.getCustomerDetails().getMobileNumber());
		vo.setNewsaleId(dto.getNewsaleId());
		dto.getPaymentType().forEach(p -> {
			PaymentAmountTypeVo payVo = new PaymentAmountTypeVo();
			payVo.setId(p.getId());
			payVo.setPaymentAmount(p.getPaymentAmount());
			payVo.setPaymentType(p.getPaymentType());
			payVos.add(payVo);
		});
		vo.setPaymentAmountTypeId(payVos);
		vo.setAmount(dto.getNetPayableAmount() - dto.getRecievedAmount());
		vo.setInvoiceNumber(dto.getInvoiceNumber());
		return vo;
	}

	public NewSaleVo convertNewSaleDtoToVo(NewSaleEntity dto) {
		NewSaleVo vo = new NewSaleVo();
		vo.setApprovedBy(dto.getApprovedBy());
		vo.setBiller(dto.getBiller());
		vo.setCreatedDate(dto.getCreatedDate());
		if (dto.getDlSlip() != null) {
			vo.setDlSlip(deliverySlipMapper.convertDsEntityListToVoList(dto.getDlSlip()));
		}
		vo.setGrossAmount(dto.getGrossAmount());
		vo.setInvoiceNumber(dto.getBillNumber());
		vo.setNatureOfSale(dto.getNatureOfSale());
		vo.setNetPayableAmount(dto.getNetPayableAmount());
		vo.setOfflineNumber(dto.getOfflineNumber());
		if (dto.getPaymentType() != null) {
			vo.setPaymentAmountType(paymentAmountTypeMapper.EntityToVo(dto.getPaymentType()));

		}
		vo.setRoundOff(dto.getRoundOff());
		vo.setTaxAmount(dto.getTaxAmount());
		vo.setTotalManualDisc(dto.getTotalManualDisc());
		vo.setTotalPromoDisc(dto.getTotalPromoDisc());
		if (dto.getCustomerDetails() != null) {
			vo.setCustomerDetails(customerMapper.convertEntityToVo(dto.getCustomerDetails()));

		}

		return vo;
		// TODO Auto-generated method stub

	}

	// Method for convert List of Bar code entities to List of bar code Vo's
	public List<BarcodeVo> convertBarcodeListFromEntityToVo(List<BarcodeEntity> listOfBarcodes) {

		List<BarcodeVo> barcodeList = new ArrayList<>();

		listOfBarcodes.parallelStream().forEach(x -> {

			BarcodeVo barcodeVo = convertBarcodeEntityToVo(x);
			barcodeList.add(barcodeVo);

		});
		return barcodeList;
	}
	
	public List<BarcodeVo> convertBarcodesEntityToVo(List<BarcodeEntity> barcodeDetails) {
		return barcodeDetails.stream().map(dto -> barentityToVo(dto)).collect(Collectors.toList());
	
		
	}

	private BarcodeVo barentityToVo(BarcodeEntity dto) {
		
		BarcodeVo vo = new BarcodeVo();
		BeanUtils.copyProperties(dto, vo);
		
		return vo;
	}


}
