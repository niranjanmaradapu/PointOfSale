package com.otsi.retail.promotions.controller;

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

import com.otsi.retail.promotions.calculate.benefits.CalculateBenifits;
import com.otsi.retail.promotions.common.CommonRequestMappigs;
import com.otsi.retail.promotions.entity.ColumnNameAndOperators;
import com.otsi.retail.promotions.entity.PromotionToStoreEntity;
import com.otsi.retail.promotions.gatewayresponse.GateWayResponse;
import com.otsi.retail.promotions.service.ColumnNameAndOperatorService;
import com.otsi.retail.promotions.service.PromotionService;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.ColumnNameAndOperatorsVo;
import com.otsi.retail.promotions.vo.ConnectionPromoVo;
import com.otsi.retail.promotions.vo.LineItemVo;
import com.otsi.retail.promotions.vo.ProductTextileVo;
import com.otsi.retail.promotions.vo.PromotionToStoreVo;
import com.otsi.retail.promotions.vo.PromotionsVo;
import com.otsi.retail.promotions.vo.ReportVo;
import com.otsi.retail.promotions.vo.SearchPromotionsVo;
import com.otsi.retail.promotions.vo.StoreVo;
import com.sun.istack.NotNull;

import io.swagger.annotations.ApiOperation;

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
	
	@Autowired
	private CalculateBenifits calculateBenifits;

	@Autowired
	private ColumnNameAndOperatorService columnNameAndOperatorService;

	// Method for adding promotion to pools
	@PostMapping(CommonRequestMappigs.ADD_PROMO)
	public GateWayResponse<?> addPromotion(@RequestBody PromotionsVo vo) {
		log.info("Recieved request to addpromotion():" + vo);
		String savePromo = promoService.addPromotion(vo);
		return new GateWayResponse<>("added promotion successfully", savePromo);

	}

	// Method for getting list of all promotions based on their status
	@GetMapping(CommonRequestMappigs.GET_PROMO_LIST)
	public GateWayResponse<?> listOfPromotions(@NotNull @RequestParam String flag, Long domainId,
			                                   Long clientId, Long storeId) {
		log.info("Recieved request to listOfPromotions():" + flag);
		ConnectionPromoVo promoVo = promoService.getListOfPromotions(flag, domainId, clientId, storeId);
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

//	@PostMapping(CommonRequestMappigs.ADD_PROMO_STORE)
//	public GateWayResponse<?> addPromotionToStore(@RequestBody PromotionsVo vo) {
//		log.info("Recieved request to addPromotionToStore():" + vo);
//		String result = promoService.addPromotionToStore(vo);
//		return new GateWayResponse<>("promotion mapped to store successfully", result);
//
//	}

	@PostMapping(CommonRequestMappigs.ADD_PROMO_STORE)
	public GateWayResponse<?> addPromotionToStore(@RequestBody PromotionToStoreVo vos) {
		log.info("Recieved request to addPromotionToStore():" + vos);
		String result = promoService.addPromotionToStore(vos);
		return new GateWayResponse<>("promotion mapped to store successfully", result);

	}

	@PostMapping(CommonRequestMappigs.STORE_LEVEL_PROMOTIONS_SEARCHING)
	public GateWayResponse<?> storeLevelPromoSearching(@RequestBody SearchPromotionsVo vo) {
		log.info("Recieved request to searchPromotion():" + vo);
		List<SearchPromotionsVo> result = promoService.storeLevelPromoSearching(vo);
		return new GateWayResponse<>("successfully getting promotions", result);

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

	// this api wrote for reports
	@PostMapping(CommonRequestMappigs.LIST_OF_PROMOTIONS_BY_SEARCH_CRITERIA)
	public GateWayResponse<?> listOfPromotions(@RequestBody SearchPromotionsVo vo) {

		List<SearchPromotionsVo> result = promoService.listOfPromotionsBySearch(vo);
		return new GateWayResponse<>("successfully getting promotions", result);

	}

	@PostMapping(CommonRequestMappigs.ADD_BENFIT)
	public GateWayResponse<?> saveBenfit(@RequestBody BenefitVo vo) {
		log.info("Recieved request to addBenfit():" + vo);
		String saveBenfit = promoService.saveBenfit(vo);
		return new GateWayResponse<>("added benfit successfully", saveBenfit);

	}

	@GetMapping(CommonRequestMappigs.ACTIVEVSINACTIVEPROMOS)
	public GateWayResponse<?> activeVSinactivePromos() {
		log.info("Recieved request to activeVSinactivePromos()");
		List<ReportVo> vo = promoService.activeVSinactivePromos();
		return new GateWayResponse<>("", vo);

	}

	@PostMapping("/checkPromtionTextile")
	public GateWayResponse<?> checkPromtionTextile(@RequestBody List<ProductTextileVo> listofInvTxt,
			@RequestParam Long storeId, @RequestParam Long domainId) {
		return new GateWayResponse<>("", promoService.checkPromtion(listofInvTxt, storeId, domainId));
	}

	// check the flags for the both promos
	@PostMapping("/checkPromtion")
	public GateWayResponse<?> checkPromotion(@RequestBody List<ProductTextileVo> listofInvTxt,
			@RequestParam Long storeId, @RequestParam Long domainId, boolean checkPromoSwitch) {
		
		
		// barcode level check promotion for textile
		List<LineItemVo> listOfLineItems = calculateBenifits.convertProductTextileIntoLineItems(listofInvTxt);
		
		if (checkPromoSwitch) {

			GateWayResponse<?> barcodeLevelCheckPromoForTxtile = checkPromtionTextile(listofInvTxt, storeId, domainId);

		} else {

			GateWayResponse<?> invoiceLevelCheckPromoForTxtile = invoiceLevelCheckPromtionTextile(listOfLineItems , storeId,
					domainId);
		}

		return new GateWayResponse<>();
	}

	@PostMapping("/invoiceLevelCheckPromtionTextile")
	public GateWayResponse<?> invoiceLevelCheckPromtionTextile(@RequestBody List<LineItemVo> listofLineItemsTxt,
			@RequestParam Long storeId, @RequestParam Long domainId) {
		return new GateWayResponse<>("", promoService.checkInvoiceLevelPromtion(listofLineItemsTxt, storeId, domainId));
	}

	@PutMapping("/updatePriority")
	public GateWayResponse<?> updatePriority(@RequestBody SearchPromotionsVo vo) {
		return new GateWayResponse<>("priority updated successfully", promoService.updatePriority(vo));
	}

	@PostMapping("/searchPromoByStoreName")
	public GateWayResponse<?> searchPromoByStoreName(@RequestBody SearchPromotionsVo vo) {
		return new GateWayResponse<>("", promoService.searchPromotionByStoreName(vo));
	}

	@GetMapping("/getAllStorePromos")
	public GateWayResponse<?> getAllPromoStores() {
		// log.info("Recieved request to getByStoreId():" + storeId);
		List<PromotionToStoreEntity> getAllStorePromos = promoService.getAllStorePromotions();
		return new GateWayResponse<>("fetching store promotions successfully", getAllStorePromos);

	}

	@PostMapping(CommonRequestMappigs.PROMOTIONS_SEARCHING)
	public GateWayResponse<?> promotionSearching(@RequestBody PromotionsVo vo) {
		log.info("Recieved request to searchPromotion():" + vo);
		List<PromotionsVo> result = promoService.promotionSearching(vo);
		return new GateWayResponse<>("successfully getting promotions", result);

	}

	@PostMapping("/save")
	public GateWayResponse<?> saveColumnNameAndOperatorsVo(
			@RequestBody ColumnNameAndOperatorsVo columnNameAndOperatorsVo) {
		log.info("Recieved request to columnNamesAndOperator():" + columnNameAndOperatorsVo);

		ColumnNameAndOperatorsVo saveColumnNameAndOperator = columnNameAndOperatorService
				.save(columnNameAndOperatorsVo);

		return new GateWayResponse<>("SuccessFully Added ColumnNames data", saveColumnNameAndOperator);

	}

	// deletebyId .getbyOperation/
	@ApiOperation(value = "Delete columnNameOperators")
	@DeleteMapping("/{id}")
	public GateWayResponse<?> deleteColumnNameId(@RequestParam Long id) {
		log.info("Recieved request delete ColumnNameById():" + id);
		String deleteColumnNameId = columnNameAndOperatorService.deleteColumnNameId(id);
		return new GateWayResponse<>("ColumNameById deleted successfully", deleteColumnNameId);

	}

	@GetMapping("/allcolumnnames")
	public GateWayResponse<?> getListofColumnNames() {
		log.info("Reterive All Records");
		List<ColumnNameAndOperators> listofColumnNames = columnNameAndOperatorService.getListofColumnNames();

		return new GateWayResponse<>("Reterive All Records", listofColumnNames);

	}

	@GetMapping(value = "/anyMatchingData")
	public GateWayResponse<?> getAnyMatchingData(@RequestParam(required = false) String columnName,
			@RequestParam(required = false) String operator) {
		List<ColumnNameAndOperators> anyMatchingData = columnNameAndOperatorService.getAnyMatchingData(columnName,
				operator);
		System.out.println("anyMatchingData" + anyMatchingData);
		return new GateWayResponse<>("Reterived matching data", anyMatchingData);
	}

}
