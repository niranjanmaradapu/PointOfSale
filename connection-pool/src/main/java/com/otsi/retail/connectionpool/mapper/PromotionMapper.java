package com.otsi.retail.connectionpool.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.entity.BenfitEntity;
import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;
import com.otsi.retail.connectionpool.vo.BenfitVo;
import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;

/**
 * This class maps all VOs and Entitys related to Promotions
 * 
 * @author Manikanta Guptha
 *
 */
@Component
public class PromotionMapper {

	public PromotionsEntity convertPromoVoToEntity(PromotionsVo vo, List<PoolEntity> poolList,BenfitVo bvo) {

		PromotionsEntity promo = new PromotionsEntity();

		if (vo.getIsForEdit()) {
			promo.setPromoId(vo.getPromoId());
		}

		promo.setPromotionName(vo.getPromotionName());
		promo.setDomainId(vo.getDomainId());
		promo.setDescription(vo.getDescription());
		promo.setPrintNameOnBill(vo.getPrintNameOnBill());
		promo.setIsTaxExtra(vo.getIsTaxExtra());
		promo.setApplicability(vo.getApplicability());
		promo.setCreatedBy(vo.getCreatedBy());
		promo.setPromoApplyType(vo.getPromoApplyType());
		promo.setBuyItemsFromPool(vo.getBuyItemsFromPool());
		promo.setIsActive(Boolean.TRUE);
		promo.setCreatedDate(LocalDate.now());
		promo.setLastModified(LocalDate.now());
		promo.setPriority(vo.getPriority());
		promo.setPromoType(vo.getPromoType());
		promo.setStartDate(vo.getStartDate());
		promo.setEndDate(vo.getEndDate());
		promo.setStoreName(vo.getStoreName());
		//BeanUtils.copyProperties(vo, promo);
		promo.setPoolEntity(poolList);// Mapping all poolIds to Promotions

		BenfitEntity benfitEntity = new BenfitEntity();
		benfitEntity.setBenfitId(bvo.getBenfitId());
		benfitEntity.setBenfitType(bvo.getBenfitType());
		benfitEntity.setDiscountType(bvo.getDiscountType());
		benfitEntity.setPercentageDiscountOn(bvo.getPercentageDiscountOn());
		benfitEntity.setDiscount(bvo.getDiscount());

//		//BeanUtils.copyProperties(bvo, benfitEntity);
        promo.setBenfitEntity(benfitEntity);

		return promo;
	}

	public List<PromotionsVo> convertPromoEntityToVo(List<PromotionsEntity> promoList) {

		List<PromotionsVo> listOfPromoVos = new ArrayList<>();

		promoList.stream().forEach(x -> {
			PromotionsVo vo = new PromotionsVo();

			vo.setPromotionName(x.getPromotionName());
			vo.setDomainId(x.getDomainId());
			vo.setDescription(x.getDescription());
			vo.setPrintNameOnBill(x.getPrintNameOnBill());
			vo.setApplicability(x.getApplicability());
			vo.setCreatedBy(x.getCreatedBy());
			vo.setIsTaxExtra(x.getIsTaxExtra());
			vo.setIsActive(x.getIsActive());
			vo.setBuyItemsFromPool(x.getBuyItemsFromPool());
			vo.setCreatedDate(x.getCreatedDate());
			vo.setLastModified(x.getLastModified());
			vo.setPromoId(x.getPromoId());
			vo.setPromoType(x.getPromoType());
			vo.setStartDate(x.getStartDate());
			vo.setEndDate(x.getEndDate());
			vo.setStoreName(x.getStoreName());
			vo.setPromoApplyType(x.getPromoApplyType());
			vo.setPriority(x.getPriority());
			listOfPromoVos.add(vo);

			BenfitVo bvo = new BenfitVo();
         
			bvo.setBenfitId(x.getBenfitEntity().getBenfitId());
			bvo.setBenfitType(x.getBenfitEntity().getBenfitType());
			bvo.setDiscountType(x.getBenfitEntity().getDiscountType());
			bvo.setPercentageDiscountOn(x.getBenfitEntity().getPercentageDiscountOn());
			bvo.setDiscount(x.getBenfitEntity().getDiscount());
			vo.setBenfitVo(bvo);

			List<ConnectionPoolVo> poolList = new ArrayList<>();

			x.getPoolEntity().stream().forEach(a -> {

				ConnectionPoolVo pool = new ConnectionPoolVo();
				pool.setPoolId(a.getPoolId());
				pool.setDomainId(a.getDomainId());
				pool.setPoolName(a.getPoolName());
				pool.setPoolType(a.getPoolType());
				pool.setIsActive(a.getIsActive());
				pool.setCreatedDate(a.getCreatedDate());
				poolList.add(pool);
			});
			vo.setPoolVo(poolList);
		});

		return listOfPromoVos;
	}

}
