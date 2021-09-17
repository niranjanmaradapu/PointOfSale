package com.otsi.retail.connectionpool.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.entity.PromotionsEntity;
import com.otsi.retail.connectionpool.entity.StoresEntity;
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
	public String addPromotion(PromotionsVo vo) throws Exception {

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

		PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList,storeList);

		if (poolVo.size() == poolList.size() && storeVo.size() == storeList.size()) {

			PromotionsEntity savedPromo = promoRepo.save(entity);

		} else {
			throw new Exception("Please give valid/active pools/stores");
		}
		return "Promotion mapped succesfully...";
	}

	// Method for getting list of Promotions by using flag(Status)
	@Override
	public List<PromotionsVo> getListOfPromotions(String flag) throws Exception {

		List<PromotionsEntity> promoList = new ArrayList<>();
		try {
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

			List<PromotionsVo> listOfPromo = promoMapper.convertPromoEntityToVo(promoList);
			return listOfPromo;

		} catch (Exception e) {
			throw new Exception("Exception occurs while modifying Promotion");
		}
	}

	// Method for modifying/editing Promotion
	@Override
	public String editPromotion(PromotionsVo vo) throws Exception {

		try {
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

					PromotionsEntity entity = promoMapper.convertPromoVoToEntity(vo, poolList,storeList);
					PromotionsEntity savedPromo = promoRepo.save(entity);

				} else {
					throw new Exception("Please give valid/active pools");
				}
				return "Promotion Modified succesfully";

			} else {
				throw new Exception("please give valid Promotion Id");
			}
		} catch (Exception e) {
			throw new Exception("Exception occurs while modifying Promotion");
		}
	}
}
