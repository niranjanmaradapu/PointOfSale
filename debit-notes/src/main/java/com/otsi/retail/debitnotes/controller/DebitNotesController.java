package com.otsi.retail.debitnotes.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.debitnotes.common.CommonRequestMappings;
import com.otsi.retail.debitnotes.exceptions.DataNotFoundException;
import com.otsi.retail.debitnotes.gatewayresponse.GateWayResponse;
import com.otsi.retail.debitnotes.model.DebitNotes;
import com.otsi.retail.debitnotes.service.DebitNotesService;
import com.otsi.retail.debitnotes.vo.CustomerVo;
import com.otsi.retail.debitnotes.vo.DebitNotesVo;
import com.otsi.retail.debitnotes.vo.NewSaleResponseVo;

@RestController
@RequestMapping(CommonRequestMappings.DEBIT_NOTES)
public class DebitNotesController {

	private Logger log = LoggerFactory.getLogger(DebitNotesController.class);

	@Autowired
	private DebitNotesService debitNotesService;

	@PostMapping(path = CommonRequestMappings.SAVE_DEBITNOTES, produces = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> saveDebitNotes(@Valid @RequestBody DebitNotesVo debitNotesVo)
			throws DataNotFoundException {
		log.info("Received Request to saveDebitNotes : " + debitNotesVo);
		DebitNotesVo debitNotesSave = debitNotesService.saveDebitNotes(debitNotesVo);
		return new GateWayResponse<>("saved debit-notes successfully", debitNotesSave);

	}

	@GetMapping(path = CommonRequestMappings.GET_DEBITNOTESBYDRNO)
	public GateWayResponse<?> getByDrNo(@RequestParam String drNo) {
		log.info("Received Request to getByDrNo : " + drNo);
		Optional<DebitNotes> debitNotes = debitNotesService.getDebitNotesByDrNo(drNo);
		return new GateWayResponse<>("fetching debit notes successfully with drNo", debitNotes);
	}

	@GetMapping(path = CommonRequestMappings.GET_ALLDEBITNOTES)
	public GateWayResponse<?> getAllDebitNotes() {
		log.info("Received Request to getAllDebitNotes");
		List<DebitNotes> allDebitNotes = debitNotesService.getAllDebitNotes();
		return new GateWayResponse<>("fetching all debit notes sucessfully", allDebitNotes);

	}

	@PostMapping(path = CommonRequestMappings.SAVE_LISTDEBITNOTES)
	public GateWayResponse<?> saveListDebitNotes(@RequestBody List<DebitNotesVo> debitNotesVos) {
		log.info("Received Request to saveListDebitNotes:" + debitNotesVos);
		List<DebitNotesVo> saveVoList = debitNotesService.saveListDebitNotes(debitNotesVos);
		return new GateWayResponse<>("saving list of debit notes", saveVoList);

	}

	@GetMapping(path = CommonRequestMappings.GET_DEBITWITHCUSTOMERMOBILENO)
	public GateWayResponse<CustomerVo> getDebitWithCustomerMobileno(String mobileNumber) {
		log.info("Received Request to getDebitWithCustomerMobileno:" + mobileNumber);
		CustomerVo customerMobile = debitNotesService.getDebitWithCustomerMobileno(mobileNumber);
		return new GateWayResponse<>("fetching customer mobile number successfully from newsale", customerMobile);

	}

	@GetMapping(path = CommonRequestMappings.GET_NEWSALEBYCUSTOMERID)
	public GateWayResponse<?> getNewsaleByCustomerId(Long CustomerId) {
		List<NewSaleResponseVo> vo = debitNotesService.getNewsaleByCustomerId(CustomerId);
		return new GateWayResponse<>("fetching customerId successfully from newsale", vo);

	}
}
