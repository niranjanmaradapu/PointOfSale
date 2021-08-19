package com.otsi.retail.userStore.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

	@Autowired
	private StoreRepository storeRepo;

	@Autowired
	private UserMapper userMapper;

	//calling the save method to save the details into the database  using store model object

	@Override
	public ResponseEntity<?> save(StoreModel storeModel) {
		StoreModel savestore=storeRepo.save(storeModel);
		return  new ResponseEntity<>(" record saved sucessfully",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> findByName(String storeName) {

		StoreModel list = storeRepo.findByStoreName(storeName);
      
		//calling a mapper to convert entity to vo object
		StoreVO findstores= userMapper.convertEntityToVolist(list);
		 return  ResponseEntity.ok().body("  sucessfully get the store deatils"+ findstores);

	}

	//delete the record from database using id
	@Override
	public ResponseEntity<Object> deleteById(Long id) {
		if (storeRepo.findById(id).isPresent()) {
			if (storeRepo.findById(id).get().getUsers().size() == 0) {
				storeRepo.deleteById(id);
				if (storeRepo.findById(id).isPresent()) {
					return ResponseEntity.unprocessableEntity().body("Failed to delete the specified record");
				} else
					return ResponseEntity.ok().body("Successfully deleted specified record");
			} else
				return ResponseEntity.unprocessableEntity()
						.body("Failed to delete,  Please delete the users associated with this store");
		} else
			return ResponseEntity.unprocessableEntity().body("No Records Found");
	}

	//get all the store information from database
	@Override
	public List<StoreVO> getAllStores() {
		// TODO Auto-generated method stub
		List<StoreVO> storeVos = new ArrayList<>();
		 List<StoreModel> stores=storeRepo.findAll();
		 stores.stream().forEach(store -> {
				StoreVO storevo = userMapper.convertEntityToVolist(store);
				storeVos.add(storevo);
			});
		return storeVos;
	}

}
