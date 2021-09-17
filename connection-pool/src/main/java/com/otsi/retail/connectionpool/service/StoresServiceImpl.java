/**
 * 
 */
package com.otsi.retail.connectionpool.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.connectionpool.entity.StoresEntity;
import com.otsi.retail.connectionpool.mapper.StoreMapper;
import com.otsi.retail.connectionpool.repository.StoreRepo;
import com.otsi.retail.connectionpool.vo.StoreVo;

/**
 * @author Sudheer.Swamy
 *
 */
@Service
public class StoresServiceImpl implements StoresService {

	@Autowired
	private StoreRepo storeRepo;

	@Autowired
	private StoreMapper storeMapper;

	@Override
	public String addStore(StoreVo vo) throws Exception {

		StoresEntity entity = storeMapper.converStoreVoToEntity(vo);
		Optional<StoresEntity> storeName = storeRepo.findByStoreName(vo.getStoreName());

		try {

			if (!(storeName.isPresent())) {
				vo = storeMapper.entityToVo(storeRepo.save(entity));

			} else {
				throw new Exception("Store already exists!!");
			}
			return "Store Created Successfully...";
		} catch (Exception e) {

			throw new Exception("Exception occurs while creating the store..");

		}

	}

	@Override
	public Optional<StoresEntity> getByStoreId(Long storeId) {

		Optional<StoresEntity> storeList = storeRepo.findById(storeId);

		return storeList;
	}

	@Override
	public Optional<StoresEntity> findStoreByName(String storeName) {

		return storeRepo.findByStoreName(storeName);

	}

}
