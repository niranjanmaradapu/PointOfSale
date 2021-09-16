package com.otsi.kalamandhir.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otsi.kalamandhir.model.Reason;

@Service
public interface ReasonService {

	public List<Reason> getAllReasons();
	public String saveReason(Reason reason);
	public String deleteReason(Long reasonId) ;
}
