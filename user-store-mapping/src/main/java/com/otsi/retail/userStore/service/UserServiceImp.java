package com.otsi.retail.userStore.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.userStore.exceptions.DuplicateRecordException;
import com.otsi.retail.userStore.exceptions.RecordNotFoundException;
import com.otsi.retail.userStore.mapper.UserMapper;
import com.otsi.retail.userStore.model.UserModel;
import com.otsi.retail.userStore.repository.UserRepository;
import com.otsi.retail.userStore.service.UserService;
import com.otsi.retail.userStore.vo.UserVo;
//here we need to implement all the unimplemented methods which are implements by interface
@Service
public class UserServiceImp implements UserService {
	@Autowired
	UserRepository userrepo;
	@Autowired
	UserMapper userMapper;
	//get all the user information from database
	@Override
	public List<UserVo> getAllUsers() {
		List<UserVo> userVos = new ArrayList<>();
		List<UserModel> users = userrepo.findAll();
		if(users.isEmpty()) {
			throw new RecordNotFoundException("Recod Not Found");
		}
		users.stream().forEach(user -> {
			UserVo uservo = userMapper.mapEntityToVo(user);
			userVos.add(uservo);
		});
		return userVos;
	}

	//create new user using userVo object
	@Override
	public String addUser(UserVo userVo) throws DuplicateRecordException {
		if (userrepo.existsByMobile(userVo.getMobile())) {
			throw new DuplicateRecordException("User Already Exists with Mobile: " + userVo.getMobile());
		}
		UserModel user = new UserModel();
		userMapper.mapVoToEntity(userVo, user);
		UserModel savedUser = userrepo.save(user);
		userMapper.mapEntityToVo(savedUser);
		 return  "user added sucessfully";
	}

	
	//update the existing user using id and uservo object
	@Override
	public  String updateuser(Long id, UserVo userVo) {
		UserModel userv = userrepo.getById(id);
		if(userv==null) {
			throw new RecordNotFoundException("Recod Not Found");
		}

		userv.getStores().clear();
		userMapper.mapVoToEntity(userVo, userv);
		UserModel user = userrepo.save(userv);
		 userMapper.mapEntityToVo(user);
		 return "user updated sucessfully";
	}

	//delete the user record  from the database using id
	@Override
	public String deleteUser(Long id) {
		
		Optional<UserModel> user = userrepo.findById(id);

		if (user.isPresent()) {
			user.get().removeStores();

			userrepo.deleteById(user.get().getId());
			return "user with id: " + id + " deleted successfully!";
		}
		throw new RecordNotFoundException("record not found");
	}
}
