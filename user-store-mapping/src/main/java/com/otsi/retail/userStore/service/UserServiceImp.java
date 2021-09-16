package com.otsi.retail.userStore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.userStore.exceptions.DuplicateRecordException;
import com.otsi.retail.userStore.mapper.UserMapper;
import com.otsi.retail.userStore.model.UserModel;
import com.otsi.retail.userStore.repository.UserRepository;
import com.otsi.retail.userStore.service.UserService;
import com.otsi.retail.userStore.vo.UserVo;
//here we need to implement all the unimplemented methods which are implements by interface
@Service
public class UserServiceImp implements UserService {
	
	private Logger log=LoggerFactory.getLogger(UserServiceImp.class);

	
	@Autowired
	UserRepository userrepo;
	@Autowired
	UserMapper userMapper;
	//get all the user information from database
	@Override
	public List<UserVo> getAllUsers() {
		log.debug("debugging getAllUsers");
		List<UserVo> userVos = new ArrayList<>();
		List<UserModel> users = userrepo.findAll();
		users.stream().forEach(user -> {
			UserVo uservo = userMapper.mapEntityToVo(user);
			userVos.add(uservo);
		});
		log.warn("we are testing if all users are fetching...");
		log.info("fetching all users:" + userVos);
		return userVos;
	}

	//create new user using userVo object
	@Override
	public String addUser(UserVo userVo) throws DuplicateRecordException {
		log.debug("debugging addUser:" + userVo);
		if (userrepo.existsByMobile(userVo.getMobile())) {
			log.error("User Already Exists with Mobile: " + userVo.getMobile());
			throw new DuplicateRecordException("User Already Exists with Mobile: " + userVo.getMobile());
		}
		UserModel user = new UserModel();
		userMapper.mapVoToEntity(userVo, user);
		UserModel savedUser = userrepo.save(user);
		userMapper.mapEntityToVo(savedUser);
		log.warn("we are testing if user is successfully added...");
		log.info("user added sucessfully");
		 return "user added sucessfully";
	}

	
	//update the existing user using id and uservo object
	@Override
	public  String updateuser(Long id, UserVo userVo) {
		log.debug("debugging updateuser:" + id);
		UserModel userv = userrepo.getById(id);

		userv.getStores().clear();
		userMapper.mapVoToEntity(userVo, userv);
		UserModel user = userrepo.save(userv);
		 userMapper.mapEntityToVo(user);
		 log.warn("we are testing if user is updated...");
			log.info("user updated sucessfully");
		 return  "user updated sucessfully";
	}

	//delete the user record  from the database using id
	@Override
	public String deleteUser(Long id) {
		log.debug("debugging deleteUser:" + id);
		Optional<UserModel> user = userrepo.findById(id);

		if (user.isPresent()) {
			user.get().removeStores();

			userrepo.deleteById(user.get().getId());
			log.warn("we are testing if user is deleted...");
			log.info("user with id: " + id + " deleted successfully!");
			return "user with id: " + id + " deleted successfully!";
		}
		log.error("record not found");
		return "record not found";
	}
}
