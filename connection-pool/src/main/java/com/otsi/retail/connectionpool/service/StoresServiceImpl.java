/**
 * 
 */
package com.otsi.retail.connectionpool.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.otsi.retail.connectionpool.entity.StoresEntity;
import com.otsi.retail.connectionpool.exceptions.DuplicateRecordException;
import com.otsi.retail.connectionpool.exceptions.InvalidDataException;
import com.otsi.retail.connectionpool.exceptions.RecordNotFoundException;
import com.otsi.retail.connectionpool.mapper.StoreMapper;
import com.otsi.retail.connectionpool.repository.StoreRepo;
import com.otsi.retail.connectionpool.vo.StoreVo;

/**
 * @author Sudheer.Swamy
 *
 */
@Service
public class StoresServiceImpl implements StoresService {

	private Logger log = LoggerFactory.getLogger(PromotionServiceImpl.class);

	@Autowired
	private StoreRepo storeRepo;

	@Autowired
	private StoreMapper storeMapper;

	@Override
	public String addStore(StoreVo vo) {
		log.debug("debugging addStore():" + vo);
		StoresEntity entity = storeMapper.converStoreVoToEntity(vo);
		Optional<StoresEntity> storeName = storeRepo.findByStoreName(vo.getStoreName());

		if (entity.getStoreName() == null && entity.getStoreDescription() == null) {
			log.error("please give valid data");
			throw new InvalidDataException("please give valid data");
		}

		if (!(storeName.isPresent())) {
			vo = storeMapper.entityToVo(storeRepo.save(entity));

		} else {
			log.error("Store already exists!!");
			throw new DuplicateRecordException("Store already exists!!");
		}
		log.warn("we are checking if store is added...");
		log.info("Store Created Successfully...");
		return "Store Created Successfully...";

	}

	@Override
	public Optional<StoresEntity> getByStoreId(Long storeId) {
		log.debug("debugging getByStoreId():" + storeId);
		Optional<StoresEntity> storeList = storeRepo.findById(storeId);
		if (storeList.isPresent()) {
			log.warn("we are checking if store is fetching by Id...");
			log.info("fetching store with Id:" + storeList);
			return storeList;
		} else
			log.error("record not found");
		throw new RecordNotFoundException("record not found");
	}

	@Override
	public Optional<StoresEntity> findStoreByName(String storeName) {
		log.debug("debugging findStoreByName():" + storeName);
		Optional<StoresEntity> storeList = storeRepo.findByStoreName(storeName);
		if (storeList.isPresent()) {
			log.warn("we are checking if store is fetching by name...");
			log.info("fetching store with name:" + storeList);
			return storeList;
		} else
			log.error("record not found");
		throw new RecordNotFoundException("record not found");
	}

}
