package com.otsi.mapping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.mapping.exceptions.DuplicateRecordException;
import com.otsi.mapping.service.UserService;
import com.otsi.mapping.vo.UserVo;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	// create user by giving userVo object

	@PostMapping("/user")
	public ResponseEntity<?> createUser(@RequestBody UserVo userVo) throws DuplicateRecordException {
		ResponseEntity<?> user = userService.addUser(userVo);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	// update the existing user with the help of id

	@PutMapping("/user/{id}")
	public ResponseEntity<?> updateExistingUser(@PathVariable(name = "id") Long id, @RequestBody UserVo userVo) {

		ResponseEntity<?> user = userService.updateuser(id, userVo);

		return new ResponseEntity<>(user, HttpStatus.CREATED);

	}
	// delete the user from database by using id

	@DeleteMapping("/disableuser/{id}")
	public ResponseEntity<String> disableUser(@PathVariable(name = "id") Long id) {
		String message = userService.deleteUser(id);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// get all the users

	@GetMapping("/getusers")
	public List<UserVo> getAllUsers() {
		return userService.getAllUsers();

	}

}
