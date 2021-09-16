package com.otsi.retail.customerManagement.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.customerManagement.model.Reason;

@Repository
public interface ReasonRepo extends JpaRepository<Reason, Long> {

}
