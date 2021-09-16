package com.otsi.retail.userStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.userStore.gatewayresponse.GateWayResponse;
import com.otsi.retail.userStore.model.StoreModel;
import com.otsi.retail.userStore.service.StoreService;
import com.otsi.retail.userStore.vo.StoreVO;
import com.otsi.retail.userStore.vo.UserVo;


@RestController
@RequestMapping("/stores")
public class StoreController {
	@Autowired
	private StoreService storeService;
	
    //create new store and save the details into DB
	@PostMapping("/store")
	public GateWayResponse<?> addStore(@RequestBody StoreModel storeModel) {
		try {
			String message= storeService.save(storeModel);
			return new GateWayResponse<>(HttpStatus.OK, message, "Success");
		}catch (Exception ex) {
			return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}


	}
   //get the store details  by using storename
	@GetMapping("/store/storename")
	public GateWayResponse<?> getStoreByName(@RequestParam String storeName) {
		try {
			StoreVO vo=storeService.findByName(storeName);
			return new GateWayResponse<>(HttpStatus.OK, vo, "Success");
			}catch (Exception ex) {
				return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
			}

	}
    //delete the store information  by using id
	@DeleteMapping("/deletestore")
	public GateWayResponse<Object> deleteStoreById(@RequestParam Long id) {
		try {
			String message=storeService.deleteById(id);
			return new GateWayResponse<>(HttpStatus.OK, message, "Success");
			}catch (Exception ex) {
				return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
			}


	}
	//get all the stores
	@RequestMapping("/getstores")
	public GateWayResponse<?> getAllStores() {
		try {
			List<StoreVO> vo=storeService.getAllStores();
			return new GateWayResponse<>(HttpStatus.OK, vo, "Success");
			}catch (Exception ex) {
				return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
			}


	}


}
