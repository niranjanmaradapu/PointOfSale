package com.otsi.retail.promotions.mapper;

import java.util.ArrayList;
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

	public PromotionsEntity convertPromoVoToEntity(PromotionsVo vo, List<PoolEntity> poolList, List<PoolEntity> poolList1) {

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
		promo.setStoreId(vo.getStoreId());
		promo.setClientId(vo.getClientId());
		// promo.setPriority(vo.getPriority());
		promo.setPromotionStartDate(vo.getPromotionStartDate());
		promo.setPromotionEndDate(vo.getPromotionEndDate());
		promo.setPoolEntity(poolList);// Mapping all poolIds to Promotions

		List<PromotionSlabsEntity> slabsEntity = new ArrayList<>();
		vo.getPromotionSlabVo().stream().forEach(s -> {
			PromotionSlabsEntity slab = new PromotionSlabsEntity();
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
			benefit.setPoolId(s.getBenfitVo().getPoolId());
			benefit.setPoolName(s.getBenfitVo().getPoolName());
			
			//newly added
			
			benefit.setPoolEntities(poolList1);

			// mapping benefits to slab
			slab.setBenfitEntity(benefit);

			slabsEntity.add(slab);
		});
		// mapping slabs to promotions
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
			benfit.setDiscountSubTypes(b.getDiscountSubType());

			// mapping benefits to promotions
			benfit.setPromotionEntity(promo);
			
			// mapping benefits to pools
			benfit.setPoolEntities(poolList1);

			benfitEntity.add(benfit);
		});
		promo.setBenfitEntity(benfitEntity);

		return promo;
	}

	public List<PromotionsVo> convertPromoEntityToVo(List<PromotionsEntity> promoList) {

		List<PromotionsVo> listOfPromoVos = new ArrayList<>();

		promoList.stream().forEach(x -> {
			PromotionsVo vo = new PromotionsVo();

			vo.setPromoId(x.getPromoId());
			vo.setPromotionName(x.getPromotionName());
			vo.setDomainId(x.getDomainId());
			vo.setStoreId(x.getStoreId());
			vo.setClientId(x.getClientId());
			vo.setDescription(x.getDescription());
			vo.setPrintNameOnBill(x.getPrintNameOnBill());
			vo.setApplicability(x.getApplicability());
			vo.setCreatedBy(x.getCreatedBy());
			vo.setIsTaxExtra(x.getIsTaxExtra());
			vo.setIsActive(x.getIsActive());
			vo.setBuyItemsFromPool(x.getBuyItemsFromPool());
			vo.setPromotionStartDate(x.getPromotionStartDate());
			vo.setPromotionEndDate(x.getPromotionEndDate());
			vo.setPromoApplyType(x.getPromoApplyType());

			listOfPromoVos.add(vo);

			List<PromotionSlabsVo> slabVo = new ArrayList<>();
			x.getPromotionSlabEntity().stream().forEach(s -> {
				PromotionSlabsVo pslabVo = new PromotionSlabsVo();
				pslabVo.setId(s.getId());
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
				
				benfitVos.add(bvo);
				// setting promoSlabValues to benefits

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
				pool.setStoreId(a.getStoreId());
				pool.setClientId(a.getClientId());
				pool.setPoolName(a.getPoolName());
				pool.setPoolType(a.getPoolType());
				pool.setIsActive(a.getIsActive());
				
				poolList.add(pool);
			});
			vo.setPoolVo(poolList);
			vo.setBenfitVo(benfitVos);
		});

		return listOfPromoVos;
	}

	public List<BenefitVo> convertBenfitEntityToVo(List<BenfitEntity> benefitEntities) {

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

			list.add(bvo);

		}

		return list;

	}

	public PromotionsVo convertPromotionEntityToVo(PromotionsEntity promotions) {
		
		
		PromotionsVo promo = new PromotionsVo();
		
		promo.setPromoId(promotions.getPromoId());
		promo.setClientId(promotions.getClientId());
		promo.setPromotionName(promotions.getPromotionName());
		promo.setDomainId(promotions.getDomainId());
		promo.setStoreId(promotions.getStoreId());
		promo.setClientId(promotions.getClientId());
		promo.setDescription(promotions.getDescription());
		promo.setPrintNameOnBill(promotions.getPrintNameOnBill());
		promo.setIsTaxExtra(promotions.getIsTaxExtra());
		promo.setApplicability(promotions.getApplicability());
		promo.setCreatedBy(promotions.getCreatedBy());
		promo.setPromoApplyType(promotions.getPromoApplyType());
		promo.setBuyItemsFromPool(promotions.getBuyItemsFromPool());
		promo.setIsActive(Boolean.TRUE);
		promo.setStoreId(promotions.getStoreId());
		promo.setClientId(promotions.getClientId());
		// promo.setPriority(vo.getPriority());
		promo.setPromotionStartDate(promotions.getPromotionStartDate());
		promo.setPromotionEndDate(promotions.getPromotionEndDate());

		List<PromotionSlabsVo> slabsVo = new ArrayList<>();
		promotions.getPromotionSlabEntity().stream().forEach(s -> {
			PromotionSlabsVo slab = new PromotionSlabsVo();
			slab.setId(s.getId());
			slab.setFromSlab(s.getFromSlab());
			slab.setToSlab(s.getToSlab());

			BenefitVo benefit = new BenefitVo();
			benefit.setBenfitId(s.getBenfitEntity().getBenfitId());
			benefit.setBenfitType(s.getBenfitEntity().getBenfitType());
			benefit.setDiscount(s.getBenfitEntity().getDiscount());
			benefit.setDiscountType(s.getBenfitEntity().getDiscountType());
			benefit.setItemValue(s.getBenfitEntity().getItemValue());
			benefit.setNumOfItemsFromBuyPool(s.getBenfitEntity().getNumOfItemsFromBuyPool());
			benefit.setNumOfItemsFromGetPool(s.getBenfitEntity().getNumOfItemsFromGetPool());
			benefit.setDiscountSubType(s.getBenfitEntity().getDiscountSubTypes());

			// mapping benefits to slab
			slab.setBenfitVo(benefit);

			slabsVo.add(slab);
		});
		// mapping slabs to promotions
		promo.setPromotionSlabVo(slabsVo);

		List<BenefitVo> benfitVo = new ArrayList<>();
		promotions.getBenfitEntity().stream().forEach(b -> {
			BenefitVo benfit = new BenefitVo();
			benfit.setBenfitId(b.getBenfitId());
			benfit.setBenfitType(b.getBenfitType());
			benfit.setDiscountType(b.getDiscountType());
			benfit.setDiscount(b.getDiscount());
			benfit.setItemValue(b.getItemValue());
			benfit.setNumOfItemsFromGetPool(b.getNumOfItemsFromBuyPool());
			benfit.setNumOfItemsFromGetPool(b.getNumOfItemsFromGetPool());
			benfit.setDiscountSubType(b.getDiscountSubTypes());

			benfitVo.add(benfit);
		});
		promo.setBenfitVo(benfitVo);

		return promo;
	}

}
