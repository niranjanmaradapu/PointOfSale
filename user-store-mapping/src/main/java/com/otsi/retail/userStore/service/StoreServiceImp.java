package com.otsi.retail.userStore.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.otsi.retail.userStore.exceptions.DuplicateRecordException;
import com.otsi.retail.userStore.exceptions.RecordNotFoundException;
import com.otsi.retail.userStore.mapper.UserMapper;
import com.otsi.retail.userStore.model.StoreModel;
import com.otsi.retail.userStore.repository.StoreRepository;
import com.otsi.retail.userStore.vo.StoreVO;

// here we need to implement all the unimplemented methods which are implements by interface
@Service
public class StoreServiceImp implements StoreService {
	
	private Logger log = LoggerFactory.getLogger(StoreServiceImp.class);

	@Autowired
	private StoreRepository storeRepo;

	@Autowired
	private UserMapper userMapper;

	// calling the save method to save the details into the database using store
	// model object

	@Override
	public String save(StoreModel storeModel) {
		log.debug("debugging saveStore():" + storeModel);
		StoreModel savestore = storeRepo.save(storeModel);
		log.warn("we are checking if store is saved...");
		log.info("after saving store details:" + storeModel);
		return " record saved sucessfully";
	}

	@Override
	public StoreVO findByName(String storeName) {
		log.debug("debugging saveStore():" + storeName);
		StoreModel list = storeRepo.findByStoreName(storeName);
		if (list == null) {
			log.error("Record not found");
			throw new RecordNotFoundException("Record not found");
		}

		// calling a mapper to convert entity to vo object
		StoreVO findstores = userMapper.convertEntityToVolist(list);
		log.warn("we are checking if store is fetching by name...");
		log.info("after fetching store by name :" + findstores);
		return findstores;

	}

	// delete the record from database using id
	@Override
	public String deleteById(Long id) throws DuplicateRecordException {
		log.debug("debugging deleteById():" + id);
		if (storeRepo.findById(id).isPresent()) {
			if (storeRepo.findById(id).get().getUsers().size() == 0) {
				storeRepo.deleteById(id);
				if (storeRepo.findById(id).isPresent()) {
					log.error("Failed to delete the specified record");
					throw new DuplicateRecordException("Failed to delete the specified record");
				} else
					log.warn("we are checking if store is deleted with specific record...");
				log.info("Successfully deleted specified record:" + id);
				return "Successfully deleted specified record";
			} else
				log.error("Failed to delete,  Please delete the users associated with this store");
			throw new DuplicateRecordException("Failed to delete,  Please delete the users associated with this store");
		} else
			log.error("No Records Found");
		throw new RecordNotFoundException("No Records Found");
	}

	// get all the store information from database
	@Override
	public List<StoreVO> getAllStores() {
		log.debug("debugging getAllStores()");
		List<StoreVO> storeVos = new ArrayList<>();
		List<StoreModel> stores = storeRepo.findAll();
		if (stores.isEmpty()) {
			log.error("Record not found");
			throw new RecordNotFoundException("Record not found");
		}
		stores.stream().forEach(store -> {
			StoreVO storevo = userMapper.convertEntityToVolist(store);
			storeVos.add(storevo);
		});
		log.warn("we are checking if all stores are fetching..");
		log.info("after fetching all stores:" + storeVos);
		return storeVos;
	}

}
