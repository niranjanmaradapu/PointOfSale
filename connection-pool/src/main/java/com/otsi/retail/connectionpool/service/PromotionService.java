package com.otsi.retail.connectionpool.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.vo.PromotionsVo;

@Component
public interface PromotionService {

	ResponseEntity<?> addPromotion(PromotionsVo vo);

	ResponseEntity<?> getListOfPromotions(String flag);

	ResponseEntity<?> editPromotion(PromotionsVo vo);

}
