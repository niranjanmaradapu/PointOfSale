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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.connectionpool.common.CommonRequestMappigs;
import com.otsi.retail.connectionpool.gatewayresponse.GateWayResponse;
import com.otsi.retail.connectionpool.service.PromotionService;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
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
}
