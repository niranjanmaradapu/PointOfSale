package com.otsi.retail.newSale.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otsi.retail.newSale.Entity.UserData;

public interface UserDataRepo extends JpaRepository<UserData, Long> {

	Optional<UserData> findByPhoneNumber(Long phoneNumber);

}
