package com.otsi.kalamandir.controller;
/*
 * in this class we are implementing creating delivery slip,update the delivery slip and close the day of POS
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otsi.kalamandir.model.DeliverySlipModel;
import com.otsi.kalamandir.service.DeliverySlipService;
import com.otsi.kalamandir.vo.DelivarySlipVo;


@RestController
public class DelivarySlipContoller {
	@Autowired
	private DeliverySlipService deliveryslipservice;

	/* create delivery slip by passing DelivarySlipVo object */
	@PostMapping(value = "/create")
	public ResponseEntity<?> createDeliverySlip(@RequestBody DelivarySlipVo dsvo) {

		DeliverySlipModel savedds = deliveryslipservice.createDeliverySlip(dsvo);
		return new ResponseEntity<>(savedds, HttpStatus.OK);
	}
	/*
	 * update delivery slip
	 * if we want update anything in deliverySlip we can update
	 * 
	 * @arguments- spId,DelivarySlipVo
	 */

	@PutMapping(value = "/updateDsSlip")
	public ResponseEntity<?> updateDeliverySlip(@RequestParam int spId, @RequestBody DelivarySlipVo dsvo) {

		DeliverySlipModel updatedds = deliveryslipservice.updateDeliverySlip(spId, dsvo);

		return new ResponseEntity<>(updatedds, HttpStatus.OK);

	}

	/* for closing the day of POS . 
	 * We need to close all the delivery based on the date .
	 *  Need to close all the delivery slips which are opened today
	 */
	@GetMapping(value = "daycloser")
	public ResponseEntity<?> dayclose() {

		String dayclose = deliveryslipservice.posDayClose();
		return new ResponseEntity<>(dayclose, HttpStatus.OK);

	}

}
