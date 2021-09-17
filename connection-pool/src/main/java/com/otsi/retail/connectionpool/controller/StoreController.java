/**
 * 
 */
package com.otsi.retail.connectionpool.controller;

import java.util.Optional;

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
import com.otsi.retail.connectionpool.entity.StoresEntity;
import com.otsi.retail.connectionpool.gatewayresponse.GateWayResponse;
import com.otsi.retail.connectionpool.service.StoresService;
import com.otsi.retail.connectionpool.vo.StoreVo;

/**
 * @author Sudheer.Swamy
 *
 */

@RestController
@CrossOrigin
@RequestMapping(CommonRequestMappigs.STORE)
public class StoreController {

	@Autowired
	private StoresService storeService;

	@PostMapping(CommonRequestMappigs.ADD_STORE)
	public GateWayResponse<?> addStore(@RequestBody StoreVo vo) {

		try {

			String saveStore = storeService.addStore(vo);
			return new GateWayResponse<>(HttpStatus.OK, saveStore, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e, "");
		}
	}

	@GetMapping(CommonRequestMappigs.GET_STORE_BY_ID)
	public GateWayResponse<?> getByStoreId(@RequestParam Long storeId) {

		try {

			Optional<StoresEntity> getAllStore = storeService.getByStoreId(storeId);
			return new GateWayResponse<>(HttpStatus.OK, getAllStore, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e, "");
		}
	}

	@GetMapping(CommonRequestMappigs.GET_STORE_BY_NAME)
	public GateWayResponse<?> getStoreDetailsByStoreName(@RequestParam String storeName) {

		try {
			Optional<StoresEntity> stores = storeService.findStoreByName(storeName);
			return new GateWayResponse<>(HttpStatus.OK, stores, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

}
