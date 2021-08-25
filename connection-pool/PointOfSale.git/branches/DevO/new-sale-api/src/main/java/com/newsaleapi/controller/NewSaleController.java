package com.newsaleapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.newsaleapi.common.CommonRequestMappigs;
import com.newsaleapi.service.NewSaleService;
import com.newsaleapi.vo.BarcodeVo;
import com.newsaleapi.vo.CustomerDetails;
import com.newsaleapi.vo.DeliverySlipVo;

@RestController
@CrossOrigin
@RequestMapping(CommonRequestMappigs.NEW_SALE)
public class NewSaleController {

	@Autowired
	private NewSaleService newSaleService;

	@PostMapping(CommonRequestMappigs.SALE)
	public ResponseEntity<?> saveNewSale(@RequestBody CustomerDetails vo) {

		ResponseEntity<?> message = newSaleService.saveNewSaleRequest(vo);

		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	@PostMapping(CommonRequestMappigs.CREATE_BARCODE)
	public ResponseEntity<?> saveBarcode(@RequestBody BarcodeVo vo) {

		ResponseEntity<?> result = newSaleService.saveBarcode(vo);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping(CommonRequestMappigs.GET_BARCODE_DETAILS)
	public ResponseEntity<?> getBarcodeDetails(@RequestParam String barCode) {

		ResponseEntity<?> barCodeDetails = newSaleService.getBarcodeDetails(barCode);

		return new ResponseEntity<>(barCodeDetails, HttpStatus.OK);

	}

	@PostMapping(CommonRequestMappigs.CREATE_DS)
	public ResponseEntity<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo) {

		ResponseEntity<?> saveDs = newSaleService.saveDeliverySlip(vo);

		return new ResponseEntity<>(saveDs, HttpStatus.OK);

	}
	
	/*
	 * rest call(getting tax details from hsn-details)
	 */

	@GetMapping(CommonRequestMappigs.GET_TAX_Details)
	public ResponseEntity<?> getTaxDetails() {

		try {
			return new ResponseEntity<>(newSaleService.getNewSaleWithTax(), HttpStatus.OK);
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return null;
         }

	/*
	 * @PostMapping(CommonRequestMappigs.CREATE_DS) public ResponseEntity<?>
	 * saveDeliverySlip(@RequestBody DeliverySlipVo vo) {
	 * 
	 * ResponseEntity<?> saveDs = newSaleService.saveDeliverySlip(vo);
	 * 
	 * return new ResponseEntity<>(saveDs, HttpStatus.OK);
	 * 
	 * }
	 */

}
