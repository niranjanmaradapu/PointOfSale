package com.otsi.retail.newSale.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.Entity.BarcodeEntity;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.LineItemsEntity;
import com.otsi.retail.newSale.Entity.LineItemsReEntity;
import com.otsi.retail.newSale.Entity.LoyalityPointsEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.LoyalityPointsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.PaymentAmountTypeVo;
import com.otsi.retail.newSale.vo.SaleBillsVO;
import com.otsi.retail.newSale.vo.SalesSummeryVo;
import com.otsi.retail.newSale.vo.TaxVo;

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

		entity.setCreatedDate(LocalDateTime.now());

		return entity;
	}

	public BarcodeVo convertBarcodeEntityToVo(BarcodeEntity entity) {

		BarcodeVo vo = new BarcodeVo();

		BeanUtils.copyProperties(entity, vo);

		return vo;
	}

	public DeliverySlipEntity convertDSVoToEntity(DeliverySlipVo vo) {

		return null;
	}

	public SaleBillsVO converEntityToVo(Page<NewSaleEntity> saleDetails) {
		SaleBillsVO saleBillsVO = new SaleBillsVO();
		Page<NewSaleVo> newSaleVOPage = saleDetails.map(sale -> {
			return mapToNewSaleVO(sale);
		});
		saleBillsVO.setNewSale(newSaleVOPage);
		saleBillsVO.setTotalAmount(saleDetails.stream().mapToLong(saleDetail -> saleDetail.getNetValue()).sum());
		return saleBillsVO;
	}

	private NewSaleVo mapToNewSaleVO(NewSaleEntity sale) {
		NewSaleVo newSaleVO = new NewSaleVo();
		List<LineItemVo> lineItemsList = new ArrayList<>();

		if (sale.getDlSlip() != null) {
			sale.getDlSlip().stream().forEach(dlSlip -> {
				dlSlip.getLineItems().stream().forEach(lineItem -> {

					LineItemVo linevo = new LineItemVo();
					linevo.setCreatedDate(lineItem.getCreatedDate());
					linevo.setBarCode(lineItem.getBarCode());
					linevo.setItemPrice(lineItem.getItemPrice());
					linevo.setGrossValue(lineItem.getGrossValue());
					linevo.setNetValue(lineItem.getNetValue());
					linevo.setQuantity(lineItem.getQuantity());
					linevo.setManualDiscount(lineItem.getManualDiscount());
					linevo.setSection(lineItem.getSection());
					linevo.setSection(lineItem.getSection());
					linevo.setHsnCode(lineItem.getHsnCode());
					linevo.setSgst(lineItem.getSgst());
					linevo.setCgst(lineItem.getCgst());
					linevo.setIgst(lineItem.getIgst());
					linevo.setCess(lineItem.getCess());
					linevo.setUserId(dlSlip.getUserId());
					linevo.setTaxValue(lineItem.getTaxValue());
					lineItemsList.add(linevo);
					newSaleVO.setLineItemsReVo(lineItemsList);
				});
			});
		}

		newSaleVO.setInvoiceNumber(sale.getOrderNumber());
		newSaleVO.setBiller(sale.getCreatedBy());
		newSaleVO.setCreatedDate(sale.getCreatedDate());
		newSaleVO.setTotalManualDisc(sale.getManualDisc());
		newSaleVO.setApprovedBy(sale.getCreatedBy());
		newSaleVO.setReason(sale.getDiscType());
		newSaleVO.setOfflineNumber(sale.getOfflineNumber());
		newSaleVO.setNetPayableAmount(sale.getNetValue());
		newSaleVO.setUserId(sale.getUserId());
		newSaleVO.setEmpId(sale.getCreatedBy());
		newSaleVO.setDiscount(sale.getManualDisc() + sale.getPromoDisc());
		newSaleVO.setStatus(sale.getStatus());
		newSaleVO.setNewsaleId(sale.getOrderId());
		return newSaleVO;
	}

	public SalesSummeryVo convertlistSaleReportEntityToVo(List<NewSaleEntity> saleDetails) {
		List<SalesSummeryVo> listSummeryVo = new ArrayList<>();

		SalesSummeryVo srvo = new SalesSummeryVo();
		srvo.setBillValue(saleDetails.stream().mapToLong(b -> b.getNetValue()).sum());
		srvo.setTotalMrp(saleDetails.stream().mapToLong(m -> m.getGrossValue()).sum());

		List<Long> result = saleDetails.stream().map(num -> num.getPromoDisc()).filter(n -> n != null)
				.collect(Collectors.toList());

		srvo.setTotalDiscount(result.stream().mapToLong(d -> d).sum());
		saleDetails.stream().forEach(saleDetail -> {

			saleDetail.getDlSlip().stream().forEach(deliverySlp -> {
				SalesSummeryVo summeryvo = new SalesSummeryVo();
				List<LineItemsEntity> lineitems1 = deliverySlp.getLineItems().stream()
						.filter(lineItem -> lineItem.getCgst() != null).collect(Collectors.toList());
				List<LineItemsEntity> lineitems2 = deliverySlp.getLineItems().stream()
						.filter(lineItem -> lineItem.getSgst() != null).collect(Collectors.toList());
				List<LineItemsEntity> lineitems3 = deliverySlp.getLineItems().stream()
						.filter(lineItem -> lineItem.getIgst() != null).collect(Collectors.toList());

				List<LineItemsEntity> lineitems4 = deliverySlp.getLineItems().stream()
						.filter(lineItem -> lineItem.getCess() != null).collect(Collectors.toList());
				List<LineItemsEntity> lineitems5 = deliverySlp.getLineItems().stream()
						.filter(lineItem -> lineItem.getTaxValue() != null).collect(Collectors.toList());

				Double totalCgst = lineitems1.stream().mapToDouble(lineItem -> lineItem.getCgst()).sum();
				Double totalSgst = lineitems2.stream().mapToDouble(lineItem -> lineItem.getSgst()).sum();
				Double totalIgst = lineitems3.stream().mapToDouble(lineItem -> lineItem.getIgst()).sum();
				Double totalCess = lineitems4.stream().mapToDouble(lineItem -> lineItem.getCess()).sum();

				summeryvo.setTotalCgst(totalCgst.floatValue());

				summeryvo.setTotalSgst(totalSgst.floatValue());
				summeryvo.setTotalIgst(totalIgst.floatValue());
				summeryvo.setTotalCess(totalCess.floatValue());

				summeryvo.setTotalTaxAmount(lineitems5.stream().mapToLong(lineItem -> lineItem.getTaxValue()).sum());

				listSummeryVo.add(summeryvo);
			});

		});

		Double totalSgst = listSummeryVo.stream().mapToDouble(summeryVo -> summeryVo.getTotalSgst()).sum();
		Double totalCgst = listSummeryVo.stream().mapToDouble(summeryVo -> summeryVo.getTotalCgst()).sum();
		Double totalIgst = listSummeryVo.stream().mapToDouble(summeryVo -> summeryVo.getTotalIgst()).sum();
		Double totalCess = listSummeryVo.stream().mapToDouble(summeryVo -> summeryVo.getTotalCess()).sum();
		srvo.setTotalSgst(totalSgst.floatValue());
		srvo.setTotalCgst(totalCgst.floatValue());
		srvo.setTotalIgst(totalIgst.floatValue());
		srvo.setTotalCess(totalCess.floatValue());

		// srvo.setTotalTaxAmount(listSummeryVo.stream().mapToLong(summeryVo ->
		// summeryVo.getTotalTaxAmount()).sum());

		return srvo;

	}

	public ListOfDeliverySlipVo convertListDSToVo(Page<DeliverySlipEntity> dsDetails) {

		ListOfDeliverySlipVo vo = new ListOfDeliverySlipVo();

		Page<DeliverySlipVo> dspageVo = dsDetails.map(dsDetail -> dsentityToVo(dsDetail));

		vo.setDeliverySlip(dspageVo);

		return vo;
	}

	private DeliverySlipVo dsentityToVo(DeliverySlipEntity dsEntity) {

		List<DeliverySlipVo> dsVoList = new ArrayList<>();

		List<LineItemVo> listBarVo = new ArrayList<>();

		dsEntity.getLineItems().stream().forEach(b -> {

			LineItemVo barvo = new LineItemVo();

			BeanUtils.copyProperties(b, barvo);
			barvo.setUserId(dsEntity.getUserId());

			listBarVo.add(barvo);
		});

		DeliverySlipVo dsvo = new DeliverySlipVo();
		Long amount = dsEntity.getLineItems().stream().mapToLong(a -> a.getNetValue()).sum();
		Long grossAmount = dsEntity.getLineItems().stream().mapToLong(a -> a.getGrossValue()).sum();

		BeanUtils.copyProperties(dsEntity, dsvo);
		dsvo.setCreatedDate(dsEntity.getCreatedDate());
		dsvo.setLineItems(listBarVo);
		dsvo.setNetAmount(amount);
		dsvo.setGrossAmount(grossAmount);

		return dsvo;

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

		vo.setPaymentAmountTypeId(payVos);
		vo.setAmount(dto.getNetValue() - dto.getNetValue());
		vo.setInvoiceNumber(dto.getOrderNumber());
		return vo;
	}

	public NewSaleVo convertNewSaleDtoToVo(NewSaleEntity dto) {
		NewSaleVo vo = new NewSaleVo();
		vo.setApprovedBy(dto.getCreatedBy());
		// vo.setBiller(dto.getBiller());
		vo.setCreatedDate(dto.getCreatedDate());
		vo.setGrossAmount(dto.getGrossValue());
		vo.setInvoiceNumber(dto.getOrderNumber());
		vo.setNatureOfSale(dto.getNatureOfSale());
		vo.setNetPayableAmount(dto.getNetValue());
		vo.setOfflineNumber(dto.getOfflineNumber());
		vo.setTaxAmount(dto.getTaxValue());
		vo.setTotalManualDisc(dto.getManualDisc());
		vo.setTotalPromoDisc(dto.getPromoDisc());
		vo.setUserId(dto.getUserId());
		vo.setApprovedBy(dto.getCreatedBy());

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

	// VoToEntity method

	public LoyalityPointsEntity convertLoyaltyVoToEntity(LoyalityPointsVo loyalityVo) {

		LoyalityPointsEntity entity = new LoyalityPointsEntity();
		entity.setLoyaltyId(loyalityVo.getLoyaltyId());
		entity.setUserId(loyalityVo.getUserId());
		entity.setMobileNumber(loyalityVo.getMobileNumber());
		entity.setCustomerName(loyalityVo.getCustomerName());
		entity.setDomainId(loyalityVo.getDomainId());
		entity.setInvoiceNumber(loyalityVo.getInvoiceNumber());
		entity.setInvoiceAmount(loyalityVo.getInvoiceAmount());
		entity.setLoyaltyPoints((loyalityVo.getInvoiceAmount() / 10));
		entity.setExpiredDate((loyalityVo.getInvoiceCreatedDate()).plusMonths(loyalityVo.getNumberOfMonths()));

		return entity;

	}

	// EntityToVo method

	public LoyalityPointsVo convertLoyaltyEntityToVo(LoyalityPointsEntity loyalityEntity) {

		LoyalityPointsVo vo = new LoyalityPointsVo();
		vo.setLoyaltyId(loyalityEntity.getLoyaltyId());
		vo.setUserId(loyalityEntity.getUserId());
		vo.setDomainId(loyalityEntity.getDomainId());
		vo.setMobileNumber(loyalityEntity.getMobileNumber());
		vo.setCustomerName(loyalityEntity.getCustomerName());
		vo.setLoyaltyPoints(loyalityEntity.getLoyaltyPoints());
		vo.setInvoiceNumber(loyalityEntity.getInvoiceNumber());
		vo.setInvoiceAmount(loyalityEntity.getInvoiceAmount());
		vo.setCreatedDate(loyalityEntity.getCreatedDate());
		vo.setExpiredDate(loyalityEntity.getExpiredDate());

		return vo;
	}

	public List<LoyalityPointsEntity> convertloyaltyVoToEntity(List<LoyalityPointsVo> loyaltyList) {
		return loyaltyList.stream().map(dto -> convertLoyaltyVoToEntity(dto)).collect(Collectors.toList());

	}

	public List<LoyalityPointsVo> convertLoyaltyEntityToVo(List<LoyalityPointsEntity> listOfLoyaltyPoints) {

		return listOfLoyaltyPoints.stream().map(entity -> convertLoyaltyEntityToVo(entity))
				.collect(Collectors.toList());

	}

}
