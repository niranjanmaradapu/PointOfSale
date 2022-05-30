package com.otsi.retail.promotions.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.promotions.common.AppConstants;
import com.otsi.retail.promotions.common.CommonRequestMappigs;
import com.otsi.retail.promotions.gatewayresponse.GateWayResponse;
import com.otsi.retail.promotions.service.PoolService;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.PoolVo;
import com.otsi.retail.promotions.vo.SearchPoolVo;

/**
 * This Controller class is for Connection pool related API's
 * 
 * @author Manikanta Guptha
 *
 */
@RestController
@RequestMapping(CommonRequestMappigs.POOL)
public class PoolController {

	private Logger log = LogManager.getLogger(PoolController.class);

	@Autowired
	private PoolService poolService;

	// Method for creating pool
	@PostMapping(CommonRequestMappigs.CREATE_POOL)
	public GateWayResponse<?> saveNewPool(@RequestBody PromotionPoolVo vo) {
		log.info("Recieved request to saveNewPool():" + vo);
		String savePool = poolService.savePool(vo);
		return new GateWayResponse<>(AppConstants.POOL_SAVE, savePool);

	}
	
	@PostMapping("/poolExistsCreateRules")
	public GateWayResponse<?> poolExistsCreateRules(@RequestBody PromotionPoolVo vo) {
		log.info("Recieved request to saveNewPool():" + vo);
		String savePool = poolService.poolExistsCreateRules(vo);
		return new GateWayResponse<>(AppConstants.POOL_SAVE, savePool);

	}
	
	// Method for getting List of Pools from status flag
	@GetMapping(CommonRequestMappigs.GET_POOL_LIST)
	public GateWayResponse<?> getListOfPools(@RequestParam String isActive,Long domainId, 
		   Long clientId) {
		log.info("Recieved request to getListOfPools():" + isActive);
		PoolVo poolvo = poolService.getListOfPools(isActive,domainId,clientId);
		return new GateWayResponse<>(AppConstants.GET_POOL, poolvo);

	}

	// Method for modify/editing the Existing Pool and Rules
	@PostMapping(CommonRequestMappigs.MODIFY_POOL)
	public GateWayResponse<?> modifyPool(@RequestBody PromotionPoolVo vo) {
		log.info("Recieved request to modifyPool():" + vo);
		String message = poolService.modifyPool(vo);
		return new GateWayResponse<>(AppConstants.MODIFY_POOL, message);

	}

	// Method for delete the pool
	@DeleteMapping(CommonRequestMappigs.DELETE_POOL)
	public GateWayResponse<?> deletePool(@RequestParam Long poolId) {
		log.info("Recieved request to modifyPool():" + poolId);
		String message = poolService.deletePool(poolId);
		return new GateWayResponse<>(AppConstants.DELETE_POOL, message);

	}

	// Method for pool searching
	@PostMapping(CommonRequestMappigs.SEARCH_POOLS)
	public GateWayResponse<?> searchPool(@RequestBody SearchPoolVo pvo) {

		List<PromotionPoolVo> vo = poolService.searchPool(pvo);

		return new GateWayResponse<>("successfully getting pools", vo);

	}
		
}
