package com.otsi.retail.promotions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.promotions.calculate.benefits.CalculateBenifits;
import com.otsi.retail.promotions.check.pools.CheckPoolRules;
import com.otsi.retail.promotions.common.Applicability;
import com.otsi.retail.promotions.common.PromoApplyType;
import com.otsi.retail.promotions.config.Config;
import com.otsi.retail.promotions.entity.BenfitEntity;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.PromotionSlabsEntity;
import com.otsi.retail.promotions.entity.PromotionToStoreEntity;
import com.otsi.retail.promotions.entity.PromotionsEntity;
import com.otsi.retail.promotions.exceptions.DuplicateRecordException;
import com.otsi.retail.promotions.exceptions.InvalidDataException;
import com.otsi.retail.promotions.exceptions.RecordNotFoundException;
import com.otsi.retail.promotions.gatewayresponse.GateWayResponse;
import com.otsi.retail.promotions.mapper.PromotionMapper;
import com.otsi.retail.promotions.repository.BenfitRepo;
import com.otsi.retail.promotions.repository.PoolRepo;
import com.otsi.retail.promotions.repository.PromotionRepo;
import com.otsi.retail.promotions.repository.PromotionToStoreRepo;
import com.otsi.retail.promotions.vo.BenefitVo;
import com.otsi.retail.promotions.vo.ConnectionPromoVo;
import com.otsi.retail.promotions.vo.LineItemVo;
import com.otsi.retail.promotions.vo.ProductVO;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.PromotionToStoreVo;
import com.otsi.retail.promotions.vo.PromotionsVo;
import com.otsi.retail.promotions.vo.ReportVo;
import com.otsi.retail.promotions.vo.SearchPromotionsVo;
import com.otsi.retail.promotions.vo.StoreVo;

/**
 * This class contains all Bussiness logics regarding promotions and their
 * management
 * 
 * @author Manikanta Guptha
 *
 */

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	private Logger log = LogManager.getLogger(PromotionServiceImpl.class);

	@Autowired
	private PromotionMapper promoMapper;

	@Autowired
	private PromotionRepo promoRepo;

	@Autowired
	private PoolRepo poolRepo;

	@Autowired
	private PromotionToStoreRepo promostoreRepo;

	@Autowired
	private BenfitRepo benfitRepo;

	@Autowired
	private Config config;

	@Autowired
	private RestTemplate template;

	@Autowired
	private CheckPoolRules checkPoolRules;

	@Autowired
	private CalculateBenifits calculateBenifits;

	// Method for adding promotion to pools
	@Override
	public String addPromotion(PromotionsVo vo) {
		log.debug("debugging addPromotion:" + vo);
		if (vo.getPoolVo() == null) {
			throw new InvalidDataException("please give valid data");
		}
		List<PromotionPoolVo> poolVo = vo.getPoolVo();

		List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

		List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

		// BenfitVo bvo = vo.getBenfitVo();

		PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList);

		PromotionsEntity promoList = null;
		if (poolVo.size() == poolList.size()) {

			promoList = promoRepo.save(entity);

		} else {
			log.error("Please give valid/active pools");
			throw new InvalidDataException("Please give valid/active pools");
		}
		log.warn("we are checking if pool is added...");
		log.info("Promotion mapped succesfully...");
		return "Promotion mapped succesfully with id: " + entity.getPromoId();
	}

	// Method for getting list of Promotions by using flag(Status)
	@Override
	public ConnectionPromoVo getListOfPromotions(String flag, Long clientId) {
		log.debug("debugging getListOfPromotions:" + flag);
		List<PromotionsEntity> promoList = new ArrayList<>();

		Boolean status = null;
		if (flag.equalsIgnoreCase("true")) {
			status = Boolean.TRUE;
		}
		if (flag.equalsIgnoreCase("false")) {
			status = Boolean.FALSE;
		}
		if (flag.equalsIgnoreCase("ALL") && clientId == null) {
			promoList = promoRepo.findAll();
		} else if (!(flag.isEmpty()) && clientId == null) {

			promoList = promoRepo.findByIsActive(status);
		} else if ((flag.isEmpty()) && clientId != null) {

			promoList = promoRepo.findByClientId(clientId);
		} else {
			promoList = promoRepo.findByIsActiveAndClientId(status, clientId);
		}

		if (!promoList.isEmpty()) {
			ConnectionPromoVo promoVo = new ConnectionPromoVo();
			List<PromotionsVo> listOfPromo = promoMapper.convertPromoEntityToVo(promoList);
			log.warn("we are checking if list of promotions is fetching...");
			log.info("list of promotions is fetching...");
			promoVo.setPromovo(listOfPromo);
			return promoVo;
		} else {
			log.error("record not found");
			throw new RecordNotFoundException("record not found");
		}

	}

	// Method for modifying/editing Promotion
	@Override
	public String editPromotion(PromotionsVo vo) {
		log.debug("debugging editPromotion:" + vo);
		if (vo.getPoolVo() == null) {
			throw new InvalidDataException("please enter valid data");
		}

		Optional<PromotionsEntity> promotion = promoRepo.findById(vo.getPromoId());

		if (promotion.isPresent()) {
			List<PromotionPoolVo> poolVo = vo.getPoolVo();

			List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

			List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

			if ((poolVo.size() == poolList.size())) {

				PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList);
				PromotionsEntity savedPromo = promoRepo.save(entity);

			} else {
				log.error("Please give valid/active pools");
				throw new InvalidDataException("Please give valid/active pools");
			}

			log.warn("we wre checking if pool is modified...");
			log.info("Promotion Modified succesfully");
			return "Promotion Modified succesfully";

		} else {
			log.error("please give valid Promotion Id");
			throw new RecordNotFoundException("please give valid Promotion Id");
		}

	}

	@Override
	public String deletePromotion(Long id) {

		Optional<PromotionsEntity> entity = promoRepo.findById(id);

		if (entity.isPresent()) {
			promoRepo.deleteByPromoId(id);
		} else {
			throw new RecordNotFoundException("Given promotion is not exists");
		}
		return "Promotion Deleted Successfully";
	}

	@Override
	public List<StoreVo> getAllStores() {
		List<StoreVo> vo = new ArrayList<StoreVo>();
		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetAllStoresFromURM(), HttpMethod.GET,
				null, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		vo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<StoreVo>>() {
		});

		return vo;
	}

	@Override
	public Page<SearchPromotionsVo> storeLevelPromoSearching(SearchPromotionsVo vo, Pageable pageable) {
		Page<PromotionToStoreEntity> promoDetails = null;

		if (vo.getStartDate() != null && vo.getEndDate() != null) {
			if (vo.getPromotionName() != null) {

				promoDetails = promostoreRepo.findByStartDateAndEndDateAndPromotionStatusAndClientId(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionStatus(), vo.getClientId(), pageable);
			} else if (vo.getPromotionName() == null) {
				promoDetails = promostoreRepo.findByStartDateAndEndDateAndPromotionStatusAndClientId(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionStatus(), vo.getClientId(), pageable);

			} else if (vo.getPromotionName() != null) {
				promoDetails = promostoreRepo.findByStartDateAndEndDateAndPromotionStatusAndClientId(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionStatus(), vo.getClientId(), pageable);

			} else if (vo.getStoreName() == null) {
				promoDetails = promostoreRepo.findByStartDateAndEndDateAndPromotionStatusAndClientId(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionStatus(), vo.getClientId(), pageable);

			} else if (vo.getStoreName() != null) {
				promoDetails = promostoreRepo.findByStartDateAndEndDateAndPromotionStatusAndClientId(vo.getStartDate(),
						vo.getEndDate(), vo.getPromotionStatus(), vo.getClientId(), pageable);

			}

			else {
				promoDetails = promostoreRepo.findByStartDateAndEndDateAndPromotionStatusAndClientId(

						vo.getStartDate(), vo.getEndDate(), vo.getPromotionStatus(), vo.getClientId(), pageable);
			}

		} else if (vo.getStartDate() == null && vo.getEndDate() == null) {
			if (vo.getPromotionName() != null) {
				promoDetails = promostoreRepo.findByPromotionNameAndClientId(vo.getPromotionName(), vo.getClientId(),
						pageable);

			} else if (vo.getStoreName() != null) {
				promoDetails = promostoreRepo.findByStoreNameAndClientId(vo.getStoreName(), vo.getClientId(), pageable);

			}

			else
				promoDetails = promostoreRepo.findByPromotionStatusAndClientId(vo.getPromotionStatus(),
						vo.getClientId(), pageable);
		}
		if (promoDetails.isEmpty()) {
			throw new RecordNotFoundException("No record found with given information");

		}

		else {
			return promoDetails.map(promo -> mapToVo(promo));

		}

	}

	private SearchPromotionsVo mapToVo(PromotionToStoreEntity promotions) {

		SearchPromotionsVo searchVo = new SearchPromotionsVo();
		searchVo.setPromotionName(promotions.getPromotionName());
		searchVo.setStoreName(promotions.getStoreName());
		searchVo.setStartDate(promotions.getStartDate());
		searchVo.setEndDate(promotions.getEndDate());
		searchVo.setPromotionStatus(promotions.getPromotionStatus());
		searchVo.setPromoId(promotions.getPromoId());
		searchVo.setId(promotions.getId());
		searchVo.setClientId(promotions.getClientId());
		searchVo.setStoreId(promotions.getStoreId());

		return searchVo;

	}

	@Override
	public String updatePromotionDates(SearchPromotionsVo vo) {
		if (vo == null) {
			throw new InvalidDataException("Please enter valid data");
		}

		Optional<PromotionToStoreEntity> dto = promostoreRepo.findById(vo.getId());

		if (Objects.nonNull(vo.getStartDate()) && Objects.nonNull(vo.getEndDate())) {
			dto.get().setStartDate(vo.getStartDate());
			dto.get().setEndDate(vo.getEndDate());
		}
		PromotionToStoreEntity promotionToStoreEntity = dto.get();

			if (LocalDate.now().isAfter(vo.getEndDate())) {
				promotionToStoreEntity.setPromotionStatus(true);
			}

		promostoreRepo.save(promotionToStoreEntity);

		return "Promotion Dates Updated Successfully";
	}

	@Override
	public String clonePromotionByStore(SearchPromotionsVo vo) {

		if (vo == null) {
			throw new InvalidDataException("please enter valid data");
		}

		if (promostoreRepo.existsByStoreName(vo.getStoreName())) {
			System.out.println("Promtion already mapped to the " + vo.getStoreName());
			throw new DuplicateRecordException("Promotion already mapped to this store ");
		}

		Optional<PromotionToStoreEntity> storeDto = promostoreRepo.findById(vo.getId());
		PromotionToStoreEntity promoStore = new PromotionToStoreEntity();

		BeanUtils.copyProperties(storeDto, promoStore);
		promoStore.setPromoId(storeDto.get().getPromoId());
		promoStore.setPriority(storeDto.get().getPriority());
		promoStore.setStartDate(storeDto.get().getStartDate());
		promoStore.setEndDate(storeDto.get().getEndDate());
		promoStore.setPromotionName(storeDto.get().getPromotionName());
		promoStore.setStoreName(vo.getStoreName());
		promoStore.setStoreId(vo.getStoreId());
		promoStore.setPromotionStatus(true);
		promoStore.setCreatedBy(storeDto.get().getCreatedBy());

		promostoreRepo.save(promoStore);

		return "Promotion Cloned Successfully";
	}

	// For Reports this method had used
	@Override
	public Page<SearchPromotionsVo> listOfPromotionsBySearch(SearchPromotionsVo svo, Pageable pageable) {

		List<SearchPromotionsVo> searchPromotions = new ArrayList<>();
		Page<PromotionToStoreEntity> promotionsList = null;

		if ((svo.getStartDate() != null) && (svo.getEndDate() != null)) {

			if ((svo.getPromoId() != null)) {

				promotionsList = promostoreRepo.findByStartDateAndEndDateAndPromoId(svo.getStartDate(),
						svo.getEndDate(), svo.getPromoId(), pageable);

			} else if ((svo.getPromoId() == null)) {

				promotionsList = promostoreRepo.findByStartDateAndEndDate(svo.getStartDate(), svo.getEndDate(),
						pageable);

			} else if (svo.getStoreName() != null) {

				promotionsList = promostoreRepo.findByStartDateAndEndDateAndStoreName(svo.getStartDate(),
						svo.getEndDate(), svo.getStoreName(), pageable);

			} else if (svo.getStoreName() == null) {

				promotionsList = promostoreRepo.findByStartDateAndEndDate(svo.getStartDate(), svo.getEndDate(),
						pageable);

			} else if (svo.getPromoId() != null && svo.getStoreName() != null) {

				promotionsList = promostoreRepo.findByStartDateAndEndDateAndPromoIdAndStoreName(svo.getStartDate(),
						svo.getEndDate(), svo.getPromoId(), svo.getStoreName(), pageable);
			} else if (svo.getPromoId() == null && svo.getStoreName() == null) {

				promotionsList = promostoreRepo.findByStartDateAndEndDate(svo.getStartDate(), svo.getEndDate(),
						pageable);

			}

			else {

				promotionsList = promostoreRepo.findByStartDateAndEndDate(svo.getStartDate(), svo.getEndDate(),
						pageable);
			}

		} else if ((svo.getStartDate() == null) && (svo.getEndDate() == null)) {
			if ((svo.getPromoId() != null)) {
				promotionsList = promostoreRepo.findByPromoIdIs(svo.getPromoId(), pageable);
			} else if (svo.getPromoId() == null && svo.getStoreName() != null) {

				promotionsList = promostoreRepo.findByStoreName(svo.getStoreName(), pageable);
			}

		}

		if (promotionsList.isEmpty()) {
			throw new RecordNotFoundException("Promotins are not exists");
		} else {

			return promotionsList.map(promo -> mapToVo(promo));

		}

	}

	@Override
	public String saveBenfit(BenefitVo vo) {

		BenfitEntity entity = new BenfitEntity();
		BeanUtils.copyProperties(vo, entity);
		BenfitEntity save = benfitRepo.save(entity);

		if (save.getBenfitId() != null) {
			return "Benfit id:" + save.getBenfitId();
		}

		return "Benfit Saving Failed";
	}

	@Override
	public List<ReportVo> activeVSinactivePromos() {

		List<ReportVo> rvo = new ArrayList<ReportVo>();

		List<PromotionsEntity> promos = promoRepo.findByIsActive(Boolean.TRUE);
		Long acount = promos.stream().mapToLong(i -> i.getPromoId()).count();
		ReportVo avo = new ReportVo();
		avo.setName("Active Promos");
		avo.setCount(acount);
		rvo.add(avo);
		List<PromotionsEntity> ipromos = promoRepo.findByIsActive(Boolean.FALSE);
		Long icount = ipromos.stream().mapToLong(i -> i.getPromoId()).count();
		ReportVo ivo = new ReportVo();
		ivo.setName("InActive Promos");
		ivo.setCount(icount);
		rvo.add(ivo);

		return rvo;
	}

	@Override
	public List<ProductVO> checkPromtion(List<ProductVO> listofInvTxt, Long storeId, Long domainId) {

		List<PromotionToStoreEntity> activePromos = promostoreRepo.findByStoreIdAndPromotionStatus(storeId, true);
		// System.out.println("Active Promo List::" + activePromos.toString());

		List<Long> promoids = activePromos.stream().map(promo -> promo.getPromoId()).collect(Collectors.toList());

		System.out.println("List Of Promo Ids:: " + promoids);

		List<PromotionsEntity> listOfPromos = promoRepo.findByPromoIdInAndApplicability(promoids,
				Applicability.promotionForEachBarcode);
		listofInvTxt.stream().forEach(barcodevo -> {

			listOfPromos.stream().forEach(p -> {
				PromotionSlabsEntity checkPromoApplyTypeForSlabs = checkPromoApplyTypeForSlabs(p, barcodevo);

				if (checkPromoApplyTypeForSlabs != null) {

					List<BenfitEntity> benList = new ArrayList<>();
					benList.add(checkPromoApplyTypeForSlabs.getBenfitEntity());

					if (checkPoolRules.checkPools(p.getPoolEntity(), barcodevo))
						barcodevo.setCalculatedDiscountsVo(
								calculateBenifits.calculate(promoMapper.convertBenfitEntityToVo(benList), barcodevo));

				} else if (checkPromoApplyType(p, barcodevo)) {

					if (checkPoolRules.checkPools(p.getPoolEntity(), barcodevo))
						barcodevo.setCalculatedDiscountsVo(calculateBenifits
								.calculate(promoMapper.convertBenfitEntityToVo(p.getBenfitEntity()), barcodevo));

				}

			});

		});

		return listofInvTxt;
	}

	private boolean checkPromoApplyType(PromotionsEntity promoEntity, ProductVO barcodeVo) {

		if (promoEntity.getPromoApplyType().equals(PromoApplyType.FixedQuantity)
				&& barcodeVo.getQuantity() >= promoEntity.getBuyItemsFromPool())
			return true;

		if (promoEntity.getPromoApplyType().equals(PromoApplyType.AnyQuantity) && barcodeVo.getQuantity() > 0)
			return true;

		return false;
	}

	private PromotionSlabsEntity checkPromoApplyTypeForSlabs(PromotionsEntity promoEntity, ProductVO barcodeVo) {

		if (promoEntity.getPromoApplyType().equals(PromoApplyType.QuantitySlab)
				|| promoEntity.getPromoApplyType().equals(PromoApplyType.ValueSlab)) {

			for (PromotionSlabsEntity slab : promoEntity.getPromotionSlabEntity()) {
				if ((barcodeVo.getQuantity() >= slab.getFromSlab() && barcodeVo.getQuantity() <= slab.getToSlab())
						|| (barcodeVo.getItemMrp() >= slab.getFromSlab()
								&& barcodeVo.getItemMrp() <= slab.getToSlab())) {
					if (slab.getBenfitEntity() == null)
						return null;

					return slab;
				}

			}

		}

		return null;
	}

	@Override
	public String addPromotionToStore(PromotionToStoreVo vo) {

		List<PromotionsVo> promoVo = vo.getPromotions();
		List<StoreVo> stores = vo.getStores();

		List<PromotionToStoreEntity> promoStoreList = new ArrayList<>();

		for (StoreVo storeVo : stores) {

			for (PromotionsVo promotionsVo : promoVo) {

				PromotionToStoreEntity promoStoreEntity = new PromotionToStoreEntity();
				promoStoreEntity.setStartDate(vo.getStartDate());
				promoStoreEntity.setEndDate(vo.getEndDate());
				promoStoreEntity.setCreatedBy(vo.getCreatedBy());
				promoStoreEntity.setPromotionStatus(vo.getPromotionStatus());
				promoStoreEntity.setPriority(vo.getPriority());
				promoStoreEntity.setStoreId(storeVo.getId());
				promoStoreEntity.setPromoId(promotionsVo.getPromoId());
				promoStoreEntity.setPromotionName(promotionsVo.getPromotionName());
				promoStoreEntity.setStoreName(storeVo.getName());
				promoStoreEntity.setClientId(vo.getClientId());

				promoStoreList.add(promoStoreEntity);
			}

		}

		promostoreRepo.saveAll(promoStoreList);

		return "Promotions mapped to store sccessfully";
	}

	@Override
	public String updatePriority(SearchPromotionsVo vo) {

		Optional<PromotionToStoreEntity> promoStoreId = promostoreRepo.findById(vo.getId());

		if (promoStoreId.isPresent()) {

			PromotionToStoreEntity promotionToStoreEntity = promoStoreId.get();
			promotionToStoreEntity.setPriority(vo.getPriority());

		} else {

			throw new RecordNotFoundException("PromoStoreId not exists");
		}

		return "Priority updated successfully for the id " + promoStoreId.get().getId();
	}

	@Override
	public List<SearchPromotionsVo> searchPromotionByStoreName(SearchPromotionsVo vo) {

		List<SearchPromotionsVo> listOfPromos = new ArrayList<>();
		List<PromotionToStoreEntity> storeNames = promostoreRepo.findByStoreName(vo.getStoreName());

		if (storeNames != null) {

			storeNames.stream().forEach(s -> {

				SearchPromotionsVo svo = new SearchPromotionsVo();

				svo.setId(s.getId());
				svo.setPromoId(s.getPromoId());
				svo.setPriority(s.getPriority());
				svo.setPromotionName(s.getPromotionName());
				svo.setStartDate(s.getStartDate());
				svo.setEndDate(s.getEndDate());
				svo.setStoreName(s.getStoreName());
				svo.setPromotionStatus(s.getPromotionStatus());
				listOfPromos.add(svo);

			});

		} else {

			throw new RecordNotFoundException("store name not exists");
		}

		return listOfPromos;
	}

	@Override
	public List<PromotionToStoreEntity> getAllStorePromotions() {

		List<PromotionToStoreEntity> promoStoreList = promostoreRepo.findAll();
		if (promoStoreList == null) {

			throw new RecordNotFoundException("Store data not exists");

		}


		return promoStoreList;
	}

	@Override
	public Page<PromotionsVo> promotionSearching(PromotionsVo svo, Pageable pageable) {

		Page<PromotionsEntity> promoList = null;

		if (svo.getIsActive() != null && svo.getApplicability().equals(Applicability.promotionForEachBarcode)) {
			promoList = promoRepo.findByIsActiveAndApplicabilityAndClientId(svo.getIsActive(),
					Applicability.promotionForEachBarcode, svo.getClientId(), pageable);

		} else if (svo.getIsActive() != null && svo.getApplicability().equals(Applicability.promotionForWholeBill)) {
			promoList = promoRepo.findByIsActiveAndApplicabilityAndClientId(svo.getIsActive(),
					Applicability.promotionForWholeBill, svo.getClientId(), pageable);

		} else if (svo.getIsActive() == null && svo.getApplicability().equals(Applicability.promotionForEachBarcode)) {
			promoList = promoRepo.findByApplicabilityAndClientId(Applicability.promotionForEachBarcode,
					svo.getClientId(), pageable);

		} else if (svo.getIsActive() == null && svo.getApplicability().equals(Applicability.promotionForWholeBill)) {
			promoList = promoRepo.findByApplicabilityAndClientId(Applicability.promotionForWholeBill, svo.getClientId(),
					pageable);

		} else if (svo.getIsActive() != null && svo.getApplicability().equals(Applicability.All)) {

			promoList = promoRepo.findByIsActiveAndClientId(svo.getIsActive(), svo.getClientId(), pageable);

		} else if (svo.getIsActive() == null && svo.getApplicability().equals(Applicability.All)) {

			promoList = promoRepo.findByIsActiveAndClientId(svo.getIsActive(), svo.getClientId(), pageable);

		}
		if (promoList.isEmpty()) {
			throw new RecordNotFoundException("Promotions data not exists");
		} else {

			return promoList.map(promo -> mapToVo(promo));
		}

	}

	private PromotionsVo mapToVo(PromotionsEntity promotions) {

		PromotionsVo listOfPromos = promoMapper.convertPromotionEntityToVo(promotions);

		return listOfPromos;

	}

	@Override
	public List<LineItemVo> checkInvoiceLevelPromtion(List<LineItemVo> listofLineItemsTxt, Long storeId,
			Long domainId) {

		// getting active promotions from the store
		List<PromotionToStoreEntity> activePromos = promostoreRepo.findByStoreIdAndPromotionStatus(storeId, true);

		// collecting promoIds
		List<Long> promoIds = activePromos.stream().map(promo -> promo.getPromoId()).collect(Collectors.toList());
		System.out.println("Active Promo Ids >>" + promoIds);

		// get promotions by based on applicability, promoIds
		List<PromotionsEntity> listOfPromos = promoRepo.findByPromoIdInAndApplicability(promoIds,
				Applicability.promotionForWholeBill);

		for (PromotionsEntity promo : listOfPromos) {

			BenfitEntity slabBenefit = null;

			List<LineItemVo> promoEligibleLineItems = calculateBenifits.getPromoEligibleLineItems(listofLineItemsTxt,
					promo, slabBenefit);

			List<Double> totalQuantityAndMrp = calculateTotalMrpAndQuantity(promoEligibleLineItems);

			if (promoEligibleLineItems.size() > 0) {

				if (promo.getPromoApplyType().equals(PromoApplyType.QuantitySlab)) {

					System.out.println("Quantity Slab");

					slabBenefit = getSlabBenefit(promo, totalQuantityAndMrp.get(0));
					

					// call benefits calculation engine with required fields

				} else if (promo.getPromoApplyType().equals(PromoApplyType.ValueSlab)) {

					System.out.println("Value Slab");

					slabBenefit = getSlabBenefit(promo, totalQuantityAndMrp.get(1));

				}

				listofLineItemsTxt = calculateBenifits.calculateInvoiceLevelBenefits(promo, slabBenefit,
						totalQuantityAndMrp, listofLineItemsTxt, promoEligibleLineItems);

			} else {

				// call the distribute to all line items method..

				listofLineItemsTxt = calculateBenifits.distributeDiscountToAllProducts(listofLineItemsTxt, 0.0);

			}
		}

		return listofLineItemsTxt;
	}

	private BenfitEntity getSlabBenefit(PromotionsEntity promo, Double value) {

		System.out.println("getSlabBenefit() method called");

		BenfitEntity benefitEntity = null;

		for (PromotionSlabsEntity promotionSlabsEntity : promo.getPromotionSlabEntity()) {

			if (value >= promotionSlabsEntity.getFromSlab() && value <= promotionSlabsEntity.getToSlab())
				benefitEntity = promotionSlabsEntity.getBenfitEntity();
			
			if(benefitEntity == null)
			{
				throw new InvalidDataException("Please provide the valid data");
			}

		}

		return benefitEntity;
	}

	private List<Double> calculateTotalMrpAndQuantity(List<LineItemVo> listofLineItemsTxt) {

		List<Double> calculatedValues = new LinkedList<>();

		double totalQuantity = 0;
		double totalMrp = 0.0;

		for (LineItemVo lineItemTextileVo : listofLineItemsTxt) {

			totalQuantity = totalQuantity + lineItemTextileVo.getQuantity();
			totalMrp = totalMrp + (lineItemTextileVo.getItemPrice() * lineItemTextileVo.getQuantity());

		}
		calculatedValues.add(totalQuantity);
		calculatedValues.add(totalMrp);

		System.out.println("Calculate totalMrpAndQuantityMethod() called");

		return calculatedValues;

	}

	@Override
	public String updatePromotionStatus(Long id, Boolean isActive) {

		PromotionToStoreEntity promoId = promostoreRepo.findByPromoId(id);

		if (promoId != null) {

			promoId.setPromotionStatus(isActive);

		} else {

			throw new RecordNotFoundException("Promo Id not exists");
		}

		return "Promotion Status Updated Successfully";
	}

}
