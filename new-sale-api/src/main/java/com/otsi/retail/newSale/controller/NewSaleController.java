package com.otsi.retail.newSale.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.otsi.retail.newSale.Exceptions.CustomerNotFoundExcecption;
import com.otsi.retail.newSale.common.CommonRequestMappigs;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.service.CustomerService;
import com.otsi.retail.newSale.service.NewSaleService;
import com.otsi.retail.newSale.vo.BarcodeVo;
import com.otsi.retail.newSale.vo.CustomerVo;
import com.otsi.retail.newSale.vo.DeliverySlipVo;
import com.otsi.retail.newSale.vo.GiftVoucherVo;
import com.otsi.retail.newSale.vo.InvoiceRequestVo;
import com.otsi.retail.newSale.vo.ListOfDeliverySlipVo;
import com.otsi.retail.newSale.vo.ListOfSaleBillsVo;
import com.otsi.retail.newSale.vo.NewSaleList;
import com.otsi.retail.newSale.vo.NewSaleResponseVo;
import com.otsi.retail.newSale.vo.NewSaleVo;
import com.otsi.retail.newSale.vo.UserDataVo;

/**
 * Controller class for accepting all the requests which are related to
 * CustomerSaving API, NewSale API, Create delivery slip API
 * 
 * @author Manikanta Guptha
 *
 */

@RestController
@RequestMapping(CommonRequestMappigs.NEW_SALE)
public class NewSaleController {

	private Logger log = LoggerFactory.getLogger(NewSaleController.class);

	@Autowired
	private NewSaleService newSaleService;

	@Autowired
	private CustomerService customerService;

	// Save customer details API
	@PostMapping(path = CommonRequestMappigs.SAVE_CUSTOMERDETAILS, produces = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> saveCustomerDetails(@Valid @RequestBody CustomerVo details) {
		log.info("Received Request to saveCustomerDetails :" + details.toString());
		try {
			String result = customerService.saveCustomerDetails(details);
			return new GateWayResponse<>(HttpStatus.OK,result,"");		
			
		} catch (Exception e) {
			log.error("exception :" + e);
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());		}
	}

	// Get customer details from Mobile number
	@GetMapping(path = CommonRequestMappigs.GET_CUSTOMERDETAILS_BY_MOBILENUMBER)
	public GateWayResponse<?> getCustomerByMobileNumber(@RequestParam String mobileNumber) {
		log.info("Received Request to getCustomerByMobileNumber :" + mobileNumber);
		CustomerVo customer;
		try {
			customer = customerService.getCustomerByMobileNumber(mobileNumber);
			return new GateWayResponse<>(HttpStatus.OK,customer,"");		
			} catch (CustomerNotFoundExcecption e) {
				return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}

	}

	// Method for saving new sale items...
	@PostMapping(CommonRequestMappigs.SALE)
	public GateWayResponse<?> saveNewSale(@RequestBody NewSaleVo vo) {
		log.info("Received Request to saveNewSale :" + vo.toString());
		try {
			String message = newSaleService.saveNewSaleRequest(vo);

			return new GateWayResponse<>( HttpStatus.OK,message);
			}catch (Exception e) {
				return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
			}
	}

	// Method for create new Barcode..
	@PostMapping(CommonRequestMappigs.CREATE_BARCODE)
	public GateWayResponse<?> saveBarcode(@RequestBody BarcodeVo vo) {
		log.info("Received Request to saveBarcode :" + vo.toString());
		try {
			String result = newSaleService.saveBarcode(vo);

			return new GateWayResponse<>( HttpStatus.OK,result,"");
			}catch (Exception e) {
				return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
			}
	}

	// Method for getting Barcode details from Barcode table using Barcode number
	@GetMapping(CommonRequestMappigs.GET_BARCODE_DETAILS)
	public GateWayResponse<?> getBarcodeDetails(@RequestParam String barCode, @RequestParam String smId) {
		log.info("Received Request to getBarcodeDetails:" + barCode);
		try {
			BarcodeVo barCodeDetails = newSaleService.getBarcodeDetails(barCode, smId);

		return new GateWayResponse<>( HttpStatus.OK,barCodeDetails,"");
		}catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}

	}

	// Method for creating Delivery slip using List of Barcodes
	@PostMapping(CommonRequestMappigs.CREATE_DS)
	public GateWayResponse<?> saveDeliverySlip(@RequestBody DeliverySlipVo vo, String enumName) {
		log.info("Received Request to saveDeliverySlip :" + vo.toString());
		try {
			String saveDs = newSaleService.saveDeliverySlip(vo, enumName);
			return new GateWayResponse<>( HttpStatus.OK,saveDs,"");
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

	// Method for getting Delivery slip Data using DsNumber
	@GetMapping(CommonRequestMappigs.GET_DS)
	public GateWayResponse<?> getDeliverySlipDetails(@RequestParam String dsNumber) {
		log.info("Received Request to getDeliverySlipDetails :" + dsNumber);
		try {
		     DeliverySlipVo dsDetails = newSaleService.getDeliverySlipDetails(dsNumber);
					return new GateWayResponse<>( HttpStatus.OK,dsDetails,"");

				} catch (Exception e) {
					log.error("exception :" + e.getMessage());
					return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
				}
	}

	// Method for getting list of sale bills
	@PostMapping(CommonRequestMappigs.GET_LISTOF_SALEBILLS)
	public GateWayResponse<?> getListOfSaleBills(@RequestBody ListOfSaleBillsVo svo) {
		log.info("Received Request to getListOfSaleBills :" + svo.toString());
		try {

			ListOfSaleBillsVo listOfSaleBills = newSaleService.getListOfSaleBills(svo);
			return new GateWayResponse<>( HttpStatus.OK,listOfSaleBills,"");
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());

		}
	}

	// Method for getting list of delivery slips

	@GetMapping(CommonRequestMappigs.GET_LISTOF_DS)
	public GateWayResponse<?> getlistofDeliverySlips(@RequestBody ListOfDeliverySlipVo listOfDeliverySlipVo) {
		log.info("Received Request to getlistofDeliverySlips :" + listOfDeliverySlipVo.toString());
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {

			ListOfDeliverySlipVo getDs = newSaleService.getlistofDeliverySlips(listOfDeliverySlipVo);

			return new GateWayResponse<>( HttpStatus.OK,getDs,"");
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());

		}

	}
	// Method for day closer

	@GetMapping(CommonRequestMappigs.DAY_CLOSER)
	public GateWayResponse<?> dayclose() {
		log.info("Recieved request to dayclose()");
		try {
			String dayclose = newSaleService.posDayClose();
			return new GateWayResponse<>(HttpStatus.OK,dayclose,"");
		} catch (Exception e) {
			log.error("exception :" + e.getMessage());
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());

		}

	}

	@GetMapping(CommonRequestMappigs.POS_CLOSEDAY)
	public GateWayResponse<?> posclose(@RequestParam Boolean posclose) {
		try {
			String dayclose = newSaleService.posClose(posclose);
			return new GateWayResponse<>(HttpStatus.OK,dayclose, "");
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

	@GetMapping(CommonRequestMappigs.GET_NEWSALEBYCUSTOMERID)
	public GateWayResponse<?> getNewsaleByCustomerId(@RequestParam Long customerId) {
		log.info("Recieved request to getNewsaleByCustomerId():" + customerId);
		try {
			List<NewSaleResponseVo> message = newSaleService.getNewsaleByCustomerId(customerId);
			return new GateWayResponse<>( HttpStatus.OK,message,"");
			}catch (Exception e) {
				return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
			}
	}

	@PostMapping(CommonRequestMappigs.UPDATE_NEWSALE)
	public GateWayResponse<?> updateNewSale(@RequestBody NewSaleResponseVo vo) {
		log.info("Recieved request to updateNewSale():" + vo.toString());
		try {
			NewSaleVo message = newSaleService.updateNewSale(vo);
			return new GateWayResponse<>(HttpStatus.OK,message,"");
			}catch (Exception e) {
				return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
			}
	}

	@PostMapping(value = "getInvoiceDetails", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public GateWayResponse<?> getInvoiceDetails(@RequestBody InvoiceRequestVo vo) {
		try {
			NewSaleList newSaleList = newSaleService.getInvoicDetails(vo);
			return new GateWayResponse<>( HttpStatus.OK,newSaleList,"");
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

	@GetMapping("/getCustomerFromNewSale/{mobileNo}")
	public GateWayResponse<?> getCustomerFromNewSale(@PathVariable String mobileNo) {
		try {
			CustomerVo customer = customerService.getCustomerByMobileNumber(mobileNo);
			return new GateWayResponse<>(HttpStatus.OK,customer, "");
		} catch (CustomerNotFoundExcecption ce) {
			return new GateWayResponse<>( HttpStatus.NOT_FOUND,ce.getMessage());
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

	@GetMapping("/getHsnDetails/{netAmt}")
	public GateWayResponse<?> getHsnDetails(@PathVariable double netAmt) {
		try {
			log.info("Recieved request to getNewSaleWithHsn()");
			double netamt=	newSaleService.getNewSaleWithHsn(netAmt);
			return new GateWayResponse<>( HttpStatus.OK,netamt,"");
		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		log.error("hsn details not found");
		throw new RuntimeException("hsn details not found");
	}

	@GetMapping("/tagCustomerToInvoice/{mobileNo}/{invoiceNo}")
	public GateWayResponse<?> tagCustomerToInvoice(@PathVariable String mobileNo, @PathVariable String invoiceNo) {
		try {
			newSaleService.tagCustomerToExisitingNewSale(mobileNo, Long.parseLong(invoiceNo));
		} catch (CustomerNotFoundExcecption e) {
			e.printStackTrace();
		}
		return null;

	}

	@GetMapping("/discTypes")
	public List<String> getDiscountsTypes() {

		List<String> discTypes = new ArrayList<>();

		discTypes.add("Promotion not applied");
		discTypes.add("RT return discount");
		discTypes.add("Mgnt. SPL Discount");	
		discTypes.add("Management discount");
		discTypes.add("DMG Discount");
		discTypes.add("Other");

		return discTypes;
	}
	
	// Method for saving GiftVoucher
	@PostMapping("/saveGv")
	public GateWayResponse<?> saveGiftVoucher(@RequestBody GiftVoucherVo vo) {
		try {
			String result = newSaleService.saveGiftVoucher(vo);
			return new GateWayResponse<>(HttpStatus.OK,result);
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,"Getting error while saving");
		}
	}

	// Method for getting Gift voucher by GV Number
	@GetMapping("/getGv")
	public GateWayResponse<?> getGiftVoucher(@RequestParam String gvNumber) {
		try {
			GiftVoucherVo result = newSaleService.getGiftVoucher(gvNumber);
			return new GateWayResponse<>( HttpStatus.OK,result,"");
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,"Exception occurs while fetching record..");
		}
	}

	// Method for saving Userdata
	@PostMapping("/saveuser")
	public GateWayResponse<?> saveUser(@RequestBody UserDataVo vo) {
		try {
			String message = customerService.saveUserData(vo);
			return new GateWayResponse<>( HttpStatus.OK,message,"");
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,"Exception occurs while saving data..");
		}
	}

	// Method for fetching user data by using mobile number
	@GetMapping("/getUserByMobileNo")
	public GateWayResponse<?> getUserByMobileNo(@RequestParam Long mobileNum) {
		try {
			UserDataVo user = customerService.getUserByMobileNo(mobileNum);

			return new  GateWayResponse<>( HttpStatus.OK,user,"");
		} catch (Exception e) {
			return new  GateWayResponse<>(HttpStatus.BAD_REQUEST,"Exception occurs while saving data.." );
		}
	}

	// Method for tagging Gift voucher to Customer
	@PostMapping("/tagCustomerToGv/{userId}/{gvId}")
	public GateWayResponse<?> tagCustomerToGv(@PathVariable Long userId, @PathVariable Long gvId) {
		try {
			String message = newSaleService.tagCustomerToGv(userId, gvId);
			return new GateWayResponse<>( HttpStatus.OK,message,"");
		} catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,"Exception occurs while saving data..");
		}
	}
	
	// Method for getting all Bar codes list
	@GetMapping("/getAllBarcodes")
	public GateWayResponse<?> getAllBarcodes() {
		try {
			List<BarcodeVo> listOfBarcodes = newSaleService.getAllBarcodes();
			return new GateWayResponse<>(HttpStatus.OK, listOfBarcodes, "Successfully fetched list of Barcodes");
		} catch (Exception e) {
			return new GateWayResponse<>(HttpStatus.BAD_REQUEST, e.getMessage(),
					"Exception occurs while fetching data");
		}
	}
	
	@PostMapping(CommonRequestMappigs.GET_BARCODES)
	//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	
	public GateWayResponse<?> getBarcodes(@RequestBody List<String> barCode) {
		log.info("Received Request to getBarcodeDetails:" + barCode);
		System.out.println("Received Request to getBarcodeDetails:" + barCode);
		try {
			List<BarcodeVo> barCodeDetails = newSaleService.getBarcodes(barCode);

		return new GateWayResponse<>( HttpStatus.OK,barCodeDetails,"");
		}catch (Exception e) {
			return new GateWayResponse<>( HttpStatus.BAD_REQUEST,e.getMessage());
		}

	}
}
