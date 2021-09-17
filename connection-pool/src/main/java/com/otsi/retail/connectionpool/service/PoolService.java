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

	String savePool(ConnectionPoolVo vo) throws Exception;

	List<ConnectionPoolVo> getListOfPools(String isActive) throws Exception;

	String modifyPool(ConnectionPoolVo vo) throws Exception;

}
