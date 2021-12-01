package com.otsi.retail.connectionpool.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.otsi.retail.connectionpool.vo.ConnectionPoolVo;
import com.otsi.retail.connectionpool.vo.PoolVo;
import com.otsi.retail.connectionpool.vo.SearchPoolVo;

/**
 * This interface is implemented by PoolServiceImpl
 * 
 * @author Manikanta Guptha
 *
 */
@Component
public interface PoolService {

	String savePool(ConnectionPoolVo vo);

	PoolVo getListOfPools(String isActive, Long domainId);

	String modifyPool(ConnectionPoolVo vo);

	String deletePool(Long poolId);

	List<SearchPoolVo> searchPool(SearchPoolVo pvo);

	

}
