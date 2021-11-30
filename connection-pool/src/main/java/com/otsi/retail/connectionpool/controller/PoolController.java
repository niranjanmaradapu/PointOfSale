package com.otsi.retail.connectionpool.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.connectionpool.common.CommonRequestMappigs;
import com.otsi.retail.connectionpool.gatewayresponse.GateWayResponse;
import com.otsi.retail.connectionpool.service.PoolService;
import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.SearchPoolVo;

/**
 * This Controller class is for Connection pool related API's
 * 
 * @author Manikanta Guptha
 *
 */
@RestController
@RequestMapping(CommonRequestMappigs.POOL)
public class PoolController {

	private Logger log = LoggerFactory.getLogger(PoolController.class);

	@Autowired
	private PoolService poolService;

	// Method for creating pool
	@PostMapping(CommonRequestMappigs.CREATE_POOL)
	public GateWayResponse<?> saveNewPool(@RequestBody ConnectionPoolVo vo) {
		log.info("Recieved request to saveNewPool():" + vo);
		String savePool = poolService.savePool(vo);
		return new GateWayResponse<>("saved pool successfully", savePool);

	}

	// Method for getting List of Pools from status flag
	@GetMapping(CommonRequestMappigs.GET_POOL_LIST)
	public GateWayResponse<?> getListOfPools(@RequestParam String isActive) {
		log.info("Recieved request to getListOfPools():" + isActive);
		List<ConnectionPoolVo> vo = poolService.getListOfPools(isActive);
		return new GateWayResponse<>("fetching list of pools successfully", vo);

	}

	// Method for modify/editing the Existing Pool and Rules
	@PostMapping(CommonRequestMappigs.MODIFY_POOL)
	public GateWayResponse<?> modifyPool(@RequestBody ConnectionPoolVo vo) {
		log.info("Recieved request to modifyPool():" + vo);
		String message = poolService.modifyPool(vo);
		return new GateWayResponse<>("updated pool successfully", message);

	}

	// Method for delete the pool
	@DeleteMapping(CommonRequestMappigs.DELETE_POOL)
	public GateWayResponse<?> deletePool(@RequestParam Long poolId) {
		log.info("Recieved request to modifyPool():" + poolId);
		String message = poolService.deletePool(poolId);
		return new GateWayResponse<>("deleted pool successfully", message);

	}

	// Method for pool searching
	@PostMapping(CommonRequestMappigs.SEARCH_POOLS)
	public GateWayResponse<?> searchPool(@RequestBody SearchPoolVo pvo) {

		List<SearchPoolVo> vo = poolService.searchPool(pvo);

		return new GateWayResponse<>("successfully getting pools", vo);

	}
}
