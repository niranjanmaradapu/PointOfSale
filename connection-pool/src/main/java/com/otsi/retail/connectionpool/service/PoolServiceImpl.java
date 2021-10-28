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

		 /*catch (Exception e) {
			log.error("please give valid data");
			throw new InvalidDataException("please give valid data");
		}*/
	}

	// Method for getting list of pools based on the status flag
	@Override
	public List<ConnectionPoolVo> getListOfPools(String isActive) {
		log.debug("debugging savePool():" + isActive);
		List<PoolEntity> poolEntity = new ArrayList<>();
		Boolean flag = null;

		if (isActive.equalsIgnoreCase("true")) {
			flag = Boolean.TRUE;
		}
		if (isActive.equalsIgnoreCase("false")) {
			flag = Boolean.FALSE;
		}

		if (isActive.equalsIgnoreCase("ALL")) {
			poolEntity = poolRepo.findAll();
		} else {
			poolEntity = poolRepo.findByIsActive(flag);
		}
		if (!poolEntity.isEmpty()) {

			List<ConnectionPoolVo> poolVo = poolMapper.convertPoolEntityToVo(poolEntity);
			log.warn("we are checking if pool is fetching...");
			log.info("fetching list of pools");
			return poolVo;

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
		if(poolRepo.findByPoolId(poolId).isPresent()) {
			if(poolRepo.findByPoolId(poolId).get().getPromoEntity().size()==0) {
				poolRepo.deleteById(poolId);
				}
			else {
				log.error("record not found");
				throw new DuplicateRecordException("promotions mapped with existing pool");
			}
			
		}
		// TODO Auto-generated method stub
		return "pool deleted sucessfully";
	}
}
