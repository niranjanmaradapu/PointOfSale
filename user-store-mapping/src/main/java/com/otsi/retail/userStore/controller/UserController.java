package com.otsi.retail.userStore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.retail.userStore.exceptions.DuplicateRecordException;
import com.otsi.retail.userStore.gatewayresponse.GateWayResponse;
import com.otsi.retail.userStore.service.UserService;
import com.otsi.retail.userStore.vo.UserVo;

@RestController
@RequestMapping("/users")
public class UserController {

	private Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	// create user by giving userVo object

	@PostMapping("/createuser")
	public GateWayResponse<?> createUser(@RequestBody UserVo userVo) throws DuplicateRecordException {
		log.info("Recieved request to createUser():" + userVo);
		String message = userService.addUser(userVo);
		return new GateWayResponse<>(HttpStatus.CREATED, message);

	}

	// update the existing user with the help of id

	@PutMapping("/updateuser/{id}")
	public GateWayResponse<?> updateExistingUser(@PathVariable(name = "id") Long id, @RequestBody UserVo userVo) {
		log.info("Recieved request to updateExistingUser():" + id + "and the user is:" + userVo);
		String message = userService.updateuser(id, userVo);
		return new GateWayResponse<>(HttpStatus.CREATED, message);

	}
	// delete the user from database by using id

	@DeleteMapping("/disableuser/{id}")
	public GateWayResponse<?> disableUser(@PathVariable(name = "id") Long id) {
		log.info("Recieved request to disableUser():" + id);
		String message = userService.deleteUser(id);
		return new GateWayResponse<>(HttpStatus.CREATED, message);

	}

	// get all the users

	@GetMapping("/o")
	public GateWayResponse<?> getAllUsers() {
		log.info("Recieved request to getAllUsers()");
		List<UserVo> vO = userService.getAllUsers();
		return new GateWayResponse<>(HttpStatus.OK, vO, "Success");

	}

}