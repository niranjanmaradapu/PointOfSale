package com.otsi.retail.connectionpool.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.vo.PromotionsVo;

@Component
public interface PromotionService {

	String addPromotion(PromotionsVo vo) throws Exception;

	List<PromotionsVo> getListOfPromotions(String flag) throws Exception;

	String editPromotion(PromotionsVo vo) throws Exception;

}
