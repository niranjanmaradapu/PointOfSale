package com.otsi.retail.promotions.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.Pool_Rule;
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

		if (poolRuleVo.getIsForEdit()) {

			poolRule.setRuleNumber(poolRuleVo.getRuleNumber());
			poolRule.setUpdatedat(LocalDate.now());
		} else {

			poolRule.setRuleNumber(poolRuleVo.getRuleNumber()+1);
			System.out.println("RuleNumber is:: "+poolRule.getRuleNumber());
			poolRule.setCreatedat(LocalDate.now());
		}

		poolRule.setColumnName(poolRuleVo.getColumnName());
		poolRule.setRuleType(poolRuleVo.getRuleType());
		poolRule.setGivenValue(poolRuleVo.getGivenValue());
		poolRule.setOperatorSymbol(poolRuleVo.getOperatorSymbol());
		poolRule.setRuleNumber(poolRuleVo.getRuleNumber());

		return poolRule;

	}
	
	public List<Pool_Rule> PoolRuleVoToEntities(List<Pool_RuleVo> poolRuleVos){
		return poolRuleVos.stream().map(poolRuleVo -> convertPoolRuleVoToEntity(poolRuleVo)).collect(Collectors.toList());
	}


	public List<PromotionPoolVo> convertPoolEntityToVo(List<PoolEntity> poolEntity) {

		List<PromotionPoolVo> listOfPool = new ArrayList<>();

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
			listOfPool.add(vo);

			List<Pool_RuleVo> pool_RuleVo = new ArrayList<>();

			x.getPool_Rule().stream().forEach(a -> {
				Pool_RuleVo rule = new Pool_RuleVo();

				rule.setColumnName(a.getColumnName());
				rule.setRuleType(a.getRuleType());
				rule.setGivenValue(a.getGivenValue());
				rule.setOperatorSymbol(a.getOperatorSymbol());
				rule.setRuleNumber(a.getRuleNumber());
				rule.setLastModified(a.getUpdatedat());
				rule.setId(a.getId());

				pool_RuleVo.add(rule);

			});
			vo.setPool_RuleVo(pool_RuleVo);

		});

		return listOfPool;
	}
	
}
