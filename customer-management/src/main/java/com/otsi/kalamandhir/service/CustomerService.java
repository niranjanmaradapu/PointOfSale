/**
 * service for Customer
 */
package com.otsi.kalamandhir.service;

import org.springframework.stereotype.Service;
import com.otsi.kalamandhir.vo.CustomerVo;
import com.otsi.kalamandhir.vo.SearchFilterVo;

/**
 * @author vasavi
 */
@Service
public interface CustomerService {

	CustomerVo generateReturnSlip(CustomerVo vo);

	CustomerVo searchbyName(SearchFilterVo vo);

	CustomerVo saveCustomer(CustomerVo vo);
}
