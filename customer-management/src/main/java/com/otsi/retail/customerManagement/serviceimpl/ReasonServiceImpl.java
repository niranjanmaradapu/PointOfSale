package com.otsi.kalamandhir.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.kalamandhir.model.Reason;
import com.otsi.kalamandhir.repo.ReasonRepo;
@Service
public class ReasonServiceImpl implements ReasonService {

	@Autowired
	private ReasonRepo reasonRepo;

	@Override
	public List<Reason> getAllReasons() {
		return reasonRepo.findAll();
	}

	@Override
	public String saveReason(Reason reason) {
		Reason entity = new Reason();
		if (reason.getReason() != null&& !reason.getReason().isEmpty()) {
		entity.setReason(reason.getReason());
		entity.setCreatedDate(LocalDate.now());
		entity.setModifiedDate(LocalDate.now());

			reasonRepo.save(entity);
			return "Saved Successfully";

		} else {
			return "reason is required";
		}

	}

	@Override
	public String deleteReason(Long reasonId) {

		if (reasonId != 0) {
			reasonRepo.deleteById(reasonId);
			return "Deleted Successfully";

		} else {
			return "reasonId is required";
		}
	}

}
