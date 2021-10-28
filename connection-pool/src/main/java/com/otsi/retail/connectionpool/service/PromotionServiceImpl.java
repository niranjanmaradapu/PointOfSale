package com.otsi.retail.connectionpool.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otsi.retail.connectionpool.config.Config;
import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.entity.PromotionToStoreEntity;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;
import com.otsi.retail.connectionpool.exceptions.InvalidDataException;
import com.otsi.retail.connectionpool.exceptions.RecordNotFoundException;
import com.otsi.retail.connectionpool.gatewayresponse.GateWayResponse;
import com.otsi.retail.connectionpool.mapper.PromotionMapper;
import com.otsi.retail.connectionpool.repository.PoolRepo;
import com.otsi.retail.connectionpool.repository.PromotionRepo;
import com.otsi.retail.connectionpool.repository.PromotionToStoreRepo;
import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;
import com.otsi.retail.connectionpool.vo.searchPromotionsVo;

/**
 * This class contains all Bussiness logics regarding promotions and their
 * management
 * 
 * @author Manikanta Guptha
 *
 */

@Service
public class PromotionServiceImpl implements PromotionService {

	private Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

	@Autowired
	private PromotionMapper promoMapper;

	@Autowired
	private PromotionRepo promoRepo;

	@Autowired
	private PoolRepo poolRepo;
	@Autowired
	private PromotionToStoreRepo promostoreRepo;

	@Autowired
	private Config config;

	@Autowired
	private RestTemplate template;

//	@Autowired
//	private StoreRepo storeRepo;

	// Method for adding promotion to pools
	@Override
	public String addPromotion(PromotionsVo vo) {
		log.debug("debugging addPromotion:" + vo);
		if (/* vo.getStoreVo() == null || */ vo.getPoolVo() == null) {
			throw new InvalidDataException("please give valid data");
		}
		List<ConnectionPoolVo> poolVo = vo.getPoolVo();

		List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

		List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

		// Code added by sudheer for store mappping
		// List<StoreVo> storeVo = vo.getStoreVo();

		/** Promotion mapped to store by storeId **/

		// List<Long> storeId = storeVo.stream().map(a ->
		// a.getStoreId()).collect(Collectors.toList());
		// List<StoresEntity> storeList = storeRepo.findByStoreIdIn(storeId);

		/** Promotion mapped to store by storeName **/

		// List<String> storeName = storeVo.stream().map(b ->
		// b.getStoreName()).collect(Collectors.toList());
		// List<StoresEntity> storeList = storeRepo.findByStoreNameIn(storeName);

		PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList/* , storeList */);

		if (poolVo.size() == poolList.size() /* && storeVo.size() == storeList.size() */) {

			PromotionsEntity savedPromo = promoRepo.save(entity);

		} else {
			log.error("Please give valid/active pools");
			throw new InvalidDataException("Please give valid/active pools");
		}
		log.warn("we are checking if pool is added...");
		log.info("Promotion mapped succesfully...");
		return "Promotion mapped succesfully...";
	}

	// Method for getting list of Promotions by using flag(Status)
	@Override
	public List<PromotionsVo> getListOfPromotions(String flag) {
		log.debug("debugging getListOfPromotions:" + flag);
		List<PromotionsEntity> promoList = new ArrayList<>();

		Boolean status = null;
		if (flag.equalsIgnoreCase("true")) {
			status = Boolean.TRUE;
		}
		if (flag.equalsIgnoreCase("false")) {
			status = Boolean.FALSE;
		}
		if (flag.equalsIgnoreCase("all")) {
			promoList = promoRepo.findAll();
		} else {
			promoList = promoRepo.findByIsActive(status);
		}
		if (!promoList.isEmpty()) {
			List<PromotionsVo> listOfPromo = promoMapper.convertPromoEntityToVo(promoList);
			log.warn("we are checking if list of promotions is fetching...");
			log.info("list of promotions is fetching...");
			return listOfPromo;
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
		/* try { */
		Optional<PromotionsEntity> promotion = promoRepo.findById(vo.getPromoId());

		if (promotion.isPresent()) {
			List<ConnectionPoolVo> poolVo = vo.getPoolVo();

			List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

			List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

			// Code added by sudheer
			// List<StoreVo> storeVo = vo.getStoreVo();

			/** Promotion mapped to store by storeId **/

			// List<Long> storeId = storeVo.stream().map(a ->
			// a.getStoreId()).collect(Collectors.toList());
			// List<StoresEntity> storeList = storeRepo.findByStoreIdIn(storeId);

			/** Promotion mapped to store by storeName **/

			// List<String> storeName = storeVo.stream().map(b ->
			// b.getStoreName()).collect(Collectors.toList());
			// List<StoresEntity> storeList = storeRepo.findByStoreNameIn(storeName);

			if (poolVo.size() == poolList.size()) {

				PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList/* , storeList */);
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
		/*
		 * } catch (Exception e) { throw new
		 * Exception("Exception occurs while modifying Promotion"); }
		 */
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

		PromotionsEntity promotionEntity = promoRepo.findByPromoName(vo.getPromoName());

		if (promotionEntity != null) {

			promotionEntity.setPromoType(vo.getPromoType());
			promotionEntity.setEndDate(vo.getEndDate());
			promotionEntity.setStartDate(vo.getStartDate());
			promotionEntity.setStoreName(vo.getStoreVo().getName());
			promoRepo.save(promotionEntity);

		}

		Long promoId = promotionEntity.getPromoId();
		Long storeId = vo.getStoreVo().getId();
		PromotionToStoreEntity entity = new PromotionToStoreEntity();
		entity.setPromoId(promoId);
		entity.setStoreId(storeId);

		promostoreRepo.save(entity);

		return "promoStore Mapped Successfully";
	}

	@Override
	public List<searchPromotionsVo> searchPromotion(searchPromotionsVo vo) {
		List<searchPromotionsVo> searchPromoVo = new ArrayList<searchPromotionsVo>();
		List<PromotionsEntity> promoDetails = new ArrayList<PromotionsEntity>();
		PromotionsEntity promoEntity = new PromotionsEntity();
		
		if(vo.getStartDate()!=null && vo.getEndDate()!=null) {
			if(vo.getPromotionName()!=null && vo.getStoreName()!=null) {
				
				 promoDetails= promoRepo.findByStartDateAndEndDateAndPromoNameAndStoreNameAndIsActive(vo.getStartDate(),vo.getEndDate(),vo.getPromotionName(),vo.getStoreName(),vo.isPromotionStatus());
			}
			else if(vo.getStoreName()!=null&& vo.getPromotionName()==null)
			{
				 promoDetails= promoRepo.findByStartDateAndEndDateAndStoreNameAndIsActive(vo.getStartDate(),vo.getEndDate(),vo.getStoreName(),vo.isPromotionStatus());

			}
			else if(vo.getPromotionName()!=null && vo.getStoreName()==null) {
				 promoDetails= promoRepo.findByStartDateAndEndDateAndPromoNameAndIsActive(vo.getStartDate(),vo.getEndDate(),vo.getPromotionName(),vo.isPromotionStatus());

			}
			else
				 promoDetails= promoRepo.findByStartDateAndEndDateAndIsActive(vo.getStartDate(),vo.getEndDate(),vo.isPromotionStatus());

			
		}
		else if(vo.getStartDate()==null && vo.getEndDate()==null) {
			if(vo.getPromotionName()!=null&& vo.getStoreName()==null) {
				 promoEntity= promoRepo.findByPromoName(vo.getPromotionName());
				 promoDetails.add(promoEntity);

			}
			else if(vo.getStoreName()!=null&& vo.getPromotionName()==null) {
				 promoDetails= promoRepo.findByStoreName(vo.getStoreName());

			}
			else if(vo.getPromotionName()!=null&& vo.getStoreName()!=null) {
				promoDetails= promoRepo.findByStoreNameAndPromoName(vo.getStoreName(),vo.getPromotionName());
			}
			
			else
				 promoDetails= promoRepo.findByIsActive(vo.isPromotionStatus());

		}
		if(promoDetails.isEmpty()) {
			throw new RecordNotFoundException("No record found with given information");
		
		
		}
		
		else {
			
			
			promoDetails.stream().forEach(p->{
				
				vo.setPromotionName(p.getPromoName());
				vo.setStoreName(p.getStoreName());
				vo.setStartDate(p.getStartDate());
				vo.setEndDate(p.getEndDate());
				vo.setPromotionStatus(p.getIsActive());
				vo.setPromoId(p.getPromoId());
				
				searchPromoVo.add(vo);
			});
			
			
		}
		
		
		return searchPromoVo;
	}
}
