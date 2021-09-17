package com.otsi.retail.connectionpool.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otsi.retail.connectionpool.entity.PoolEntity;
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

	@Autowired
	private PoolMapper poolMapper;

	@Autowired
	private PoolRepo poolRepo;

	@Autowired
	private RuleRepo ruleRepo;

	// Method for saving pools from Connection VO
	@Override
	public String savePool(ConnectionPoolVo vo) throws Exception {

		try {
			PoolEntity poolEntity = poolMapper.convertPoolVoToEntity(vo);

			PoolEntity savedPool = poolRepo.save(poolEntity);

			poolEntity.getRuleEntity().forEach(x -> {

				x.setPoolEntity(savedPool);
				ruleRepo.save(x);
			});

			return "Pool saved Successfully";

		} catch (Exception e) {
			throw new Exception("Exception occurs while saving the Pool");
		}
	}

	// Method for getting list of pools based on the status flag
	@Override
	public List<ConnectionPoolVo>  getListOfPools(String isActive) throws Exception {
		try {
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
				return poolVo;

			} else {
				throw new Exception("No data found");
			}
		} catch (Exception e) {
			throw new Exception("Getting error while fetching data");
		}
	}

	// Method for Modifying/Edit the existing pool details
	@Override
	public String modifyPool(ConnectionPoolVo vo) throws Exception {

		try {

			Optional<PoolEntity> pool = poolRepo.findById(vo.getPoolId());

			if (pool.isPresent()) {

				PoolEntity poolEntity = poolMapper.convertPoolVoToEntity(vo);

				poolEntity.setPoolId(vo.getPoolId());
				poolEntity.setLastModified(LocalDate.now());

				PoolEntity savedPool = poolRepo.save(poolEntity);

			} else {
				return "Invalid Pool Id";
			}
			throw new Exception("Pool Modified Successfully...");
		} catch (Exception e) {
			throw new Exception("Exception occurs while modifying the record.");
		}
	}
}
