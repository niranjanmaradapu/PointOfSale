package com.otsi.retail.promotions.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.vo.PoolVo;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.SearchPoolVo;

/**
 * This interface is implemented by PoolServiceImpl
 * 
 * @author Manikanta Guptha
 *
 */
@Component
public interface PoolService {

	String savePool(PromotionPoolVo vo);

	PoolVo getListOfPools(String isActive, Long domainId, Long clientId);

	String modifyPool(PromotionPoolVo vo);

	String deletePool(Long poolId);

	List<PromotionPoolVo> searchPool(SearchPoolVo pvo);

	String poolExistsCreateRules(PromotionPoolVo vo);

}
