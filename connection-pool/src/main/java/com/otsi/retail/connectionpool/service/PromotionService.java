package com.otsi.retail.connectionpool.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.vo.LineItemVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;
import com.otsi.retail.connectionpool.vo.searchPromotionsVo;

@Component
public interface PromotionService {

	String addPromotion(PromotionsVo vo);

	List<PromotionsVo> getListOfPromotions(String flag);

	String editPromotion(PromotionsVo vo);
	
	String deletePromotion(Long id);

	List<StoreVo> getAllStores();

	String addPromotionToStore(PromotionsVo vo);

	List<searchPromotionsVo> searchPromotion(searchPromotionsVo vo);

	List<searchPromotionsVo> searchByStore(searchPromotionsVo vo);
	
	String updatePriority(searchPromotionsVo vo);
	
	String updatePromotionDates(searchPromotionsVo vo);
	
	String clonePromotionByStore(searchPromotionsVo vo);

	String addPromtionToBarcode(Long promoId, String barcode);

	List<LineItemVo> checkPromtion(List<LineItemVo> lineItmes);

	
}
