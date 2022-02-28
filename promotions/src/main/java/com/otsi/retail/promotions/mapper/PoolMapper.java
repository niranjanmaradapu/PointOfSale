package com.otsi.retail.promotions.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.Condition;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.Pool_Rule;
import com.otsi.retail.promotions.vo.ConditionVo;
import com.otsi.retail.promotions.vo.Pool_RuleVo;
import com.otsi.retail.promotions.vo.PromotionPoolVo;

@Component
public class PoolMapper {

	public PoolEntity convertPoolVoToEntity(PromotionPoolVo vo) {

		PoolEntity poolEntity = new PoolEntity();

		poolEntity.setPoolName(vo.getPoolName());
		poolEntity.setDomainId(vo.getDomainId());
		poolEntity.setPoolType(vo.getPoolType());
		poolEntity.setCreatedBy(vo.getCreatedBy());
		poolEntity.setCreatedDate(LocalDate.now());
		poolEntity.setLastModified(LocalDate.now());
		poolEntity.setIsActive(Boolean.TRUE);

		if (vo.getIsForEdit()) {
			poolEntity.setModifiedBy(vo.getModifiedBy());

		}

		return poolEntity;
	}

	public Pool_Rule convertPoolRuleVoToEntity(Pool_RuleVo poolRuleVo) {

		Pool_Rule poolRule = new Pool_Rule();

		List<Condition> listOfConditions = new ArrayList<>();

		if (poolRuleVo.getIsForEdit()) {

			poolRule.setRuleNumber(poolRuleVo.getRuleNumber());
			poolRule.setUpdatedAt(LocalDate.now());

			poolRuleVo.getConditionVos().stream().forEach(c -> {

				Condition condition = new Condition();
				condition.setId(c.getId());
				condition.setColumnName(c.getColumnName());
				condition.setGivenValues(c.getGivenValues());
				condition.setOperatorSymbol(c.getOperatorSymbol());
				condition.setUpdatedAt(LocalDate.now());
				listOfConditions.add(condition);
			});

		} else {

			poolRule.setRuleNumber(poolRuleVo.getRuleNumber() + 1);
			System.out.println("RuleNumber is:: " + poolRule.getRuleNumber());
			poolRule.setCreatedAt(LocalDate.now());

			poolRuleVo.getConditionVos().stream().forEach(c -> {

				Condition condition = new Condition();
				condition.setColumnName(c.getColumnName());
				condition.setGivenValues(c.getGivenValues());
				condition.setOperatorSymbol(c.getOperatorSymbol());
				condition.setCreatedAt(LocalDate.now());
				listOfConditions.add(condition);
			});

		}
		poolRule.setConditions(listOfConditions);
		poolRule.setRuleType(poolRuleVo.getRuleType());

		return poolRule;

	}

	
	public List<Pool_Rule> PoolRuleVoToEntities(List<Pool_RuleVo> poolRuleVos) {
		return poolRuleVos.stream().map(poolRuleVo -> convertPoolRuleVoToEntity(poolRuleVo))
				.collect(Collectors.toList());
	}

	public List<PromotionPoolVo> convertPoolEntityToVo(List<PoolEntity> poolEntity) {

		List<PromotionPoolVo> listOfPool = new ArrayList<>();
		List<Pool_RuleVo> pool_RuleVo = new ArrayList<>();
		List<ConditionVo> conditionsVo = new ArrayList<>();

		poolEntity.stream().forEach(x -> {

			PromotionPoolVo vo = new PromotionPoolVo();

			vo.setPoolName(x.getPoolName());
			vo.setPoolId(x.getPoolId());
			vo.setDomainId(x.getDomainId());
			vo.setCreatedBy(x.getCreatedBy());
			vo.setCreatedDate(x.getCreatedDate());
			vo.setPoolType(x.getPoolType());
			vo.setIsActive(x.getIsActive());
			vo.setLastModified(x.getLastModified());

			x.getPool_Rule().stream().forEach(a -> {
				Pool_RuleVo rule = new Pool_RuleVo();

				rule.setRuleType(a.getRuleType());
				rule.setRuleNumber(a.getRuleNumber());
				rule.setLastModified(a.getUpdatedAt());
				rule.setId(a.getId());

				a.getConditions().stream().forEach(cond -> {

					ConditionVo condition = new ConditionVo();
					condition.setId(cond.getId());
					condition.setColumnName(cond.getColumnName());
					condition.setGivenValues(cond.getGivenValues());
					condition.setOperatorSymbol(cond.getOperatorSymbol());
					conditionsVo.add(condition);
					vo.setPool_RuleVo(pool_RuleVo);
				});
				
				rule.setConditionVos(conditionsVo);
				pool_RuleVo.add(rule);
			});
			
			listOfPool.add(vo);

		});

		return listOfPool;
	}

}
