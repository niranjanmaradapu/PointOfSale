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
import java.util.stream.Collectors;

import com.otsi.retail.customerManagement.exceptions.ServiceDownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otsi.retail.customerManagement.config.Config;
import com.otsi.retail.customerManagement.controller.ReasonController;
import com.otsi.retail.customerManagement.exceptions.DataNotFoundException;
import com.otsi.retail.customerManagement.exceptions.InvalidDataException;
import com.otsi.retail.customerManagement.exceptions.RecordNotFoundException;
import com.otsi.retail.customerManagement.gatewayresponse.GateWayResponse;
import com.otsi.retail.customerManagement.mapper.ReturnSlipMapper;
import com.otsi.retail.customerManagement.model.Barcode;
import com.otsi.retail.customerManagement.model.ReturnSlip;
import com.otsi.retail.customerManagement.model.TaggedItems;
import com.otsi.retail.customerManagement.repo.BarcodeRepo;
import com.otsi.retail.customerManagement.repo.ReturnSlipRepo;
import com.otsi.retail.customerManagement.service.CustomerService;
import com.otsi.retail.customerManagement.utils.ReturnSlipStatus;
import com.otsi.retail.customerManagement.vo.BarcodeVo;
import com.otsi.retail.customerManagement.vo.CustomerDetailsVo;
import com.otsi.retail.customerManagement.vo.GenerateReturnSlipRequest;
import com.otsi.retail.customerManagement.vo.HsnDetailsVo;
import com.otsi.retail.customerManagement.vo.InvoiceRequestVo;
import com.otsi.retail.customerManagement.vo.LineItemVo;
import com.otsi.retail.customerManagement.vo.ListOfReturnSlipsVo;
import com.otsi.retail.customerManagement.vo.NewSaleList;
import com.otsi.retail.customerManagement.vo.RetrnSlipDetailsVo;
import com.otsi.retail.customerManagement.vo.TaxVo;

/**
 * @author vasavi
 */
@Component
public class CustomerServiceImpl implements CustomerService {

	private Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private BarcodeRepo barCodeRepo;

	@Autowired
	private HSNVoService hsnService;

	@Autowired
	private ReturnSlipRepo returnSlipRepo;

	@Autowired
	private ReturnSlipMapper returnSlipMapper;

	@Autowired
	Config config;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<ListOfReturnSlipsVo> getListOfReturnSlips(ListOfReturnSlipsVo vo) {
		log.debug("debugging getListOfReturnSlips():" + vo);
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
			if (vo.getRtNumber() != null && vo.getBarcode() == null && vo.getCreatedBy() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndRtNoOrderByCreatedDateAsc(vo.getDateFrom(),
						vo.getDateTo(), vo.getRtNumber());
			}

			/**
			 * getting the record using dates and barcode
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreatedBy() == null && vo.getBarcode() != null) {

				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndTaggedItems_barCodeOrderByCreatedDateAsc(
						vo.getDateFrom(), vo.getDateTo(), vo.getBarcode());

			} else if (vo.getRtNumber() == null && vo.getCreatedBy() != null && vo.getBarcode() == null) {

				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenAndCreatedByOrderByCreatedDateAsc(
						vo.getDateFrom(), vo.getDateTo(), vo.getCreatedBy());

			}
			/**
			 * getting the record using dates only
			 *
			 */
			else
				retunSlipdetails = returnSlipRepo.findByCreatedDateBetweenOrderByCreatedDateAsc(vo.getDateFrom(),
						vo.getDateTo());
			/**
			 * getting the records without dates
			 *
			 */
		} else if (vo.getDateFrom() == null && vo.getDateTo() == null) {
			/**
			 * getting the record using RtNumber
			 *
			 */
			if (vo.getRtNumber() != null && vo.getCreatedBy() == null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByRtNoOrderByCreatedDateAsc(vo.getRtNumber());
			}

			/**
			 * getting the record using barcode
			 *
			 */

			else if (vo.getRtNumber() == null && vo.getCreatedBy() == null && vo.getBarcode() != null) {

				retunSlipdetails = returnSlipRepo.findByTaggedItems_barCodeOrderByCreatedDateAsc(vo.getBarcode());

			}

			/**
			 * getting the record using RtReviewStatus
			 *
			 */
			else if (vo.getRtNumber() == null && vo.getCreatedBy() != null && vo.getBarcode() == null) {
				retunSlipdetails = returnSlipRepo.findByCreatedByOrderByCreatedDateAsc(vo.getCreatedBy());
			}

		}

		List<ListOfReturnSlipsVo> rvo = returnSlipMapper.mapReturnEntityToVo(retunSlipdetails);

		if (rvo != null) {
			log.warn("we are checking if list of return slips is fetching...");
			log.info("fetching list of return slips successfully:" + rvo);
			return rvo;
		} else
			log.error("No return slips are found");
		// throw new RuntimeException("no record found with the giveninformation");
		throw new DataNotFoundException("No return slips are found");
	}
	
	
	
	

	@Override
	@CircuitBreaker(name = "newSale", fallbackMethod = "getInvoiceFallback")
	public List<ReturnSlipVo> getInvoiceDetailsFromNewSale(InvoiceRequestVo vo) throws Exception {
		log.debug("debugging getInvoiceDetailsFromNewSale():" + vo);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<InvoiceRequestVo> entity = new HttpEntity<>(vo, headers);
		URI uri = null;
		try {
			uri = UriComponentsBuilder.fromUri(new URI(config.getInvoiceDetails())).build().encode().toUri();

			ResponseEntity<GateWayResponse> responce = restTemplate.exchange(uri, HttpMethod.POST, entity,
					GateWayResponse.class);

			if (responce.getStatusCode() == HttpStatus.OK && responce.getStatusCode().equals(HttpStatus.OK)) {
				log.warn("we are checking if invoice details is fetching from newsale...");
				log.info("fetching invoice details from newsale:" + responce.getBody());
				ObjectMapper mapper = new ObjectMapper();
				List<ReturnSlipVo> bvo = mapper.convertValue(responce.getBody().getResult(),
						new TypeReference<List<ReturnSlipVo>>() {
						});
				return bvo;
			} else {
				log.error("Invoice details are not exists" + responce.getBody());
				throw new RecordNotFoundException("Invoice details are not exists" + responce.getBody());
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public NewSaleList getInvoiceFallback(InvoiceRequestVo vo) {
		log.error("Invocie details service down");
		throw new ServiceDownException("Invoice details are down");
	}

	@Override
	public String createReturnSlip(GenerateReturnSlipRequest request) throws Exception {
		/*
		 * log.debug("debugging createReturnSlip:" + request); if
		 * (!request.getIsUserTagged()) { tagUserToInvoice(request.getMobileNumber(),
		 * request.getInvoiceNo()); } List<TaggedItems> tgItems = request.getBarcodes();
		 * 
		 * List<String> barcodes = tgItems.stream().map(x ->
		 * x.getBarCode()).collect(Collectors.toList());
		 * 
		 * HttpHeaders headers = new HttpHeaders();
		 * 
		 * HttpEntity<List<String>> request1 = new HttpEntity<List<String>>(barcodes,
		 * headers);
		 * 
		 * ResponseEntity<GateWayResponse> newsaleResponse =
		 * restTemplate.exchange(config.getGetbarcodesUrl(), HttpMethod.POST, request1,
		 * GateWayResponse.class);
		 */
		/*
		 * System.out.println("Received Request to getBarcodeDetails:" +
		 * newsaleResponse); ObjectMapper mapper = new ObjectMapper().registerModule(new
		 * JavaTimeModule()) .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
		 * false);
		 */
		// GateWayResponse<?> gatewayResponse =
		// mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		/*
		 * List<BarcodeVo> bvo =
		 * mapper.convertValue(newsaleResponse.getBody().getResult(), new
		 * TypeReference<List<BarcodeVo>>() { });
		 */
		ReturnSlip returnSlipDto = new ReturnSlip();
		returnSlipDto.setCrNo(generateCrNumber());
		returnSlipDto.setRtNo(generateRtNumber());
		returnSlipDto.setCreatedDate(LocalDate.now());
		returnSlipDto.setModifiedDate(LocalDate.now());
		returnSlipDto.setIsReviewed(Boolean.FALSE);
		returnSlipDto.setCreatedBy(request.getCreatedBy());
		returnSlipDto.setTaggedItems(request.getBarcodes());
		returnSlipDto.setRtStatus(ReturnSlipStatus.PENDING.getId());
		returnSlipDto.setAmount(request.getTotalAmount());
		returnSlipDto.setMobileNumber(request.getMobileNumber());
		returnSlipDto.setCustomerName(request.getCustomerName());
		returnSlipDto.setDomianId(request.getDomianId());
		returnSlipRepo.save(returnSlipDto);
		
		log.warn("we are checking if return slip is saved...");
		log.info("Successfully saved " + returnSlipDto.getRtNo());
		return "Successfully saved " + returnSlipDto.getRtNo();

	}

	private void tagUserToInvoice(String mobileNumber, String invoiceNo) throws Exception {
		log.debug("debugging tagUserToInvoice():" + mobileNumber + "and the invoice is:" + invoiceNo);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(headers);
		URI uri = null;
		try {
			uri = UriComponentsBuilder
					.fromUri(new URI(config.getTagCustomerToInvoice() + "/" + mobileNumber + "/" + invoiceNo)).build()
					.encode().toUri();

			ResponseEntity<GateWayResponse> res = restTemplate.exchange(uri, HttpMethod.GET, entity,
					GateWayResponse.class);
			log.info("tagging user to invoice:" + res);
		} catch (URISyntaxException e) {
			throw new Exception(e.getMessage());
		}
	}

	@Override
	public CustomerDetailsVo getCustomerFDetailsFromInvoice(String mobileNo) throws Exception {
		log.debug("debugging getCustomerFDetailsFromInvoice:" + mobileNo);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(headers);
		URI uri = null;
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		try {
			uri = UriComponentsBuilder.fromUri(new URI(config.getCustomerDetails() + "/" + mobileNo)).build().encode()
					.toUri();

			ResponseEntity<GateWayResponse> res = restTemplate.exchange(uri, HttpMethod.GET, entity,
					GateWayResponse.class);
			if (res.getStatusCode() == HttpStatus.OK) {
				log.info("fetching customer details from invoice:" + res.getBody());

				CustomerDetailsVo vo = mapper.convertValue(res.getBody().getResult(), CustomerDetailsVo.class);
				return vo;
			} else {
				log.error("Mobile number not exists" + res.getBody());
				throw new RecordNotFoundException("Mobile number not exists" + res.getBody());
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
		log.debug("debugging getAllListOfReturnSlips()");
		List<ReturnSlip> rmodel = returnSlipRepo.findAll();
		if (rmodel.isEmpty()) {
			log.error("No return slips are found");
			throw new DataNotFoundException("No return slips are found");
		}
		List<ListOfReturnSlipsVo> rvo = returnSlipMapper.mapReturnEntityToVo(rmodel);
		log.info("fetching list of return slips successfully:" + rvo);
		return rvo;
	}

	@Override

	public RetrnSlipDetailsVo ReturnSlipsDeatils(String rtNumber) throws JsonMappingException, JsonProcessingException, URISyntaxException {
		log.debug("debugging ReturnSlipsDeatils():" + rtNumber);
		ReturnSlip rts = returnSlipRepo.findByRtNo(rtNumber);
		if (rts == null) {
			log.error("given RT number is not exists");
			throw new RecordNotFoundException("given RT number is not exists");
		}
		URI uri = null;

		List<RetrnSlipDetailsVo> lvo = new ArrayList<>();

		List<TaggedItems> tgItems = rts.getTaggedItems();

		List<String> barcodes = tgItems.stream().map(x -> x.getBarCode()).collect(Collectors.toList());

		System.out.println("Received Request to getBarcodeDetails:" + barcodes);

		HttpHeaders headers = new HttpHeaders();
		uri = UriComponentsBuilder.fromUri(new URI(config.getGetbarcodesUrl() + "/" + rts.getDomianId())).build()
				.encode().toUri();
		

		HttpEntity<List<String>> request = new HttpEntity<List<String>>(barcodes, headers);

		ResponseEntity<?> newsaleResponse = restTemplate.exchange(uri, HttpMethod.POST, request,
				GateWayResponse.class);

		System.out.println("Received Request to getBarcodeDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<LineItemVo> bvo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<LineItemVo>>() {
		});

		List<HsnDetailsVo> list = new ArrayList<>();
		List<LineItemVo> liVo = new ArrayList<>();
		

		bvo.stream().forEach(x -> {

			try {
				HsnDetailsVo hsnDetails = getHsnDetails(x.getNetValue());
				LineItemVo iVo = new LineItemVo() ;
				iVo.setBarCode(x.getBarCode());
				iVo.setCreationDate(x.getCreationDate());
				iVo.setDiscount(x.getDiscount());
				iVo.setDomainId(x.getDomainId());
				iVo.setGrossValue(x.getGrossValue());
				iVo.setItemPrice(x.getItemPrice());
				iVo.setNetValue(x.getNetValue());
				iVo.setLastModified(x.getLastModified());
				iVo.setQuantity(x.getQuantity());
				iVo.setSection(x.getSection());
				list.add(hsnDetails);
				iVo.setHsnDetailsVo(hsnDetails);
				liVo.add(iVo);
				

			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		RetrnSlipDetailsVo rrvo = new RetrnSlipDetailsVo();

		// HsnDetailsVo HsnDetails = getHsnDetails(rts.getAmount());

		rrvo.setBarcode(liVo);
		//rrvo.setHsnCode(list);
		rrvo.setTotalQty(bvo.stream().mapToInt(q -> q.getQuantity()).sum());
		rrvo.setTaxableAmount(list.stream().mapToDouble(t -> t.getTaxVo().getTaxableAmount()).sum());
		rrvo.setTotalCgst(list.stream().mapToDouble(c -> c.getTaxVo().getCgst()).sum());
		rrvo.setTotalSgst(list.stream().mapToDouble(s -> s.getTaxVo().getSgst()).sum());
		rrvo.setTotalIgst(list.stream().mapToDouble(i -> i.getTaxVo().getIgst()).sum());
		rrvo.setTotalNetAmount(bvo.stream().mapToLong(n -> n.getNetValue()).sum());
		rrvo.setCreatedDate(rts.getCreatedDate());
		rrvo.setRtNumber(rts.getRtNo());
		rrvo.setMobileNumber(rts.getMobileNumber());
		rrvo.setCustomerName(rts.getCustomerName());
		rrvo.setCreatedBy(rts.getCreatedBy());
		log.info("return slip details:" + rrvo);
		return rrvo;
	}

	@Override
	public HsnDetailsVo getHsnDetails(double netAmt) throws JsonMappingException, JsonProcessingException {
		log.debug("debugging getHsnDetails:" + netAmt);
		List<HsnDetailsVo> vo = hsnService.getHsn();
		if (vo == null) {
			log.error("Record not found");
			new RecordNotFoundException("Record not found");
		}
		TaxVo tvo = new TaxVo();
		HsnDetailsVo hvo = new HsnDetailsVo();

		vo.stream().forEach(x -> {

			x.getSlabVos().stream().forEach(a -> {

				if (a.getPriceFrom() <= netAmt && netAmt <= a.getPriceTo()) {

					tvo.setTaxableAmount((float) netAmt / (1 + (a.getTaxVo().getCess() / 100)));

					tvo.setGst(a.getTaxVo().getCess());
					tvo.setSgst((float) ((netAmt - tvo.getTaxableAmount()) / 2));
					tvo.setCgst((float) ((netAmt - tvo.getTaxableAmount()) / 2));
					tvo.setTaxLabel(a.getTaxVo().getTaxLabel());
					if (a.getTaxVo().getIgst() != 0.00) {
						tvo.setIgst(tvo.getCgst() + tvo.getSgst());

					} else {
						tvo.setIgst((float) 0.00);
					}
					hvo.setHsnCode(x.getHsnCode());
					hvo.setTaxVo(tvo);

				}
			});
		});
		log.warn("we are checking if hsn details is fetching...");
		log.info("fetching hsn details successfuuly:" + hvo);
		return hvo;

	}

	@Override
	public String updateReturnSlip(String rtNumber, GenerateReturnSlipRequest request) {
		log.debug("debugging getHsnDetails:" + rtNumber + " and the request is:" + request);
		ReturnSlip rts = returnSlipRepo.findByRtNo(rtNumber);
		if (rts == null) {
			log.error("RT Number not found");
			throw new RecordNotFoundException("RT Number not found");
		}
		// ReturnSlip returnSlipDto = new ReturnSlip();
		rts.setIsReviewed(true);

		returnSlipRepo.save(rts);
		log.warn("we wre checking if return slip is updated..");
		log.info("Successfully updated " + rts.getRtNo());
		return "Successfully updated " + rts.getRtNo();
	}





	@Override
	public String deleteReturnSlips(int rsId) {
		ReturnSlip lvo=new ReturnSlip();
		lvo = returnSlipRepo.findByRsId(rsId);
		if(lvo!=null) {
			
			returnSlipRepo.delete(lvo);
		}
		else
		{
			throw new RecordNotFoundException("Record not found");		
		}
		
		return "Record Deleted successfully";
	}





	@Override
	public ReturnSlip getReturnSlipByrtNo(String rtNo) throws Exception {
		
		if(rtNo==null || rtNo.equals(null)) {
			throw new Exception("Please give valid returnslip number ");
		}
		
		ReturnSlip returnSlip=	returnSlipRepo.findByRtNo(rtNo);
		if(returnSlip!=null) {
			if(returnSlip.getRtStatus()==ReturnSlipStatus.PENDING.getId()) {
				
			ReturnSlipVo vo=	returnSlipMapper.convertDtoToVo(returnSlip);
				return returnSlip;
			}else {
				throw new Exception("returnSlip already cliamed or canceled ");
			}
			
		}else {
			throw new Exception("returnSlip details no found with rtNumber : "+rtNo);
		}
		
	}

}
