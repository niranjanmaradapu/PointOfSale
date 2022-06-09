package com.otsi.retail.promotions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.entity.Condition;

@Repository
public interface ConditionRepo extends JpaRepository<Condition, Long> {

}
