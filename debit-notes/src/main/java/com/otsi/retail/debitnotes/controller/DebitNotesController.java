package com.otsi.retail.debitnotes.controller;

import java.util.List;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.otsi.retail.debitnotes.common.CommonRequestMappings;
import com.otsi.retail.debitnotes.gatewayresponse.GateWayResponse;
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
	public GateWayResponse<?> saveDebitNotes(@Valid @RequestBody DebitNotesVo debitNotesVo) {
		log.info("Received Request to saveDebitNotes : " + debitNotesVo);
		try {

			DebitNotesVo debitNotesSave = debitNotesService.saveDebitNotes(debitNotesVo);
			return new GateWayResponse<>(HttpStatus.OK, debitNotesSave.toString());
		} catch (Exception e) {
			log.error("message:" + e.getMessage());
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping(path = CommonRequestMappings.GET_DEBITNOTESBYDRNO)
	public GateWayResponse<?> getByDrNo(@RequestParam String drNo) {
		log.info("Received Request to getByDrNo : " + drNo);
		return new GateWayResponse<>(debitNotesService.getDebitNotesByDrNo(drNo));
	}

	@GetMapping(path = CommonRequestMappings.GET_ALLDEBITNOTES)
	public GateWayResponse<?> getAllDebitNotes() {
		log.info("Received Request to getAllDebitNotes");
		return new GateWayResponse<>(debitNotesService.getAllDebitNotes());

	}

	@PostMapping(path = CommonRequestMappings.SAVE_LISTDEBITNOTES)
	public GateWayResponse<?> saveListDebitNotes(@RequestBody List<DebitNotesVo> debitNotesVos) {
		log.info("Received Request to saveListDebitNotes:" + debitNotesVos);
		List<DebitNotesVo> saveVoList = debitNotesService.saveListDebitNotes(debitNotesVos);
		return new GateWayResponse<>(HttpStatus.OK, saveVoList.toString());

	}

	@GetMapping(path = CommonRequestMappings.GET_DEBITWITHCUSTOMERMOBILENO)
	public GateWayResponse<CustomerVo> getDebitWithCustomerMobileno(String mobileNumber) {
		log.info("Received Request to getDebitWithCustomerMobileno:" + mobileNumber);
		return new GateWayResponse<>(debitNotesService.getDebitWithCustomerMobileno(mobileNumber));

	}

	@GetMapping(path = CommonRequestMappings.GET_NEWSALEBYCUSTOMERID)
	public GateWayResponse<?> getNewsaleByCustomerId(Long CustomerId) {
		List<NewSaleResponseVo> vo = debitNotesService.getNewsaleByCustomerId(CustomerId);
		return new GateWayResponse<>(HttpStatus.OK, vo.toString());

	}
}
