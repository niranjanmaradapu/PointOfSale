package com.otsi.retail.customerManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otsi.retail.customerManagement.model.Reason;

@Service
public interface ReasonService {

	public List<Reason> getAllReasons();
	public String saveReason(Reason reason);
	public String deleteReason(Long reasonId) ;
}
