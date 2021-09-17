package com.otsi.retail.connectionpool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

/**
 * This Controller class is for Connection pool related API's
 * 
 * @author Manikanta Guptha
 *
 */
@RestController
@CrossOrigin
@RequestMapping(CommonRequestMappigs.POOL)
public class PoolController {

	@Autowired
	private PoolService poolService;

	// Method for creating pool
	@PostMapping(CommonRequestMappigs.CREATE_POOL)
	public GateWayResponse<?> saveNewPool(@RequestBody ConnectionPoolVo vo) {

		try {

			String savePool = poolService.savePool(vo);
			return new GateWayResponse<>( HttpStatus.OK,savePool,"");

		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

	// Method for getting List of Pools from status flag
	@GetMapping(CommonRequestMappigs.GET_POOL_LIST)
	public GateWayResponse<?> getListOfPools(@RequestParam String isActive) {

		try {

			List<ConnectionPoolVo> vo = poolService.getListOfPools(isActive);
			return new GateWayResponse<>( HttpStatus.OK,vo,"");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST,"Getting error while fetching the Data");
		}
	}

	// Method for modify/editing the Existing Pool and Rules
	@PostMapping(CommonRequestMappigs.MODIFY_POOL)
	public GateWayResponse<?> modifyPool(@RequestBody ConnectionPoolVo vo) {

		try {
			String message = poolService.modifyPool(vo);
			return new GateWayResponse<>( HttpStatus.OK,message,"");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST,"Exception occurs while modifying the record.");
		}
	}
}
