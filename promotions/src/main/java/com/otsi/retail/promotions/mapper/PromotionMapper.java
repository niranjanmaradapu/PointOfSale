package com.otsi.retail.promotions.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.BenfitEntity;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.PromotionSlabsEntity;
import com.otsi.retail.promotions.entity.PromotionsEntity;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.PromotionSlabsVo;
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
		//promo.setPriority(vo.getPriority());
		promo.setPromotionStartDate(vo.getPromotionStartDate());
		promo.setPromotionEndDate(vo.getPromotionEndDate());
		promo.setPoolEntity(poolList);// Mapping all poolIds to Promotions
		
		List<PromotionSlabsEntity> slabsEntity = new  ArrayList<>();
		vo.getPromotionSlabVo().stream().forEach(s->{
			PromotionSlabsEntity  slab =  new PromotionSlabsEntity();
			slab.setId(s.getId());
			slab.setFromSlab(s.getFromSlab());
			slab.setToSlab(s.getToSlab());
		   
			
			BenfitEntity benefit = new BenfitEntity();
			benefit.setBenfitId(s.getBenfitVo().getBenfitId());
			benefit.setBenfitType(s.getBenfitVo().getBenfitType());
			benefit.setDiscount(s.getBenfitVo().getDiscount());
			benefit.setDiscountType(s.getBenfitVo().getDiscountType());
			benefit.setItemValue(s.getBenfitVo().getItemValue());
			benefit.setNumOfItemsFromBuyPool(s.getBenfitVo().getNumOfItemsFromBuyPool());
			benefit.setNumOfItemsFromGetPool(s.getBenfitVo().getNumOfItemsFromGetPool());
			benefit.setDiscountSubTypes(s.getBenfitVo().getDiscountSubType());
			
			//mapping benefits to slab
			slab.setBenfitEntity(benefit);
			
			slabsEntity.add(slab);
		});
	    //mapping slabs to promotions
		promo.setPromotionSlabEntity(slabsEntity);

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
			benfit.setDiscountSubTypes(b.getDiscountSubType());
			benfit.setPoolName(b.getPoolName());
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
			vo.setPromotionStartDate(x.getPromotionStartDate());
			vo.setPromotionEndDate(x.getPromotionEndDate());
			vo.setPromoApplyType(x.getPromoApplyType());
			listOfPromoVos.add(vo);
			
			List<PromotionSlabsVo> slabVo = new ArrayList<>();
			x.getPromotionSlabEntity().stream().forEach(s->{
				PromotionSlabsVo pslabVo = new PromotionSlabsVo();
				pslabVo.setFromSlab(s.getFromSlab());
				pslabVo.setToSlab(s.getToSlab());
				
				BenefitVo bvo = new BenefitVo();
				bvo.setBenfitId(s.getBenfitEntity().getBenfitId());
				bvo.setBenfitType(s.getBenfitEntity().getBenfitType());
				bvo.setDiscount(s.getBenfitEntity().getDiscount());
				bvo.setDiscountSubType(s.getBenfitEntity().getDiscountSubTypes());
				bvo.setDiscountType(s.getBenfitEntity().getDiscountType());
				bvo.setItemValue(s.getBenfitEntity().getItemValue());
				bvo.setNumOfItemsFromBuyPool(s.getBenfitEntity().getNumOfItemsFromBuyPool());
				bvo.setNumOfItemsFromGetPool(s.getBenfitEntity().getNumOfItemsFromGetPool());
				bvo.setPoolId(s.getBenfitEntity().getPoolId());
				bvo.setPoolName(s.getBenfitEntity().getPoolName());
				pslabVo.setBenfitVo(bvo);
				slabVo.add(pslabVo);
			});
			
			vo.setPromotionSlabVo(slabVo);

			List<BenefitVo> benfitVos = new ArrayList<>();
			x.getBenfitEntity().stream().forEach(b -> {
				BenefitVo bvo = new BenefitVo();
				
				
				bvo.setBenfitId(b.getBenfitId());
				bvo.setBenfitType(b.getBenfitType());
				bvo.setDiscount(b.getDiscount());
				bvo.setDiscountType(b.getDiscountType());
				bvo.setItemValue(b.getItemValue());
				bvo.setDiscountSubType(b.getDiscountSubTypes());
				bvo.setNumOfItemsFromBuyPool(b.getNumOfItemsFromBuyPool());
				bvo.setNumOfItemsFromGetPool(b.getNumOfItemsFromGetPool());
				bvo.setPoolId(b.getPoolId());
				bvo.setPoolName(b.getPoolName());
				benfitVos.add(bvo);
				//setting promoSlabValues to benefits
				
//				pslabVo.setFromSlab(b.getPromotionSlabEntity().getFromSlab());
//				pslabVo.setToSlab(b.getPromotionSlabEntity().getToSlab());
//				bvo.setPromoSlabVo(pslabVo);
//				benfitVos.add(bvo);
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
	
	public List<BenefitVo> convertBenfitEntityToVo(List<BenfitEntity>  benefitEntities)
	{
		
		List<BenefitVo> list = new ArrayList<>();
		
		for (BenfitEntity benfitEntity : benefitEntities) {

           BenefitVo bvo = new BenefitVo();
           
			bvo.setBenfitId(benfitEntity.getBenfitId());
			bvo.setBenfitType(benfitEntity.getBenfitType());
			bvo.setDiscount(benfitEntity.getDiscount());
			bvo.setDiscountType(benfitEntity.getDiscountType());
			bvo.setDiscountSubType(benfitEntity.getDiscountSubTypes());
			bvo.setItemValue(benfitEntity.getItemValue());
			bvo.setNumOfItemsFromBuyPool(benfitEntity.getNumOfItemsFromBuyPool());
			bvo.setNumOfItemsFromGetPool(benfitEntity.getNumOfItemsFromGetPool());
			bvo.setPoolId(benfitEntity.getPoolId());
			bvo.setPoolName(benfitEntity.getPoolName());
			
			list.add(bvo);
			
		}	
			
		return list;
		
		
	}
	
	


}
