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
import com.otsi.retail.newSale.Entity.LineItemsEntity;
import com.otsi.retail.newSale.Entity.LineItemsReEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.common.DomainData;
import com.otsi.retail.newSale.service.NewSaleServiceImpl;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.HsnDetailsVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.PaymentAmountTypeVo;
import com.otsi.retail.newSale.vo.SaleReportVo;

@Component
public class NewSaleMapper {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private PaymentAmountTypeMapper paymentAmountTypeMapper;

	@Autowired
	private DeliverySlipMapper deliverySlipMapper;
	private NewSaleVo nsvo;

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
			List<LineItemVo> listBarVo = new ArrayList<>();
			if (x.getDomainId() == DomainData.RE.getId()) {
				if (x.getLineItemsRe() != null) {
					x.getLineItemsRe().stream().forEach(a -> {

						LineItemVo barvo = new LineItemVo();

						barvo.setCreationDate(a.getCreationDate());
						barvo.setBarCode(a.getBarCode());
						barvo.setSection(a.getSection());
						barvo.setItemPrice(a.getItemPrice());
						barvo.setGrossValue(a.getGrossValue());
						barvo.setNetValue(a.getNetValue());
						barvo.setQuantity(a.getQuantity());
						barvo.setDiscount(a.getDiscount());
                        listBarVo.add(barvo);
						nsvo.setLineItemsReVo(listBarVo);
					});
				}
			} else if (x.getDomainId() != DomainData.RE.getId()) {

				if (x.getDlSlip() != null) {
					x.getDlSlip().stream().forEach(a -> {
						a.getLineItems().stream().forEach(b -> {

							LineItemVo linevo = new LineItemVo();

							linevo.setCreationDate(b.getCreationDate());
							linevo.setBarCode(b.getBarCode());
							linevo.setItemPrice(b.getItemPrice());
							linevo.setGrossValue(b.getGrossValue());
							linevo.setNetValue(b.getNetValue());
							linevo.setQuantity(b.getQuantity());
							linevo.setDiscount(b.getDiscount());
							linevo.setSection(b.getSection());

							listBarVo.add(linevo);
							nsvo.setLineItemsReVo(listBarVo);
						});
					});
				}

			}
			nsvo.setInvoiceNumber(x.getOrderNumber());
			nsvo.setBiller(x.getCreatedBy());
			nsvo.setCreatedDate(x.getCreationDate());
			nsvo.setTotalManualDisc(x.getManualDisc());
			nsvo.setApprovedBy(x.getCreatedBy());
			nsvo.setReason(x.getDiscType());
			nsvo.setOfflineNumber(x.getOfflineNumber());
			nsvo.setNetPayableAmount(x.getNetValue());
			nsvo.setUserId(x.getUserId());
			nsvo.setEmpId(x.getCreatedBy());
			nsvo.setStatus(x.getStatus());
			nsvo.setNewsaleId(x.getOrderId());

			sVoList.add(nsvo);

			lsvo.setNewSaleVo(sVoList);

		});

		lsvo.setTotalAmount(saleDetails.stream().mapToLong(i -> i.getNetValue()).sum());
		lsvo.setTotalDiscount(saleDetails.stream().mapToLong(d -> d.getManualDisc()).sum());

		return lsvo;

	}

	public SaleReportVo convertlistSaleReportEntityToVo(List<NewSaleEntity> saleDetails) {

		SaleReportVo srvo = new SaleReportVo();

		srvo.setBillValue(saleDetails.stream().mapToLong(b -> b.getNetValue()).sum());
		srvo.setTotalMrp(saleDetails.stream().mapToLong(m -> m.getGrossValue()).sum());
		srvo.setTotalDiscount(saleDetails.stream().mapToLong(d -> d.getPromoDisc()).sum());
		return srvo;

	}
	
	

	public ListOfDeliverySlipVo convertListDSToVo(List<DeliverySlipEntity> dsDetails) {

		ListOfDeliverySlipVo vo = new ListOfDeliverySlipVo();

		List<DeliverySlipVo> dsVoList = new ArrayList<>();
		List<LineItemsEntity> barEnt = new ArrayList<>();

		dsDetails.stream().forEach(x -> {

			List<LineItemVo> listBarVo = new ArrayList<>();

			x.getLineItems().stream().forEach(b -> {

				LineItemVo barvo = new LineItemVo();

				BeanUtils.copyProperties(b, barvo);

				listBarVo.add(barvo);
			});

			DeliverySlipVo dsvo = new DeliverySlipVo();

			BeanUtils.copyProperties(x, dsvo);
			dsvo.setLineItems(listBarVo);

			dsVoList.add(dsvo);

		});

		//vo.setToatalPromoDisc(dsDetails.stream().mapToLong(i -> i.getPromoDisc()).sum());
		//vo.setTotalNetAmount(dsDetails.stream().mapToLong(i -> i.getNetAmount()).sum());
		//vo.setTotalGrossAmount(dsDetails.stream().mapToLong(i -> i.getMrp()).sum());
		vo.setDeliverySlipVo(dsVoList);

		dsDetails.stream().forEach(a -> {

			vo.setBartoatalPromoDisc(a.getLineItems().stream().mapToLong(i -> i.getDiscount()).sum());
			vo.setBartotalNetAmount(a.getLineItems().stream().mapToLong(i -> i.getNetValue()).sum());
			vo.setBartotalGrossAmount(a.getLineItems().stream().mapToLong(i -> i.getGrossValue()).sum());
			vo.setBarTotalQty(a.getLineItems().stream().mapToInt(q -> q.getQuantity()).sum());

		});

		return vo;
	}

	public NewSaleVo entityToVo(NewSaleEntity dto) {
		NewSaleVo vo = new NewSaleVo();
		vo.setNewsaleId(dto.getOrderId());
		vo.setInvoiceNumber(dto.getOrderNumber());
		vo.setNetPayableAmount(dto.getGrossValue());
		vo.setRecievedAmount(dto.getNetValue());
		// vo.setCustomerDetails(customerMapper.convertEntityToVo(dto.getCustomerDetails()));
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
		vo.setCustomerId(dto.getUserId());
		// vo.setCustomerName(dto.getCustomerDetails().getName());
		// vo.setMobileNumber(dto.getCustomerDetails().getMobileNumber());
		vo.setNewsaleId(dto.getOrderId());
//		dto.getPaymentType().forEach(p -> {
//			PaymentAmountTypeVo payVo = new PaymentAmountTypeVo();
//			payVo.setId(p.getId());
//			payVo.setPaymentAmount(p.getPaymentAmount());
//			payVo.setPaymentType(p.getPaymentType());
//			payVos.add(payVo);
//		});
		vo.setPaymentAmountTypeId(payVos);
		vo.setAmount(dto.getNetValue() - dto.getNetValue());
		vo.setInvoiceNumber(dto.getOrderNumber());
		return vo;
	}

	public NewSaleVo convertNewSaleDtoToVo(NewSaleEntity dto) {
		NewSaleVo vo = new NewSaleVo();
		vo.setApprovedBy(dto.getCreatedBy());
		// vo.setBiller(dto.getBiller());
		vo.setCreatedDate(dto.getCreationDate());
//		if (dto.getDlSlip() != null) {
//			vo.setDlSlip(deliverySlipMapper.convertDsEntityListToVoList(dto.getDlSlip()));
//		}
		vo.setGrossAmount(dto.getGrossValue());
		vo.setInvoiceNumber(dto.getOrderNumber());
		vo.setNatureOfSale(dto.getNatureOfSale());
		vo.setNetPayableAmount(dto.getNetValue());
		vo.setOfflineNumber(dto.getOfflineNumber());
//		if (dto.getPaymentType() != null) {
//			vo.setPaymentAmountType(paymentAmountTypeMapper.EntityToVo(dto.getPaymentType()));
//
//		}
		// vo.setRoundOff(dto.getRoundOff());
		vo.setTaxAmount(dto.getTaxValue());
		vo.setTotalManualDisc(dto.getManualDisc());
		vo.setTotalPromoDisc(dto.getPromoDisc());
//		if (dto.getCustomerDetails() != null) {
//			vo.setCustomerDetails(customerMapper.convertEntityToVo(dto.getCustomerDetails()));
//		}

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

	public List<LineItemVo> convertBarcodesEntityToVo(List<LineItemsEntity> barcodeDetails) {
		return barcodeDetails.stream().map(dto -> barentityToVo(dto)).collect(Collectors.toList());

	}
	public List<LineItemVo> convertBarcodesReEntityToVo(List<LineItemsReEntity> barcodeDetails) {
		return barcodeDetails.stream().map(dto -> barentityToVo(dto)).collect(Collectors.toList());

	}
	private LineItemVo barentityToVo(LineItemsReEntity dto) {

		LineItemVo vo = new LineItemVo();
		BeanUtils.copyProperties(dto, vo);

		return vo;
	}

	private LineItemVo barentityToVo(LineItemsEntity dto) {

		LineItemVo vo = new LineItemVo();
		BeanUtils.copyProperties(dto, vo);

		return vo;
	}

}
