package com.otsi.retail.connectionpool.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.vo.BenfitVo;
import com.otsi.retail.connectionpool.vo.ConnectionPromoVo;
import com.otsi.retail.connectionpool.vo.LineItemVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;
import com.otsi.retail.connectionpool.vo.SearchPromotionsVo;

@Component
public interface PromotionService {

	String addPromotion(PromotionsVo vo);

	ConnectionPromoVo getListOfPromotions(String flag, Long domainId);

	String editPromotion(PromotionsVo vo);
	
	String deletePromotion(Long id);

	List<StoreVo> getAllStores();

	String addPromotionToStore(PromotionsVo vo);

	List<SearchPromotionsVo> searchPromotion(SearchPromotionsVo vo);

	List<SearchPromotionsVo> searchByStore(SearchPromotionsVo vo);
	
	String updatePriority(SearchPromotionsVo vo);
	
	String updatePromotionDates(SearchPromotionsVo vo);
	
	String clonePromotionByStore(SearchPromotionsVo vo);

	String addPromtionToBarcode(Long promoId, String barcode);

	List<LineItemVo> checkPromtion(List<LineItemVo> lineItmes);
	
	List<SearchPromotionsVo> listOfPromotionsBySearch(SearchPromotionsVo svo);
	
	String saveBenfit(BenfitVo vo);

	
}
