package com.otsi.retail.connectionpool.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.otsi.retail.connectionpool.vo.BenfitVo;
import com.otsi.retail.connectionpool.vo.ConnectionPromoVo;
import com.otsi.retail.connectionpool.vo.LineItemVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.ReportVo;
import com.otsi.retail.connectionpool.vo.SearchPromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;
import com.sun.istack.NotNull;

/**
 * This Controller class contains Promotions related API's
 * 
 * @author Manikanta Guptha
 *
 */

@RestController
@Transactional
@RequestMapping(CommonRequestMappigs.PROMO)
public class PromotionController {

	private Logger log = LogManager.getLogger(PromotionController.class);

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
	public GateWayResponse<?> listOfPromotions(@NotNull @RequestParam String flag, Long domainId) {
		log.info("Recieved request to listOfPromotions():" + flag);
		ConnectionPromoVo promoVo = promoService.getListOfPromotions(flag,domainId);
		return new GateWayResponse<>("fetching list of promotions successfully", promoVo);

	}

	// Method for modifying/editing Promotion
	@PutMapping(CommonRequestMappigs.EDIT_PROMO)
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
	public GateWayResponse<?> searchPromotion(@RequestBody SearchPromotionsVo vo) {
		log.info("Recieved request to searchPromotion():" + vo);
		List<SearchPromotionsVo> result = promoService.searchPromotion(vo);
		return new GateWayResponse<>("successfully getting promotions", result);

	}

	@PostMapping(CommonRequestMappigs.SEARCH_BY_STORE)
	public GateWayResponse<?> searchByStore(@RequestBody SearchPromotionsVo vo) {
		log.info("Recieved request to searchByStore():" + vo);
		List<SearchPromotionsVo> result = promoService.searchByStore(vo);
		return new GateWayResponse<>("  successfully getting store mapped promotions", result);

	}

	@PutMapping(CommonRequestMappigs.UPDATE_PRIORITY)
	public GateWayResponse<?> updatePrioriy(@RequestBody SearchPromotionsVo vo) {
		log.info("Recieved request to updatePriority():" + vo);
		String result = promoService.updatePriority(vo);
		return new GateWayResponse<>(" priority updated successfully", result);

	}

	@PutMapping(CommonRequestMappigs.UPDATE_PROMO_DATES)
	public GateWayResponse<?> updatePromoDates(@RequestBody SearchPromotionsVo vo) {
		log.info("Recieved request to updatePromoDates():" + vo);
		String result = promoService.updatePromotionDates(vo);
		return new GateWayResponse<>(" promotion dates updated successfully", result);

	}

	@PostMapping(CommonRequestMappigs.CLONE_PROMO_BY_STORE)
	public GateWayResponse<?> clonePromotionByStore(@RequestBody SearchPromotionsVo vo) {
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
	
	@PostMapping(CommonRequestMappigs.LIST_OF_PROMOTIONS_BY_SEARCH_CRITERIA)
	public GateWayResponse<?> listOfPromotions(@RequestBody SearchPromotionsVo vo) {
		
		List<SearchPromotionsVo> result = promoService.listOfPromotionsBySearch(vo);
		return new GateWayResponse<>("successfully getting promotions", result);

	}
	
	@PostMapping(CommonRequestMappigs.ADD_BENFIT)
	public GateWayResponse<?> saveBenfit(@RequestBody BenfitVo vo) {
		log.info("Recieved request to addBenfit():" + vo);
		String saveBenfit = promoService.saveBenfit(vo);
		return new GateWayResponse<>("added benfit successfully", saveBenfit);

	}
	
	@GetMapping(CommonRequestMappigs.ACTIVEVSINACTIVEPROMOS)
	
	public GateWayResponse<?>activeVSinactivePromos(){
		log.info("Recieved request to activeVSinactivePromos()");
		List<ReportVo> vo = promoService.activeVSinactivePromos();
		return new GateWayResponse<>("", vo);
		
	}

}
