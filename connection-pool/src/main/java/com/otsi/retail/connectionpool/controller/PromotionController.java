package com.otsi.retail.connectionpool.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.connectionpool.common.CommonRequestMappigs;
import com.otsi.retail.connectionpool.gatewayresponse.GateWayResponse;
import com.otsi.retail.connectionpool.service.PromotionService;
import com.otsi.retail.connectionpool.service.PromotionServiceImpl;
import com.otsi.retail.connectionpool.vo.LineItemVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;
import com.otsi.retail.connectionpool.vo.searchPromotionsVo;
import com.sun.istack.NotNull;

/**
 * This Controller class contains Promotions related API's
 * 
 * @author Manikanta Guptha
 *
 */

@RestController
@CrossOrigin
@Transactional
@RequestMapping(CommonRequestMappigs.PROMO)
public class PromotionController {

	private Logger log = LoggerFactory.getLogger(PromotionController.class);

	@Autowired
	private PromotionService promoService;

	// Method for adding promotion to pools
	@PostMapping(CommonRequestMappigs.ADD_PROMO)
	public GateWayResponse<?> addPromotion(@RequestBody PromotionsVo vo) {
		log.info("Recieved request to addpromotion():" + vo);
		String savePromo = promoService.addPromotion(vo);
		return new GateWayResponse<>("added promotion successfully", savePromo);

	}

	// Method for getting list of all promotions based on their status
	@GetMapping(CommonRequestMappigs.GET_PROMO_LIST)
	public GateWayResponse<?> listOfPromotions(@NotNull @RequestParam String flag) {
		log.info("Recieved request to listOfPromotions():" + flag);
		List<PromotionsVo> promoList = promoService.getListOfPromotions(flag);
		return new GateWayResponse<>("fetching list of promotions successfully", promoList);

	}

	// Method for modifying/editing Promotion
	@PostMapping(CommonRequestMappigs.EDIT_PROMO)
	public GateWayResponse<?> editPromotion(@RequestBody PromotionsVo vo) {
		log.info("Recieved request to editPromotion():" + vo);
		String result = promoService.editPromotion(vo);
		return new GateWayResponse<>("updated promotion successfully", result);

	}

	// Method for delete Promotion
	@DeleteMapping(CommonRequestMappigs.DELETE_PROMO)
	public GateWayResponse<?> deletePromotion(@RequestParam Long id) {
		log.info("Recieved request deletePromotion():" + id);
		String result = promoService.deletePromotion(id);
		return new GateWayResponse<>("promotion deleted successfully", result);

	}

	@GetMapping(CommonRequestMappigs.GET_ALL_STORES)
	public GateWayResponse<?> getAllStores() {
		// log.info("Recieved request to getByStoreId():" + storeId);
		List<StoreVo> getAllStore = promoService.getAllStores();
		return new GateWayResponse<>("fetching stores successfully", getAllStore);

	}

	@PostMapping(CommonRequestMappigs.ADD_PROMO_STORE)
	public GateWayResponse<?> addPromotionToStore(@RequestBody PromotionsVo vo) {
		log.info("Recieved request to addPromotionToStore():" + vo);
		String result = promoService.addPromotionToStore(vo);
		return new GateWayResponse<>("promotion mapped to store successfully", result);

	}

	@PostMapping(CommonRequestMappigs.SEARCH_PROMOTION)
	public GateWayResponse<?> searchPromotion(@RequestBody searchPromotionsVo vo) {
		log.info("Recieved request to searchPromotion():" + vo);
		List<searchPromotionsVo> result = promoService.searchPromotion(vo);
		return new GateWayResponse<>("successfully getting promotions", result);

	}

	@PostMapping(CommonRequestMappigs.SEARCH_BY_STORE)
	public GateWayResponse<?> searchByStore(@RequestBody searchPromotionsVo vo) {
		log.info("Recieved request to searchByStore():" + vo);
		List<searchPromotionsVo> result = promoService.searchByStore(vo);
		return new GateWayResponse<>("  successfully getting store mapped promotions", result);

	}

	@PutMapping(CommonRequestMappigs.UPDATE_PRIORITY)
	public GateWayResponse<?> updatePrioriy(@RequestBody searchPromotionsVo vo) {
		log.info("Recieved request to updatePriority():" + vo);
		String result = promoService.updatePriority(vo);
		return new GateWayResponse<>(" priority updated successfully", result);

	}

	@PutMapping(CommonRequestMappigs.UPDATE_PROMO_DATES)
	public GateWayResponse<?> updatePromoDates(@RequestBody searchPromotionsVo vo) {
		log.info("Recieved request to updatePromoDates():" + vo);
		String result = promoService.updatePromotionDates(vo);
		return new GateWayResponse<>(" promotion dates updated successfully", result);

	}

	@PostMapping(CommonRequestMappigs.CLONE_PROMO_BY_STORE)
	public GateWayResponse<?> clonePromotionByStore(@RequestBody searchPromotionsVo vo) {
		log.info("Recieved request clonePromotionByStore():" + vo);
		String result = promoService.clonePromotionByStore(vo);
		return new GateWayResponse<>(" promotion cloned successfully", result);

	}

	@PostMapping("addpromotoproduct")
	public GateWayResponse<?> addPromtionToBarcode(@RequestParam Long promoId, @RequestParam String barcode) {

		String result = promoService.addPromtionToBarcode(promoId, barcode);

		return new GateWayResponse<>("", result);

	}

	@PostMapping("/checkPromtion")
	public GateWayResponse<?> checkPromtion(@RequestBody List<LineItemVo> lineItmes) {

		PromotionServiceImpl.i=0;
		
		List<LineItemVo> result = promoService.checkPromtion(lineItmes);

		return new GateWayResponse<>("", result);

	}

}
