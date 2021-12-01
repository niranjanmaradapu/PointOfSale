package com.otsi.retail.connectionpool.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.otsi.retail.connectionpool.entity.PoolEntity;
import com.otsi.retail.connectionpool.exceptions.DuplicateRecordException;
import com.otsi.retail.connectionpool.exceptions.InvalidDataException;
import com.otsi.retail.connectionpool.exceptions.RecordNotFoundException;
import com.otsi.retail.connectionpool.mapper.PoolMapper;
import com.otsi.retail.connectionpool.repository.PoolRepo;
import com.otsi.retail.connectionpool.repository.RuleRepo;
import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.PoolVo;
import com.otsi.retail.connectionpool.vo.SearchPoolVo;

/**
 * This class having Bussiness logics for all pool services
 * 
 * @author Manikanta Guptha
 *
 */
@Service
public class PoolServiceImpl implements PoolService {

	private Logger log = LoggerFactory.getLogger(PoolServiceImpl.class);

	@Autowired
	private PoolMapper poolMapper;

	@Autowired
	private PoolRepo poolRepo;

	@Autowired
	private RuleRepo ruleRepo;

	// Method for saving pools from Connection VO
	@Override
	public String savePool(ConnectionPoolVo vo) {
		log.debug("debugging savePool():" + vo);
		if (vo.getRuleVo() == null) {
			throw new InvalidDataException("please enter valid data");
		}
		/* try { */
		PoolEntity poolEntity = poolMapper.convertPoolVoToEntity(vo);

		PoolEntity savedPool = poolRepo.save(poolEntity);

		poolEntity.getRuleEntity().forEach(x -> {

			x.setPoolEntity(savedPool);
			ruleRepo.save(x);
		});
		log.warn("we are checking if pool is saved...");
		log.info("pool saved Successfully");
		return "Pool saved Successfully";

		/*
		 * catch (Exception e) { log.error("please give valid data"); throw new
		 * InvalidDataException("please give valid data"); }
		 */
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
			List<ConnectionPoolVo> poolVo = poolMapper.convertPoolEntityToVo(poolEntity);
			log.warn("we are checking if pool is fetching...");
			log.info("fetching list of pools");

			poolvo.setPoolvo(poolVo);
			poolvo.setDomainId(domainId);
			return poolvo;

		} else {
			log.error("record not found");
			throw new RecordNotFoundException("record not found");
		}
	} /*
		 * catch (Exception e) {
		 * System.out.println("Getting error while fetching data"); throw new
		 * RecordNotFoundException("record not found"); }
		 */

	// Method for Modifying/Edit the existing pool details
	@Override
	public String modifyPool(ConnectionPoolVo vo) {
		log.debug("debugging modifyPool():" + vo);
		if (vo.getRuleVo() == null) {
			throw new InvalidDataException("please enter valid data");
		}

		Optional<PoolEntity> pool = poolRepo.findById(vo.getPoolId());

		if (pool.isPresent()) {

			PoolEntity poolEntity = poolMapper.convertPoolVoToEntity(vo);

			poolEntity.setPoolId(vo.getPoolId());
			poolEntity.setLastModified(LocalDate.now());

			PoolEntity savedPool = poolRepo.save(poolEntity);

		} else {
			log.error("record not found");
			throw new RecordNotFoundException("record not found");
		}
		log.warn("we are checking if pool is modify...");
		log.info("Pool Modified Successfully...");
		return "Pool Modified Successfully...";

	}

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

	@Override
	public List<SearchPoolVo> searchPool(SearchPoolVo pvo) {

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

		} else {

			pools.stream().forEach(p -> {

				SearchPoolVo spvo = new SearchPoolVo();
				spvo.setPoolId(p.getPoolId());
				spvo.setPoolName(p.getPoolName());
				spvo.setPoolType(p.getPoolType());
				spvo.setCreatedBy(p.getCreatedBy());
				spvo.setCreatedDate(p.getCreatedDate());
				spvo.setIsActive(p.getIsActive());
				searchPoolvo.add(spvo);

			});

		}

		return searchPoolvo;
	}

}
