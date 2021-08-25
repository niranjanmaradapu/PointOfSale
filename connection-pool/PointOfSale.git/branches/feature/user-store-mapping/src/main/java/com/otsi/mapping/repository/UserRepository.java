package com.otsi.mapping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.mapping.model.UserModel;
import com.otsi.mapping.vo.UserVo;
//purpose: for accessing database
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

	void save(UserVo userVo);

	boolean existsByMobile(String mobile);

}
