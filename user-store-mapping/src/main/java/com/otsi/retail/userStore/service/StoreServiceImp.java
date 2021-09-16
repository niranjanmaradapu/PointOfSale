package com.otsi.retail.userStore.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.userStore.controller.UserController;
import com.otsi.retail.userStore.mapper.UserMapper;
import com.otsi.retail.userStore.model.StoreModel;
import com.otsi.retail.userStore.model.UserModel;
import com.otsi.retail.userStore.repository.StoreRepository;
import com.otsi.retail.userStore.service.StoreService;
import com.otsi.retail.userStore.vo.StoreVO;
import com.otsi.retail.userStore.vo.UserVo;
// here we need to implement all the unimplemented methods which are implements by interface
@Service
public class StoreServiceImp implements StoreService {
	
	private Logger log=LoggerFactory.getLogger(StoreServiceImp.class);

	@Autowired
	private StoreRepository storeRepo;

	@Autowired
	private UserMapper userMapper;

	//calling the save method to save the details into the database  using store model object

	@Override
	public String save(StoreModel storeModel) {
		log.debug("debugging save store()");
		StoreModel savestore=storeRepo.save(storeModel);
		log.warn("we are checking if store is saved..");
		log.info("store saved successfully");
		return  " record saved sucessfully";
	}

	@Override
	public StoreVO findByName(String storeName) {
		log.debug("debugging findByName()");
		StoreModel list = storeRepo.findByStoreName(storeName);
      
		//calling a mapper to convert entity to vo object
		StoreVO findstores= userMapper.convertEntityToVolist(list);
		log.warn("we are testing if store details is fetching...");
		log.info("  sucessfully get the store deatils");
		 return  findstores;

	}

	//delete the record from database using id
	@Override
	public String deleteById(Long id) {
		log.debug("debugging deleteById:" + id);
		if (storeRepo.findById(id).isPresent()) {
			if (storeRepo.findById(id).get().getUsers().size() == 0) {
				log.error("Failed to delete the specified record");
				storeRepo.deleteById(id);
				if (storeRepo.findById(id).isPresent()) {
					return "Failed to delete the specified record";
				} else
					log.warn("we are testing if specificied record is deleted...");
				log.info("Successfully deleted specified record");
					return "Successfully deleted specified record";
			} else
				log.error("Failed to delete,  Please delete the users associated with this store");
				return "Failed to delete,  Please delete the users associated with this store";
		} else
			log.error("No Records Found");
			return "No Records Found";
	}

	//get all the store information from database
	@Override
	public List<StoreVO> getAllStores() {
		log.debug("debugging getAllStores");
		// TODO Auto-generated method stub
		List<StoreVO> storeVos = new ArrayList<>();
		 List<StoreModel> stores=storeRepo.findAll();
		 stores.stream().forEach(store -> {
				StoreVO storevo = userMapper.convertEntityToVolist(store);
				storeVos.add(storevo);
			});
		 log.warn("we are testing if all stores is fetching...");
			log.info("fetching all stores:" + storeVos);
		return storeVos;
	}

}
