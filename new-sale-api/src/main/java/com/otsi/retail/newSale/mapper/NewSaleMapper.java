package com.otsi.retail.newSale.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.otsi.retail.newSale.common.DomainData;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.LineItemVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.LoyalityPointsVo;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.PaymentAmountTypeVo;
import com.otsi.retail.newSale.vo.SaleBillsVO;
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
					linevo.setDiscount(lineItem.getDiscount());
					linevo.setSection(lineItem.getSection());
					linevo.setSection(lineItem.getSection());
					linevo.setHsnCode(lineItem.getHsnCode());
					linevo.setSgst(lineItem.getSgst());
					linevo.setCgst(lineItem.getCgst());
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
		newSaleVO.setStatus(sale.getStatus());
		newSaleVO.setNewsaleId(sale.getOrderId());
		return newSaleVO;
	}

	public SaleReportVo convertlistSaleReportEntityToVo(List<NewSaleEntity> saleDetails) {

		SaleReportVo srvo = new SaleReportVo();

		srvo.setBillValue(saleDetails.stream().mapToLong(b -> b.getNetValue()).sum());
		srvo.setTotalMrp(saleDetails.stream().mapToLong(m -> m.getGrossValue()).sum());

		List<Long> result = saleDetails.stream().map(num -> num.getPromoDisc()).filter(n -> n != null)
				.collect(Collectors.toList());

		srvo.setTotalDiscount(result.stream().mapToLong(d -> d).sum());
		return srvo;

	}

	public ListOfDeliverySlipVo convertListDSToVo(Page<DeliverySlipEntity> dsDetails) {

		ListOfDeliverySlipVo vo = new ListOfDeliverySlipVo();

		Page<DeliverySlipVo> dspageVo = dsDetails.map(dsDetail -> dsentityToVo(dsDetail));

		vo.setDeliverySlip(dspageVo);
		/*vo.setToatalPromoDisc(dsDetails.stream().filter(promo->promo.getPromoDisc()!=null).mapToLong(promo->promo.getPromoDisc()).sum());
		dsDetails.stream().forEach(a -> {
	        vo.setBartoatalPromoDisc(a.getLineItems().stream().filter(i->i.getDiscount()!=null).mapToLong(i ->i.getDiscount()).sum());
			vo.setBartotalNetAmount(a.getLineItems().stream().mapToLong(i -> i.getNetValue()).sum());
			vo.setBartotalGrossAmount(a.getLineItems().stream().mapToLong(i -> i.getGrossValue()).sum());
			vo.setBarTotalQty(a.getLineItems().stream().mapToInt(q -> q.getQuantity()).sum());

		});*/

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
		dsvo.setMrp(grossAmount);

		// dsVoList.add(dsvo);

		// vo.setToatalPromoDisc(dsDetails.stream().mapToLong(i ->
		// i.getPromoDisc()).sum());
		// vo.setTotalNetAmount(dsDetails.stream().mapToLong(i ->
		// i.getNetAmount()).sum());
		// vo.setTotalGrossAmount(dsDetails.stream().mapToLong(i -> i.getMrp()).sum());
		/*
		 * vo.setDeliverySlipVo(dsVoList);
		 * 
		 * dsDetails.stream().forEach(a -> {
		 * 
		 * 
		 * //vo.setBartoatalPromoDisc(a.getLineItems().stream().mapToLong(i ->
		 * i.getDiscount()).sum());
		 * vo.setBartotalNetAmount(a.getLineItems().stream().mapToLong(i ->
		 * i.getNetValue()).sum());
		 * vo.setBartotalGrossAmount(a.getLineItems().stream().mapToLong(i ->
		 * i.getGrossValue()).sum());
		 * vo.setBarTotalQty(a.getLineItems().stream().mapToInt(q ->
		 * q.getQuantity()).sum());
		 * 
		 * });
		 */

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
		vo.setCreatedDate(dto.getCreatedDate());
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
		vo.setUserId(dto.getUserId());
		vo.setApprovedBy(dto.getCreatedBy());
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
