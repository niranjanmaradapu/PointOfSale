package com.otsi.retail.customerManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.retail.customerManagement.exceptions.DataNotFoundException;
import com.otsi.retail.customerManagement.exceptions.InvalidDataException;
import com.otsi.retail.customerManagement.exceptions.RecordNotFoundException;
import com.otsi.retail.customerManagement.model.Reason;
import com.otsi.retail.customerManagement.repo.ReasonRepo;

@Service
public class ReasonServiceImpl implements ReasonService {

	private Logger log = LoggerFactory.getLogger(ReasonServiceImpl.class);

	@Autowired
	private ReasonRepo reasonRepo;

	@Override
	public List<Reason> getAllReasons() {
		log.debug("debugging getAllReasons()");
		List<Reason> reasons = reasonRepo.findAll();

		if (reasons.isEmpty()) {
			log.error("Data not found");
			throw new DataNotFoundException("Data not found");
		}
		log.warn("wea re checking if all reasons are fetching..");
		log.info("fetching all reasons succesfully:" + reasons);
		return reasons;
	}

	@Override
	public String saveReason(Reason reason) {
		log.debug("debugging saveReason():" + reason);
		Reason entity = new Reason();
		if (reason.getReason() != null && !reason.getReason().isEmpty()) {
			entity.setReason(reason.getReason());
			entity.setCreatedDate(LocalDate.now());
			entity.setModifiedDate(LocalDate.now());

			reasonRepo.save(entity);
			log.warn("we are checking if  reasons are saved..");
			log.info("reasons saved  succesfully:" + entity);
			return "Saved Successfully";

		} else {
			log.error("please enter some data");
			throw new InvalidDataException("please enter some data");
		}

	}

	@Override
	public String deleteReason(Long reasonId) {
		log.debug("debugging deleteReason():" + reasonId);
		Optional<Reason> reason = reasonRepo.findById(reasonId);
		if (!reason.isPresent()) {
			log.error("Record Not Found");
			throw new RecordNotFoundException("Record Not Found");
		}

		if (reasonId != 0) {
			reasonRepo.deleteById(reasonId);
			log.warn("we are checking if  reason is deleted..");
			log.info("reasons deleted  succesfully:" + reasonId);
			return "Deleted Successfully";

		} else {
			log.error("reasonId is required");
			return "reasonId is required";
		}
	}

}
