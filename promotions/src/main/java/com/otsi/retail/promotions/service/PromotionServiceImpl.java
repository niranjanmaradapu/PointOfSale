package com.otsi.retail.promotions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.promotions.common.BenfitType;
import com.otsi.retail.promotions.common.DiscountType;
import com.otsi.retail.promotions.common.Operator;
import com.otsi.retail.promotions.common.PromotionType;
import com.otsi.retail.promotions.config.Config;
import com.otsi.retail.promotions.entity.BenfitEntity;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.PromoBarcodeEntity;
import com.otsi.retail.promotions.entity.PromotionToStoreEntity;
import com.otsi.retail.promotions.entity.PromotionsEntity;
import com.otsi.retail.promotions.exceptions.DuplicateRecordException;
import com.otsi.retail.promotions.exceptions.InvalidDataException;
import com.otsi.retail.promotions.exceptions.RecordNotFoundException;
import com.otsi.retail.promotions.gatewayresponse.GateWayResponse;
import com.otsi.retail.promotions.mapper.PromotionMapper;
import com.otsi.retail.promotions.repository.BenfitRepo;
import com.otsi.retail.promotions.repository.PoolRepo;
import com.otsi.retail.promotions.repository.PromoBarcodeEntityRepository;
import com.otsi.retail.promotions.repository.PromotionRepo;
import com.otsi.retail.promotions.repository.PromotionToStoreRepo;
import com.otsi.retail.promotions.vo.BenfitVo;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.ConnectionPromoVo;
import com.otsi.retail.promotions.vo.LineItemVo;
import com.otsi.retail.promotions.vo.PromotionsVo;
import com.otsi.retail.promotions.vo.ReportVo;
import com.otsi.retail.promotions.vo.Pool_RuleVo;
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
	private PromoBarcodeEntityRepository promoBarcodeRepo;


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
	public ConnectionPromoVo getListOfPromotions(String flag, Long domainId) {
		log.debug("debugging getListOfPromotions:" + flag);
		List<PromotionsEntity> promoList = new ArrayList<>();

		Boolean status = null;
		if (flag.equalsIgnoreCase("true")) {
			status = Boolean.TRUE;
		}
		if (flag.equalsIgnoreCase("false")) {
			status = Boolean.FALSE;
		}
		if (flag.equalsIgnoreCase("all") && domainId == null) {
			promoList = promoRepo.findAll();
		} else if (!(flag.isEmpty()) && domainId == null) {

			promoList = promoRepo.findByIsActive(status);
		} else if (flag.isEmpty() && domainId != null) {

			promoList = promoRepo.findByDomainId(domainId);
		} else {
			promoList = promoRepo.findByIsActiveAndDomainId(status, domainId);
		}
		if (!promoList.isEmpty()) {
			ConnectionPromoVo promoVo = new ConnectionPromoVo();
			List<PromotionsVo> listOfPromo = promoMapper.convertPromoEntityToVo(promoList);
			log.warn("we are checking if list of promotions is fetching...");
			log.info("list of promotions is fetching...");
			promoVo.setPromovo(listOfPromo);
			promoVo.setDomainId(domainId);
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
	public String addPromotionToStore(PromotionsVo vo) {

		PromotionsEntity promotionEntity = promoRepo.findByPromotionNameIs(vo.getPromotionName());

		if (promotionEntity != null) {

			promotionEntity.setPromotionStartDate(vo.getPromotionStartDate());
			promotionEntity.setPromotionEndDate(vo.getPromotionEndDate());
			promoRepo.save(promotionEntity);

		}

		Long promoId = promotionEntity.getPromoId();
		Long storeId = vo.getStoreVo().getId();
		PromotionToStoreEntity entity = new PromotionToStoreEntity();
		entity.setPromoId(promoId);
		entity.setStoreId(storeId);
		entity.setPriority(vo.getPriority());
		entity.setCreatedBy(vo.getCreatedBy());
		entity.setPromotionStartDate(vo.getPromotionStartDate());	
		entity.setPromotionEndDate(vo.getPromotionEndDate());;
		entity.setPromotionStatus(vo.getIsActive());		
		promostoreRepo.save(entity);

		return "Promotion Mapped Successfully";
	}

	@Override
	public List<SearchPromotionsVo> searchPromotion(SearchPromotionsVo vo) {
		List<SearchPromotionsVo> searchPromoVo = new ArrayList<SearchPromotionsVo>();
		List<PromotionsEntity> promoDetails = new ArrayList<PromotionsEntity>();
		PromotionsEntity promoEntity = new PromotionsEntity();
		PromotionToStoreEntity promoStore = new PromotionToStoreEntity();

		if (vo.getPromotionStartDate() != null && vo.getPromotionEndDate() != null) {
			if (vo.getPromotionName() != null) {

				promoDetails = promoRepo.findByPromotionStartDateAndPromotionEndDateAndPromotionNameAndIsActive(
						vo.getPromotionStartDate(), vo.getPromotionEndDate(), vo.getPromotionName(), vo.getIsActive());
			} else if (vo.getPromotionName() == null) {
				promoDetails = promoRepo.findByPromotionStartDateAndPromotionEndDateAndIsActive(vo.getPromotionStartDate(),
						vo.getPromotionEndDate(), vo.getIsActive());

			} else if (vo.getPromotionName() != null) {
				promoDetails = promoRepo.findByPromotionStartDateAndPromotionEndDateAndPromotionNameAndIsActive(vo.getPromotionStartDate(),
						vo.getPromotionEndDate(), vo.getPromotionName(), vo.getIsActive());

			} else
				promoDetails = promoRepo.findByPromotionStartDateAndPromotionEndDateAndIsActive(vo.getPromotionStartDate(), vo.getPromotionEndDate(),
						vo.getIsActive());

		} else if (vo.getPromotionStartDate() == null && vo.getPromotionEndDate() == null) {
			if (vo.getPromotionName() != null) {
				promoDetails = promoRepo.findByPromotionName(vo.getPromotionName());
				// promoDetails.add(promoEntity);

			} 

			else
				promoDetails = promoRepo.findByIsActive(vo.getIsActive());

		}
		if (promoDetails.isEmpty()) {
			throw new RecordNotFoundException("No record found with given information");

		}

		else {
			Long promoCount = promoDetails.stream().count();
			promoDetails.stream().forEach(p -> {
				SearchPromotionsVo searchVo = new SearchPromotionsVo();
				searchVo.setPromotionName(p.getPromotionName());
				searchVo.setPromotionStartDate(p.getPromotionStartDate());
				searchVo.setPromotionEndDate(p.getPromotionEndDate());
				searchVo.setIsActive(p.getIsActive());
				searchVo.setPromoId(p.getPromoId());
				searchVo.setPriority(promoStore.getPriority());
				searchVo.setPromotionsCount(promoCount);

				searchPromoVo.add(searchVo);
			});

		}

		return searchPromoVo;
	}


	@Override
	public String updatePromotionDates(SearchPromotionsVo vo) {
		if (vo == null) {
			throw new InvalidDataException("Please enter valid data");
		}

		PromotionToStoreEntity dto = promostoreRepo.findByPromoId(vo.getPromoId());

		if (Objects.nonNull(vo.getPromotionStartDate()) && Objects.nonNull(vo.getPromotionEndDate())
				&& Objects.nonNull(vo.getIsActive())) {
			dto.setPromoId(vo.getPromoId());
			dto.setPromotionStartDate(vo.getPromotionStartDate());
			
			dto.setPromotionEndDate(vo.getPromotionEndDate());
			dto.setPromotionStatus(vo.getIsActive());
		}

		PromotionToStoreEntity pEntity = promostoreRepo.save(dto);

		return "Promotion Dates Updated Successfully";
	}

	@Override
	public String clonePromotionByStore(SearchPromotionsVo vo) {

		if (vo == null) {
			throw new InvalidDataException("Please enter valid data");
		}

		SearchPromotionsVo svo = new SearchPromotionsVo();
		Optional<PromotionsEntity> dto = promoRepo.findById(vo.getPromoId());
		PromotionsEntity newDto = new PromotionsEntity();
		PromotionToStoreEntity promoStore = new PromotionToStoreEntity();

			BeanUtils.copyProperties(dto, newDto);
			newDto.setPromotionStartDate(dto.get().getPromotionStartDate());
			newDto.setPromotionEndDate(dto.get().getPromotionEndDate());
			newDto.setPromotionName(dto.get().getPromotionName());
			newDto.setIsActive(true);
			newDto.setDomainId(dto.get().getDomainId());
			newDto.setCreatedBy(dto.get().getCreatedBy());
			newDto.setCreatedDate(LocalDate.now());
			newDto.setDescription(dto.get().getDescription());
			newDto.setPromoApplyType(dto.get().getPromoApplyType());
			newDto.setIsTaxExtra(dto.get().getIsTaxExtra());
			newDto.setLastModified(LocalDate.now());
			newDto.setPrintNameOnBill(dto.get().getPrintNameOnBill());
			newDto.setApplicability(dto.get().getApplicability());
			newDto.setBuyItemsFromPool(dto.get().getBuyItemsFromPool());

			List<BenfitEntity> benfits = new ArrayList<>();

			dto.get().getBenfitEntity().stream().forEach(b -> {
				BenfitEntity benfit = new BenfitEntity();
				benfit.setBenfitType(b.getBenfitType());
				benfit.setDiscountType(b.getDiscountType());
				benfit.setDiscount(b.getDiscount());
				benfit.setNumOfItemsFromBuyPool(b.getNumOfItemsFromBuyPool());
				benfit.setNumOfItemsFromGetPool(b.getNumOfItemsFromGetPool());
				benfit.setItemValue(b.getItemValue());
				benfit.setPercentageDiscountOn(b.getPercentageDiscountOn());
				benfit.setPoolId(b.getPoolId());
				benfit.setPoolName(b.getPoolName());
				benfits.add(benfit);
			});
			newDto.setBenfitEntity(benfits);
			promoRepo.save(newDto);

		return "Promotion Cloned Successfully";
	}

	// Method for adding promotion to product
	@Override
	public String addPromtionToBarcode(Long promoId, String barcode) {

		PromotionsEntity promo = promoRepo.findByPromoId(promoId);

		if (promo != null) {

			PromoBarcodeEntity entity = new PromoBarcodeEntity();
			entity.setPromoId(promoId);
			entity.setBarCode(barcode);

			promoBarcodeRepo.save(entity);

		} else {
			return "Promotion is not valid";
		}

		return "Successfully add promotion to Barcode";
	}

	@Override
	public List<SearchPromotionsVo> listOfPromotionsBySearch(SearchPromotionsVo svo) {

		List<SearchPromotionsVo> searchPromotions = new ArrayList<>();
		List<PromotionsEntity> promotionsList = new ArrayList<>();

		if ((svo.getPromotionStartDate() != null) && (svo.getPromotionEndDate() != null)) {

			if ((svo.getPromoId() != null)) {

				promotionsList = promoRepo.findByPromotionStartDateAndPromotionEndDateAndPromoId(svo.getPromotionStartDate(),
						svo.getPromotionEndDate(), svo.getPromoId());

			} else if ((svo.getPromoId() == null)) {
				promotionsList = promoRepo.findByPromotionStartDateAndPromotionEndDate(svo.getPromotionStartDate(), svo.getPromotionEndDate());
			} else if (svo.getPromoId() != null) {

				promotionsList = promoRepo.findByPromotionStartDateAndPromotionEndDateAndPromoId(svo.getPromotionStartDate(), svo.getPromotionEndDate(),
						svo.getPromoId());

			} else {

				promotionsList = promoRepo.findByPromotionStartDateAndPromotionEndDate(svo.getPromotionStartDate(), svo.getPromotionEndDate());
			}

		} else if ((svo.getPromotionStartDate() == null) && (svo.getPromotionEndDate() == null)) {
			if ((svo.getPromoId() != null)) {
				promotionsList = promoRepo.findByPromoIdIs(svo.getPromoId());
			}

		}

		if (promotionsList.isEmpty()) {
			throw new RecordNotFoundException("Promotins are not exists");
		} else {

			promotionsList.stream().forEach(r -> {

				SearchPromotionsVo searchVo = new SearchPromotionsVo();
				searchVo.setPromoId(r.getPromoId());
				searchVo.setPromotionName(r.getPromotionName());
				searchVo.setDescription(r.getDescription());
				searchVo.setPromotionStartDate(r.getPromotionStartDate());
				searchVo.setPromotionEndDate(r.getPromotionEndDate());
				searchPromotions.add(searchVo);
			});

		}

		return searchPromotions;
	}

	@Override
	public String saveBenfit(BenfitVo vo) {

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


}
