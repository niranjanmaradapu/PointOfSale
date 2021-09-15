package com.otsi.retail.userStore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.userStore.exceptions.DuplicateRecordException;
import com.otsi.retail.userStore.gatewayResponse.GateWayResponse;
import com.otsi.retail.userStore.service.UserService;
import com.otsi.retail.userStore.vo.UserVo;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private Logger log=LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	// create user by giving userVo object

	@PostMapping("/createuser")
	public GateWayResponse<?> createUser(@RequestBody UserVo userVo) throws DuplicateRecordException {
		log.info("Received request to createUser():"+userVo);
		try {
		String message = userService.addUser(userVo);
		return new GateWayResponse<>( HttpStatus.CREATED,message,"Success");
		}catch (Exception ex) {
			return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// update the existing user with the help of id

	@PutMapping("/updateuser/{id}")
	public GateWayResponse<?> updateExistingUser(@PathVariable(name = "id") Long id, @RequestBody UserVo userVo) {
		log.info("Received request to updateExistingUser()");
		try {
		String message = userService.updateuser(id, userVo);

		return new GateWayResponse<>( HttpStatus.CREATED,message,"Success");
       }catch (Exception ex) {
			return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}

	}
	// delete the user from database by using id

	@DeleteMapping("/disableuser/{id}")
	public GateWayResponse<?> disableUser(@PathVariable(name = "id") Long id) {
		log.info("Received request to disableUser():"+id);
		try
		{
		String message = userService.deleteUser(id);
		return new GateWayResponse<>( HttpStatus.CREATED,message,"Success");
		}catch (Exception ex) {
			return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}

	// get all the users

	@GetMapping("/getusers")
	public GateWayResponse<?> getAllUsers() {
		log.info("Received request to getAllUsers()");
		try
		{
			List<UserVo>  vO = userService.getAllUsers();
			return new GateWayResponse<>( HttpStatus.OK,vO,"Success");
			}catch (Exception ex) {
				return new GateWayResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
			}

	}

}
