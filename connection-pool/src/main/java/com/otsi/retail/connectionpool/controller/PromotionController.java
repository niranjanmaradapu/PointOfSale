package com.otsi.retail.connectionpool.controller;

import java.util.List;
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
@RequestMapping(CommonRequestMappigs.PROMO)
public class PromotionController {

	@Autowired
	private PromotionService promoService;

	// Method for adding promotion to pools
	@PostMapping(CommonRequestMappigs.ADD_PROMO)
	public GateWayResponse<?> addPromotion(@RequestBody PromotionsVo vo) {

		try {
			String savePromo = promoService.addPromotion(vo);
			return new GateWayResponse<>(HttpStatus.OK, savePromo, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e, "");
		}
	}

	// Method for getting list of all promotions based on their status
	@GetMapping(CommonRequestMappigs.GET_PROMO_LIST)
	public GateWayResponse<?> listOfPromotions(@NotNull @RequestParam String flag) {

		try {
			List<PromotionsVo> promoList = promoService.getListOfPromotions(flag);
			return new GateWayResponse<>(HttpStatus.OK, promoList, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e, "");
		}
	}

	// Method for modifying/editing Promotion
	@PostMapping(CommonRequestMappigs.EDIT_PROMO)
	public GateWayResponse<?> editPromotion(@RequestBody PromotionsVo vo) {

		try {
			String result = promoService.editPromotion(vo);
			return new GateWayResponse<>(HttpStatus.OK, result, "");

		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e, "");
		}
	}
}
