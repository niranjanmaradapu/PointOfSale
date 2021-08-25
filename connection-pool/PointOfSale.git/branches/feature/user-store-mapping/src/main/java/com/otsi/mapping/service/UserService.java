package com.otsi.mapping.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.mapping.exceptions.DuplicateRecordException;
import com.otsi.mapping.vo.UserVo;
//this is the interface here we are writing  all the unimplemented methods
@Service
public interface UserService {

	ResponseEntity<?> addUser(UserVo userVo) throws DuplicateRecordException ;

	ResponseEntity<?> updateuser(Long id, UserVo userVo);

	String deleteUser(Long id);

	List<UserVo> getAllUsers();

}
