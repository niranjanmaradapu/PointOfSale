package com.otsi.kalamandhir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.kalamandhir.model.Reason;

@Repository
public interface ReasonRepo extends JpaRepository<Reason, Long> {

}
