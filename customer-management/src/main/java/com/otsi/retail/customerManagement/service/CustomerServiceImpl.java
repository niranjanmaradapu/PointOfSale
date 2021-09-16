/**
 * Service implementation for Customer
 */
package com.otsi.retail.customerManagement.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.otsi.retail.customerManagement.gatewayresponse.GateWayResponse;
import com.otsi.retail.customerManagement.mapper.ReturnSlipMapper;
import com.otsi.retail.customerManagement.model.Barcode;
import com.otsi.retail.customerManagement.model.ReturnSlip;
import com.otsi.retail.customerManagement.repo.BarcodeRepo;
import com.otsi.retail.customerManagement.repo.ReturnSlipRepo;
import com.otsi.retail.customerManagement.service.CustomerService;
import com.otsi.retail.customerManagement.utils.ReturnSlipStatus;
import com.otsi.retail.customerManagement.vo.CustomerDetailsVo;
import com.otsi.retail.customerManagement.vo.GenerateReturnSlipRequest;
import com.otsi.retail.customerManagement.vo.InvoiceRequestVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;
import com.otsi.retail.customerManagement.vo.NewSaleList;

/**
 * @author vasavi
 */
@Component
public class CustomerServiceImpl implements CustomerService {





	

	@Autowired
	private BarcodeRepo barCodeRepo;

	@Autowired
	private ReturnSlipRepo returnSlipRepo;

	@Autowired
	private ReturnSlipMapper returnSlipMapper;


	@Autowired
	private RestTemplate restTemplate;
	private final static String NEW_SALE_GET_INVOICEDETAILS_URL = "http://localhost:8081/newsale/getInvoiceDetails";
	private final static String GET_CUSTOMER_FROM_NEWSALE_URL = "http://localhost:8081/newsale/getCustomerFromNewSale";
	private final static String TAG_CUSTOMER_TO_INVOICE = "http://localhost:8081/newsale/tagCustomerToInvoice";

	@Override
	public List<ListOfReturnSlipsVo> getListOfReturnSlips(ListOfReturnSlipsVo vo) {
		List<ReturnSlip> retunSlipdetails = new ArrayList<>();
		/**
		 * getting the record using dates combination
		 *
		 */
		if (vo.getDateFrom() != null && vo.getDateTo() != null) {
			/**
			 * getting the record using dates and RtNumber
			 *
			 */
			if (vo.getRtNumber() != null && vo.getCreditNote() == null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndRtNoOrderByCreatedDateAsc(vo.getDateFrom(), vo.getDateTo(),
						vo.getRtNumber());
			}
			/**
			 * getting the record using dates and creditNote
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() != null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndCrNoOrderByCreatedDateAsc(vo.getDateFrom(), vo.getDateTo(),
						vo.getCreditNote());
			}
			/**
			 * getting the record using dates and RtStatus
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() == null && vo.getRtStatus() != null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndRtStatusOrderByCreatedDateAsc(vo.getDateFrom(), vo.getDateTo(),
						vo.getRtStatus());
			}
			/**
			 * getting the record using dates and RtReviewStatus
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() == null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() != null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndIsReviewedOrderByCreatedDateAsc(vo.getDateFrom(),
						vo.getDateTo(), vo.getRtReviewStatus());
			}
			/**
			 * getting the record using dates and barcode
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() == null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() != null) {
				
			retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndTaggedItems_barCodeOrderByCreatedDateAsc(vo.getDateFrom(),vo.getDateTo(),vo.getBarcode());
				
			}
			/**
			 * getting the record using dates only
			 *
			 */
			else
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenOrderByCreatedDateAsc(vo.getDateFrom(), vo.getDateTo());
			/**
			 * getting the records without dates
			 *
			 */
		} else if (vo.getDateFrom() == null && vo.getDateTo() == null) {
			/**
			 * getting the record using RtNumber
			 *
			 */
			if (vo.getRtNumber() != null && vo.getCreditNote() == null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByRtNoOrderByCreatedDateAsc(vo.getRtNumber());
			}
			/**
			 * getting the record using creditnote
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() != null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByRtNoOrderByCreatedDateAsc(vo.getCreditNote());
			}
			/**
			 * getting the record using RtStatus
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() == null && vo.getRtStatus() != null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByRtNoOrderByCreatedDateAsc(vo.getRtStatus());
			}
			/**
			 * getting the record using barcode
			 *
			 */

			else if (vo.getRtNumber() == null && vo.getCreditNote() == null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() == null && vo.getBarcode() != null) {
				
					retunSlipdetails = returnSlipRepo.findByTaggedItems_barCodeOrderByCreatedDateAsc(vo.getBarcode());
				
			}

			/**
			 * getting the record using RtReviewStatus
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreditNote() == null && vo.getRtStatus() == null
					&& vo.getRtReviewStatus() != null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByIsReviewedOrderByCreatedDateAsc(vo.getRtReviewStatus());
			}

		}

		List<ListOfReturnSlipsVo> rvo = returnSlipMapper.mapEntityToVo(retunSlipdetails);
		if (!rvo.isEmpty()) {
			return rvo;
		} else
			throw new RuntimeException("no record found with the giveninformation");
	}

	@Override
	public NewSaleList getInvoiceDetailsFromNewSale(InvoiceRequestVo vo) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<InvoiceRequestVo> entity = new HttpEntity<>(vo, headers);
		URI uri = null;
		try {
			uri = UriComponentsBuilder.fromUri(new URI(NEW_SALE_GET_INVOICEDETAILS_URL)).build().encode().toUri();

			ResponseEntity<NewSaleList> responce = restTemplate.exchange(uri, HttpMethod.POST, entity,
					NewSaleList.class);
			if (responce.getStatusCode() == HttpStatus.OK && responce.getStatusCode().equals(HttpStatus.OK)) {
				return responce.getBody();
			} else {
				throw new Exception("" + responce.getBody());
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public String createReturnSlip(GenerateReturnSlipRequest request) throws Exception {
		try {

			if(!request.getIsUserTagged()) {
				tagUserToInvoice(request.getMobileNumber(),request.getInvoiceNo());
			}
			
			ReturnSlip returnSlipDto = new ReturnSlip();
			returnSlipDto.setCrNo(generateCrNumber());
			returnSlipDto.setRtNo(generateRtNumber());
			returnSlipDto.setCreatedDate(LocalDate.now());
			returnSlipDto.setModifiedDate(LocalDate.now());
			returnSlipDto.setIsReviewed(Boolean.FALSE);
			returnSlipDto.setCreatedBy(request.getCreatedBy());
			returnSlipDto.setTaggedItems(request.getBarcodes());
			returnSlipDto.setRtStatus(ReturnSlipStatus.PENDING.getId());
			returnSlipDto.setAmount(request.getAmount());
			returnSlipRepo.save(returnSlipDto);
			return "Successfully saved "+returnSlipDto.getRtNo();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

	}

	private void tagUserToInvoice(String mobileNumber, String invoiceNo) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(headers);
		URI uri = null;
		try {
			uri = UriComponentsBuilder.fromUri(new URI(TAG_CUSTOMER_TO_INVOICE  + "/" + mobileNumber +"/"+invoiceNo)).build().encode()
					.toUri();
			
			ResponseEntity<String> res = restTemplate.exchange(uri, HttpMethod.GET, entity,
					String.class);
		} catch (URISyntaxException e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public CustomerDetailsVo getCustomerFDetailsFromInvoice(String mobileNo) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(headers);
		URI uri = null;
		try {
			uri = UriComponentsBuilder.fromUri(new URI(GET_CUSTOMER_FROM_NEWSALE_URL + "/" + mobileNo)).build().encode()
					.toUri();

			ResponseEntity<CustomerDetailsVo> res = restTemplate.exchange(uri, HttpMethod.GET, entity,
					CustomerDetailsVo.class);
			if (res.getStatusCode() == HttpStatus.OK) {
				System.out.println(res.getBody());
				return res.getBody();
			} else {
				throw new Exception("" + res.getBody());
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private String generateRtNumber() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		LocalDate currentdate = LocalDate.now();
		int month = currentdate.getMonth().getValue();
		int year = currentdate.getYear();
		return "RT" + timestamp.getTime();
	}

	private String generateCrNumber() {
		return null;
	}

	@Override
	public List<ListOfReturnSlipsVo> getAllListOfReturnSlips() {
		List<ReturnSlip> rmodel = returnSlipRepo.findAll();
		List<ListOfReturnSlipsVo> rvo = returnSlipMapper.mapEntityToVo(rmodel);
		
		return rvo;
	} 


	

}
