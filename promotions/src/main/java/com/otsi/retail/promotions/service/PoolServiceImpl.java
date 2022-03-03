package com.otsi.retail.promotions.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.retail.promotions.entity.Condition;
import com.otsi.retail.promotions.entity.PoolEntity;
import com.otsi.retail.promotions.entity.Pool_Rule;
import com.otsi.retail.promotions.exceptions.DuplicateRecordException;
import com.otsi.retail.promotions.exceptions.InvalidDataException;
import com.otsi.retail.promotions.exceptions.RecordNotFoundException;
import com.otsi.retail.promotions.mapper.PoolMapper;
import com.otsi.retail.promotions.repository.ConditionRepo;
import com.otsi.retail.promotions.repository.PoolRepo;
import com.otsi.retail.promotions.repository.RuleRepo;
import com.otsi.retail.promotions.vo.PoolVo;
import com.otsi.retail.promotions.vo.PromotionPoolVo;
import com.otsi.retail.promotions.vo.SearchPoolVo;

/**
 * This class having Bussiness logics for all pool services
 * 
 * @author Manikanta Guptha
 *
 */
@Service
public class PoolServiceImpl implements PoolService {

	private Logger log = LogManager.getLogger(PoolServiceImpl.class);

	@Autowired
	private PoolMapper poolMapper;

	@Autowired
	private PoolRepo poolRepo;

	@Autowired
	private RuleRepo ruleRepo;
	
	@Autowired
	private ConditionRepo conditionRepo;

	// Method for saving pools from PromotionPoolVO
	@Override
	public String savePool(PromotionPoolVo vo) {
		log.debug("debugging savePool():" + vo);
		if (vo.getPool_RuleVo() == null) {
			throw new InvalidDataException("please enter valid data");
		}

		PoolEntity savedPool = null;
		PoolEntity pool = null;

		if (vo.getPoolId() != null) {

			pool = poolRepo.findById(vo.getPoolId()).get();
			savedPool = pool;
		}

		if (pool == null) {
			PoolEntity poolEntity = poolMapper.convertPoolVoToEntity(vo);
			savedPool = poolRepo.save(poolEntity);
		}
		PoolEntity savePool = savedPool;
		vo.getPool_RuleVo().forEach(x -> {

			Pool_Rule poolRule = poolMapper.convertPoolRuleVoToEntity(x);
			poolRule.setPoolEntity(savePool);
			ruleRepo.save(poolRule);

		});
		log.warn("we are checking if pool is saved...");
		log.info("pool saved Successfully");
		return "Pool saved Successfully with id: " + savedPool.getPoolId();

	}
	// Method for create rules if pool exists from PromotionPoolVO
	@Override
	public String poolExistsCreateRules(PromotionPoolVo vo) {

		if (vo.getPool_RuleVo() == null && vo.getPoolId() != null) {
			throw new InvalidDataException("please enter valid data");
		}

		Optional<PoolEntity> pool = poolRepo.findById(vo.getPoolId());

		if (pool.isPresent()) {

			vo.getPool_RuleVo().forEach(x -> {

				Pool_Rule poolRules = poolMapper.convertPoolRuleVoToEntity(x);
				poolRules.setPoolEntity(pool.get());
				ruleRepo.save(poolRules);
          
			});

		} else {

			throw new RecordNotFoundException("Pool Not Found");
		}

		return "Rules Created If Pool Exists: " + pool.get().getPoolId();
	}
   
	// Method for modifying/edit existing pools and rules from PromotionPoolVO
	@Override
	public String modifyPool(PromotionPoolVo vo) {
		log.debug("debugging modifyPool():" + vo);
		if (vo.getPool_RuleVo() == null) {
			throw new InvalidDataException("please enter valid data");
		}

		Optional<PoolEntity> pool = poolRepo.findById(vo.getPoolId());

		if (pool.isPresent()) {

			PoolEntity poolEntity = poolMapper.convertPoolVoToEntity(vo);

			poolEntity.setPoolId(vo.getPoolId());
			poolEntity.setLastModified(LocalDate.now());

			PoolEntity savedPool = poolRepo.save(poolEntity);

			vo.getPool_RuleVo().forEach(x -> {

				Optional<Pool_Rule> rule = ruleRepo.findById(x.getId());
				if (rule.isPresent()) {
					Pool_Rule poolRules = poolMapper.convertPoolRuleVoToEntity(x);
					poolRules.setPoolEntity(savedPool);
					poolRules.setId(x.getId());
					ruleRepo.save(poolRules);
				}

			});

		} else {
			log.error("record not found");
			throw new RecordNotFoundException("record not found");
		}
		log.warn("we are checking if pool is modify...");
		log.info("Pool Modified Successfully...");
		return "Pool Modified Successfully...";

	}

	// Method for getting list of pools based on the status flag
	@Override
	public PoolVo getListOfPools(String isActive, Long domainId) {
		log.debug("debugging savePool():" + isActive);
		List<PoolEntity> poolEntity = new ArrayList<>();
		Boolean flag = null;

		if (isActive.equalsIgnoreCase("true")) {
			flag = Boolean.TRUE;
		}
		if (isActive.equalsIgnoreCase("false")) {
			flag = Boolean.FALSE;
		}

		if (isActive.equalsIgnoreCase("ALL") && domainId == null) {
			poolEntity = poolRepo.findAll();

		} else if (!(isActive.isEmpty()) && domainId == null) {
			poolEntity = poolRepo.findByIsActive(flag);
		} else if (isActive.isEmpty() && domainId != null) {

			poolEntity = poolRepo.findByDomainId(domainId);
			poolEntity.stream().forEach(p -> {

				p.setDomainId(null);
			});
		}

		else {
			poolEntity = poolRepo.findByIsActiveAndDomainId(flag, domainId);
			poolEntity.stream().forEach(p -> {

				p.setDomainId(null);
			});

		}
		if (!poolEntity.isEmpty()) {
			PoolVo poolvo = new PoolVo();
					
			
			List<PromotionPoolVo> poolVo = poolMapper.convertPoolEntityToVo(poolEntity);
			log.warn("we are checking if pool is fetching...");
			log.info("fetching list of pools");

			poolvo.setPoolvo(poolVo);
			poolvo.setDomainId(domainId);
			return poolvo;

		} else {
			log.error("record not found");
			throw new RecordNotFoundException("record not found");
		}
	} 
	
	// Method for delete the existing pool details
	@Override
	public String deletePool(Long poolId) {
		if (poolRepo.findByPoolId(poolId).isPresent()) {
			if (poolRepo.findByPoolId(poolId).get().getPromoEntity().size() == 0) {
				poolRepo.deleteById(poolId);
			} else {
				log.error("record not found");
				throw new DuplicateRecordException("promotions mapped with existing pool");
			}

		}
		// TODO Auto-generated method stub
		return "pool deleted sucessfully";
	}
	
	// Method for searchPools from SearchPoolVo
	@Override
	public List<PromotionPoolVo> searchPool(SearchPoolVo pvo) {

		List<SearchPoolVo> searchPoolvo = new ArrayList<>();
		List<PoolEntity> pools = new ArrayList<>();
		if (pvo.getIsActive() != null) {
			if (pvo.getCreatedBy() != null && pvo.getPoolType() != null) {

				pools = poolRepo.findByCreatedByAndPoolTypeAndIsActive(pvo.getCreatedBy(), pvo.getPoolType(),
						pvo.getIsActive());

			} else if (pvo.getCreatedBy() != null && pvo.getPoolType() == null) {

				pools = poolRepo.findByCreatedByAndIsActive(pvo.getCreatedBy(), pvo.getIsActive());
			} else if (pvo.getPoolType() != null && pvo.getCreatedBy() == null) {
				pools = poolRepo.findByPoolTypeAndIsActive(pvo.getPoolType(), pvo.getIsActive());
			} else {

				pools = poolRepo.findByIsActive(pvo.getIsActive());
			}

		} else if (pvo.getIsActive() == null) {

			if (pvo.getCreatedBy() != null && pvo.getPoolType() == null) {

				pools = poolRepo.findByCreatedBy(pvo.getCreatedBy());

			} else if (pvo.getCreatedBy() == null && pvo.getPoolType() != null) {

				pools = poolRepo.findByPoolType(pvo.getPoolType());

			} else {

				pools = poolRepo.findByCreatedByAndPoolType(pvo.getCreatedBy(), pvo.getPoolType());
			}

		}

		if (pools.isEmpty()) {

			throw new RecordNotFoundException("Records not exists");
		}

		List<PromotionPoolVo> vo = poolMapper.convertPoolEntityToVo(pools);

		return vo;
	}

}
