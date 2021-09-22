package com.otsi.retail.connectionpool.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;
import com.otsi.retail.connectionpool.entity.StoresEntity;
import com.otsi.retail.connectionpool.exceptions.InvalidDataException;
import com.otsi.retail.connectionpool.exceptions.RecordNotFoundException;
import com.otsi.retail.connectionpool.mapper.PromotionMapper;
import com.otsi.retail.connectionpool.repository.PoolRepo;
import com.otsi.retail.connectionpool.repository.PromotionRepo;
import com.otsi.retail.connectionpool.repository.StoreRepo;
import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.PromotionsVo;
import com.otsi.retail.connectionpool.vo.StoreVo;

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
	private StoreRepo storeRepo;

	// Method for adding promotion to pools
	@Override
	public String addPromotion(PromotionsVo vo) {
		log.debug("debugging addPromotion:" + vo);
		if (vo.getStoreVo() == null || vo.getPoolVo() == null) {
			throw new InvalidDataException("please give valid data");
		}
		List<ConnectionPoolVo> poolVo = vo.getPoolVo();

		List<Long> poolIds = poolVo.stream().map(x -> x.getPoolId()).collect(Collectors.toList());

		List<PoolEntity> poolList = poolRepo.findByPoolIdInAndIsActive(poolIds, Boolean.TRUE);

		// Code added by sudheer
		List<StoreVo> storeVo = vo.getStoreVo();

		/** Promotion mapped to store by storeId **/

		List<Long> storeId = storeVo.stream().map(a -> a.getStoreId()).collect(Collectors.toList());
		List<StoresEntity> storeList = storeRepo.findByStoreIdIn(storeId);

		/** Promotion mapped to store by storeName **/

		// List<String> storeName = storeVo.stream().map(b ->
		// b.getStoreName()).collect(Collectors.toList());
		// List<StoresEntity> storeList = storeRepo.findByStoreNameIn(storeName);

		PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList, storeList);

		if (poolVo.size() == poolList.size() && storeVo.size() == storeList.size()) {

			PromotionsEntity savedPromo = promoRepo.save(entity);

		} else {
			log.error("Please give valid/active pools/stores");
			throw new InvalidDataException("Please give valid/active pools/stores");
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
			List<StoreVo> storeVo = vo.getStoreVo();

			/** Promotion mapped to store by storeId **/

			List<Long> storeId = storeVo.stream().map(a -> a.getStoreId()).collect(Collectors.toList());
			List<StoresEntity> storeList = storeRepo.findByStoreIdIn(storeId);

			/** Promotion mapped to store by storeName **/

			// List<String> storeName = storeVo.stream().map(b ->
			// b.getStoreName()).collect(Collectors.toList());
			// List<StoresEntity> storeList = storeRepo.findByStoreNameIn(storeName);

			if (poolVo.size() == poolList.size()) {

				PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList, storeList);
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
}
