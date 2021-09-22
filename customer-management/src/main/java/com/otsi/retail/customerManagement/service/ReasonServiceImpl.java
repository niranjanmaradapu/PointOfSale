package com.otsi.retail.customerManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.retail.customerManagement.exceptions.DataNotFoundException;
import com.otsi.retail.customerManagement.exceptions.InvalidDataException;
import com.otsi.retail.customerManagement.exceptions.RecordNotFoundException;
import com.otsi.retail.customerManagement.model.Reason;
import com.otsi.retail.customerManagement.repo.ReasonRepo;

@Service
public class ReasonServiceImpl implements ReasonService {

	@Autowired
	private ReasonRepo reasonRepo;

	@Override
	public List<Reason> getAllReasons() {

		List<Reason> reasons = reasonRepo.findAll();

		if (reasons.isEmpty()) {
			throw new DataNotFoundException("Data not found");
		}
		return reasons;
	}

	@Override
	public String saveReason(Reason reason) {
		Reason entity = new Reason();
		if (reason.getReason() != null && !reason.getReason().isEmpty()) {
			entity.setReason(reason.getReason());
			entity.setCreatedDate(LocalDate.now());
			entity.setModifiedDate(LocalDate.now());

			reasonRepo.save(entity);
			return "Saved Successfully";

		} else {
			throw new InvalidDataException("please enter some data");
		}

	}

	@Override
	public String deleteReason(Long reasonId) {
		
		Optional<Reason> reason = reasonRepo.findById(reasonId);
		if(!reason.isPresent()){
			throw new RecordNotFoundException("Record Not Found");
		}

		if (reasonId != 0) {
			reasonRepo.deleteById(reasonId);
			return "Deleted Successfully";

		} else {
			return "reasonId is required";
		}
	}

}
