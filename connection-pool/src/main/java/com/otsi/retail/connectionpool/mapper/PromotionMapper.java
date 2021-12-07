package com.otsi.retail.connectionpool.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;
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

	public PromotionsEntity convertPromoVoToEntity(PromotionsVo vo, List<PoolEntity> poolList/*,List<StoresEntity> stores */) {

		PromotionsEntity promo = new PromotionsEntity();

		if (vo.getIsForEdit()) {
			promo.setPromoId(vo.getPromoId());
		}
		promo.setPromoName(vo.getPromotionName());
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
		promo.setPoolEntity(poolList);// Mapping all poolIds to Promotions
		
		/** Store Mapping **/
		
		//promo.setStoreEntity(stores);//Mapping all storeIds to Promotions

		return promo;
	}

	public List<PromotionsVo> convertPromoEntityToVo(List<PromotionsEntity> promoList) {

		List<PromotionsVo> listOfPromoVos = new ArrayList<>();

		promoList.stream().forEach(x -> {
			PromotionsVo vo = new PromotionsVo();

			vo.setPromotionName(x.getPromoName());
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
			vo.setPromoApplyType(x.getPromoApplyType());
			vo.setPriority(x.getPriority());
			listOfPromoVos.add(vo);

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
