package com.otsi.retail.promotions.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.PromotionToStoreEntity;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.ConnectionPromoVo;
import com.otsi.retail.promotions.vo.ProductTextileVo;
import com.otsi.retail.promotions.vo.PromotionToStoreVo;
import com.otsi.retail.promotions.vo.PromotionsVo;
import com.otsi.retail.promotions.vo.ReportVo;
import com.otsi.retail.promotions.vo.SearchPromotionsVo;
import com.otsi.retail.promotions.vo.StoreVo;

@Component
public interface PromotionService {

	String addPromotion(PromotionsVo vo);

	ConnectionPromoVo getListOfPromotions(String flag, Long domainId);

	String editPromotion(PromotionsVo vo);
	
	String deletePromotion(Long id);

	List<StoreVo> getAllStores();

	//String addPromotionToStore(PromotionsVo vo);
	String addPromotionToStore(PromotionToStoreVo vo);

	List<SearchPromotionsVo> searchPromotion(SearchPromotionsVo vo);

	String updatePromotionDates(SearchPromotionsVo vo);
	
	String clonePromotionByStore(SearchPromotionsVo vo);
	
	List<SearchPromotionsVo> listOfPromotionsBySearch(SearchPromotionsVo svo);
	
	String saveBenfit(BenefitVo vo);
	
	List<ReportVo> activeVSinactivePromos();

	List<ProductTextileVo> checkPromtion(List<ProductTextileVo> listofInvTxt, Long storeId, Long domainId);

	String updatePriority(SearchPromotionsVo vo);
	
	List<SearchPromotionsVo> searchPromotionByStoreName(SearchPromotionsVo vo);
	
	List<PromotionToStoreEntity> getAllStorePromotions();
		
}
