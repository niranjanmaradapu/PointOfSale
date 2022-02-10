package com.otsi.retail.promotions.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otsi.retail.promotions.entity.Pool_Rule;

@Repository
public interface RuleRepo extends JpaRepository<Pool_Rule, Long> {


	List<Pool_Rule> findByIdIn(List<Long> ruleIdList);


}
