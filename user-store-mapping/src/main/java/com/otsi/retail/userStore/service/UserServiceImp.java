package com.otsi.retail.userStore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.retail.userStore.exceptions.DuplicateRecordException;
import com.otsi.retail.userStore.exceptions.RecordNotFoundException;
import com.otsi.retail.userStore.mapper.UserMapper;
import com.otsi.retail.userStore.model.UserModel;
import com.otsi.retail.userStore.repository.UserRepository;
import com.otsi.retail.userStore.vo.UserVo;

//here we need to implement all the unimplemented methods which are implements by interface
@Service
public class UserServiceImp implements UserService {

	private Logger log = LoggerFactory.getLogger(StoreServiceImp.class);

	@Autowired
	UserRepository userrepo;
	@Autowired
	UserMapper userMapper;

	// get all the user information from database
	@Override
	public List<UserVo> getAllUsers() {
		log.debug("debugging getAllUsers()");
		List<UserVo> userVos = new ArrayList<>();
		List<UserModel> users = userrepo.findAll();
		if (users.isEmpty()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("Record Not Found");
		}
		users.stream().forEach(user -> {
			UserVo uservo = userMapper.mapEntityToVo(user);
			userVos.add(uservo);
		});
		log.warn("we are checking if all user are fetching...");
		log.info("after fetching all users:" + userVos);
		return userVos;
	}

	// create new user using userVo object
	@Override
	public String addUser(UserVo userVo) throws DuplicateRecordException {
		log.debug("debugging addUser():" + userVo);
		if (userrepo.existsByMobile(userVo.getMobile())) {
			log.error("User Already Exists with Mobile: " + userVo.getMobile());
			throw new DuplicateRecordException("User Already Exists with Mobile: " + userVo.getMobile());
		}
		UserModel user = new UserModel();
		userMapper.mapVoToEntity(userVo, user);
		UserModel savedUser = userrepo.save(user);
		userMapper.mapEntityToVo(savedUser);
		log.warn("we are checking if user is added...");
		log.info("after user saved succesffully:" + userMapper);
		return "user added sucessfully";
	}

	// update the existing user using id and uservo object
	@Override
	public String updateuser(Long id, UserVo userVo) {
		log.debug("debugging updateuser():" + id + "and the user is:" + userVo);
		UserModel userv = userrepo.getById(id);
		if (userv == null) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("Record Not Found");
		}
		userv.getStores().clear();
		userMapper.mapVoToEntity(userVo, userv);
		UserModel user = userrepo.save(userv);
		userMapper.mapEntityToVo(user);
		log.warn("we wre checking if usre is updated...");
		log.info("after user is updated:" + userMapper);
		return "user updated sucessfully";
	}

	// delete the user record from the database using id
	@Override
	public String deleteUser(Long id) {
		log.debug("debugging deleteUser:" + id);
		Optional<UserModel> user = userrepo.findById(id);

		if (user.isPresent()) {
			user.get().removeStores();
			userrepo.deleteById(user.get().getId());
			log.warn("we are checking if user is deleted...");
			log.info("after deleting user with id:" + id);
			return "user with id: " + id + " deleted successfully!";
		}
		log.error("record not found");
		throw new RecordNotFoundException("record not found");
	}
}
