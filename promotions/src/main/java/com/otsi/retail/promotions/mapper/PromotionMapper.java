package com.otsi.retail.promotions.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.BenfitEntity;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.PromotionsEntity;
import com.otsi.retail.promotions.entity.Pool_Rule;
import com.otsi.retail.promotions.vo.BenfitVo;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.PromotionsVo;

/**
 * This class maps all VOs and Entitys related to Promotions
 * 
 * @author Manikanta Guptha
 *
 */
@Component
public class PromotionMapper {

	public PromotionsEntity convertPromoVoToEntity(PromotionsVo vo, List<PoolEntity> poolList) {

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
		promo.setPoolEntity(poolList);// Mapping all poolIds to Promotions

		List<BenfitEntity> benfitEntity = new ArrayList<>();
		vo.getBenfitVo().stream().forEach(b -> {
			BenfitEntity benfit = new BenfitEntity();
			benfit.setBenfitId(b.getBenfitId());
			benfit.setBenfitType(b.getBenfitType());
			benfit.setDiscountType(b.getDiscountType());
			benfit.setDiscount(b.getDiscount());
			benfit.setItemValue(b.getItemValue());
			benfit.setNumOfItemsFromGetPool(b.getNumOfItemsFromBuyPool());
			benfit.setNumOfItemsFromGetPool(b.getNumOfItemsFromGetPool());
			benfit.setPoolId(b.getPoolId());
			benfit.setPercentageDiscountOn(b.getPercentageDiscountOn());
			benfit.setPoolName(b.getPoolName());
			benfit.setToSlab(b.getToSlab());
			benfit.setFromSlab(b.getFromSlab());
			benfitEntity.add(benfit);
		});
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

			List<BenfitVo> benfitVos = new ArrayList<>();
			x.getBenfitEntity().stream().forEach(b -> {
				BenfitVo bvo = new BenfitVo();
				bvo.setBenfitId(b.getBenfitId());
				bvo.setBenfitType(b.getBenfitType());
				bvo.setDiscount(b.getDiscount());
				bvo.setDiscountType(b.getDiscountType());
				bvo.setItemValue(b.getItemValue());
				bvo.setPercentageDiscountOn(b.getPercentageDiscountOn());
				bvo.setNumOfItemsFromBuyPool(b.getNumOfItemsFromBuyPool());
				bvo.setNumOfItemsFromGetPool(b.getNumOfItemsFromGetPool());
				bvo.setPoolId(b.getPoolId());
				bvo.setPoolName(b.getPoolName());
				bvo.setToSlab(b.getToSlab());
				bvo.setFromSlab(b.getFromSlab());
				benfitVos.add(bvo);
			});

			List<PromotionPoolVo> poolList = new ArrayList<>();

			x.getPoolEntity().stream().forEach(a -> {

				PromotionPoolVo pool = new PromotionPoolVo();
				pool.setPoolId(a.getPoolId());
				pool.setDomainId(a.getDomainId());
				pool.setPoolName(a.getPoolName());
				pool.setPoolType(a.getPoolType());
				pool.setIsActive(a.getIsActive());
				pool.setCreatedDate(a.getCreatedDate());
				poolList.add(pool);
			});
			vo.setPoolVo(poolList);
			vo.setBenfitVo(benfitVos);
		});

		return listOfPromoVos;
	}


}
