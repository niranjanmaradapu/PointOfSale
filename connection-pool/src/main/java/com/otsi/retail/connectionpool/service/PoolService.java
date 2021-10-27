package com.otsi.retail.connectionpool.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;

/**
 * This interface is implemented by PoolServiceImpl
 * 
 * @author Manikanta Guptha
 *
 */
@Component
public interface PoolService {

	String savePool(ConnectionPoolVo vo);

	List<ConnectionPoolVo> getListOfPools(String isActive);

	String modifyPool(ConnectionPoolVo vo);

	String deletePool(Long poolId);

}
