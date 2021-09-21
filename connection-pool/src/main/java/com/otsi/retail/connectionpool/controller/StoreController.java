/**
 * 
 */
package com.otsi.retail.connectionpool.controller;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	private Logger log = LoggerFactory.getLogger(StoreController.class);

	@Autowired
	private StoresService storeService;

	@PostMapping(CommonRequestMappigs.ADD_STORE)
	public GateWayResponse<?> addStore(@RequestBody StoreVo vo) {
		log.info("Recieved request to addStore():" + vo);
		String saveStore = storeService.addStore(vo);
		return new GateWayResponse<>(HttpStatus.OK, saveStore, "");

	}

	@GetMapping(CommonRequestMappigs.GET_STORE_BY_ID)
	public GateWayResponse<?> getByStoreId(@RequestParam Long storeId) {
		log.info("Recieved request to getByStoreId():" + storeId);
		Optional<StoresEntity> getAllStore = storeService.getByStoreId(storeId);
		return new GateWayResponse<>(HttpStatus.OK, getAllStore, "");

	}

	@GetMapping(CommonRequestMappigs.GET_STORE_BY_NAME)
	public GateWayResponse<?> getStoreDetailsByStoreName(@RequestParam String storeName) {
		log.info("Recieved request to getStoreDetailsByStoreName():" + storeName);
		try {
			Optional<StoresEntity> stores = storeService.findStoreByName(storeName);
			return new GateWayResponse<>(HttpStatus.OK, stores, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}

	}

}
