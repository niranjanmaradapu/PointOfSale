package com.otsi.retail.newSale.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
import com.otsi.retail.newSale.Entity.LineItemsEntity;
import com.otsi.retail.newSale.Entity.LineItemsReEntity;
import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.common.DomainData;
import com.otsi.retail.newSale.config.Config;
import com.otsi.retail.newSale.gatewayresponse.GateWayResponse;
import com.otsi.retail.newSale.repository.DeliverySlipRepository;
import com.otsi.retail.newSale.repository.LineItemReRepo;
import com.otsi.retail.newSale.repository.LineItemRepo;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.vo.ListOfReturnSlipsVo;
import com.otsi.retail.newSale.vo.ReportVo;
import com.otsi.retail.newSale.vo.StoreVo;
import com.otsi.retail.newSale.vo.UserDetailsVo;

@Service
public class ReportsServiceImp implements ReportService {

	@Autowired
	private NewSaleRepository newsaleRepo;

	@Autowired
	private DeliverySlipRepository dsRepo;

	@Autowired
	private LineItemRepo lineItemRepo;

	@Autowired
	private LineItemReRepo lineItemReRepo;

	@Autowired
	private RestTemplate template;

	@Autowired
	private Config config;

	public List<StoreVo> getStoresForGivenId(List<Long> storeIds) throws URISyntaxException {
		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getStoreDetails())).build().encode().toUri();

		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(storeIds, headers);

		ResponseEntity<?> newsaleResponse = template.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);

		System.out.println("Received Request to getBarcodeDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<StoreVo> bvo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<StoreVo>>() {
		});
		return bvo;

	}

	public List<UserDetailsVo> getUsersForGivenIds(List<Long> userIds) throws URISyntaxException {

		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getUserDetails())).build().encode().toUri();

		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(userIds, headers);

		ResponseEntity<?> newsaleResponse = template.exchange(uri, HttpMethod.POST, request, GateWayResponse.class);

		System.out.println("Received Request to getBarcodeDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<UserDetailsVo> uvo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<UserDetailsVo>>() {
				});
		return uvo;

	}

	@Override
	public List<ReportVo> getInvoicesGeneratedDetails(Long storeId, Long domainId) {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();

		LocalDate dateto = LocalDate.now();
		LocalDate dateFrom = dateto.minusMonths(11);

		List<Object[]> nsentity = newsaleRepo.getByStoreIdAndDomainIdAndCreatedDateBetween(storeId, domainId,dateFrom,dateto);
		if (nsentity.size() > 0) {
		    for (int j = 0; j < nsentity.size(); j++) {
		        Object[] object = nsentity.get(j);
		        BigDecimal amount = (BigDecimal) object[0];
		        Double month = (Double) object[1];
		        
		        Long amountLong = amount.longValue();
		        Long Month =month.longValue();
		        ReportVo r = new ReportVo();
		        r.setAmount(amountLong);
		        r.setMonth(Month);
                lrvo.add(r);
		        
		    }
		}
		
		return lrvo;
	}

	@Override
	public List<ReportVo> getSaleMonthyTrendDetails() {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();
		LocalDate lastYear = Date.minusYears(1);

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
		List<NewSaleEntity> nen = nsentity.stream().filter(a -> a.getCreatedDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		return null;

	}

	@Override
	public List<ReportVo> getTopfiveSalesByStore(Long domainId) {
		List<ReportVo> rvo = new ArrayList<ReportVo>();

		LocalDate dateTo = LocalDate.now();
		LocalDate dateFrom = dateTo.with(TemporalAdjusters.firstDayOfMonth());

		List<Object[]> nsentity = newsaleRepo.findByDomainIdAndCreatedDateBetween(domainId,dateFrom,dateTo);
		/*List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());*/
		if (nsentity.size() > 0) {
		    for (int j = 0; j < nsentity.size(); j++) {
		        Object[] object = nsentity.get(j);
		        BigInteger storeId = (BigInteger) object[0];
		        BigDecimal amount = (BigDecimal) object[1];
		        
		        long amountLong = amount.longValue();
		        long storeIdLong =storeId.longValue();
		        ReportVo r = new ReportVo();
		        r.setAmount(amountLong);
		        r.setStoreId(storeIdLong);
                rvo.add(r);
		        
		    }
		}
		List<Long> storeIds = rvo.stream().map(a -> a.getStoreId()).distinct().collect(Collectors.toList());

		/*storeIds.stream().forEach(n -> {
			ReportVo vo = new ReportVo();
			// List<NewSaleEntity> newsaleDetails = newsaleRepo.findByStoreId(n);
			List<NewSaleEntity> en = nen.stream().filter(a -> a.getStoreId().equals(n)).collect(Collectors.toList());
			Long amount = en.stream().mapToLong(a -> a.getNetValue()).sum();
			vo.setAmount(amount);
			vo.setStoreId(n);

			rvo.add(vo);
		});

		List<ReportVo> sorted = rvo.stream().sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
				.collect(Collectors.toList());
		List<ReportVo> first5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());
		List<Long> sIds = first5ElementsList.stream().map(s -> s.getStoreId()).collect(Collectors.toList());
		List<StoreVo> svos = new ArrayList<>();*/
		List<StoreVo> svos = new ArrayList<>();
		try {
			svos = getStoresForGivenId(storeIds);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rvo.size() == svos.size()) {

			svos.stream().forEach(s -> {

				rvo.stream().forEach(r -> {

					if (s.getId() == r.getStoreId()) {

						r.setName(s.getName());
					}

				});

			});

		}

		return rvo;
	}

	@Override
	public List<ReportVo> getsaleSummeryDetails(Long storeId, Long domainId) {

		List<ReportVo> rvo = new ArrayList<ReportVo>();

		LocalDate dateTo = LocalDate.now();
		LocalDate dateFrom =dateTo.with(TemporalAdjusters.firstDayOfMonth());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ListOfReturnSlipsVo lrvo = new ListOfReturnSlipsVo();
		lrvo.setDateFrom(dateFrom);
		lrvo.setDateTo(dateTo);
		lrvo.setDomainId(domainId);
		lrvo.setStoreId(storeId);
		;
		HttpEntity<ListOfReturnSlipsVo> entity = new HttpEntity<>(lrvo, headers);

		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetListOfReturnSlips(),
				HttpMethod.POST, entity, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		List<ListOfReturnSlipsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<ListOfReturnSlipsVo>>() {
				});
		
		Long ramount = vo.stream().mapToLong(a -> a.getAmount()).sum();
		ReportVo revo = new ReportVo();
		revo.setName("ReturnInvoice");
		revo.setAmount(ramount);
		rvo.add(revo);

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainIdAndCreatedDateBetween(storeId, domainId,dateFrom,dateTo);
		
		Long amount = nsentity.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();
		rsvo.setName("SaleInvoice");
		rsvo.setAmount(amount);
		rvo.add(rsvo);

		return rvo;
	}

	@Override
	public ReportVo getTodaysSale(Long storeId, Long domainId) {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainIdAndCreatedDate(storeId, domainId,Date);
		

		Long amount = nsentity.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();
		rsvo.setName("today's sale");
		rsvo.setAmount(amount);

		return rsvo;
	}

	@Override
	public ReportVo getMonthlySale(Long storeId, Long domainId) {

		LocalDate dateTo = LocalDate.now();
		LocalDate dateFrom = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainIdAndCreatedDateBetween(storeId, domainId,dateFrom,dateTo);
		

		Long amount = nsentity.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();
		rsvo.setName("currntmonth sale");
		rsvo.setAmount(amount);

		return rsvo;
	}

	

	@Override
	public ReportVo getcurrentMonthSalevsLastMonth(Long storeId, Long domainId) {

		ReportVo currentMonth = getMonthlySale(storeId, domainId);
		LocalDate dateFrom = LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate dateTo = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainIdAndCreatedDateBetween(storeId, domainId,dateFrom,dateTo);
		/*List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());*/
		List<NewSaleEntity> newsaleentity = nsentity.stream().filter(nEntity->nEntity.getNetValue()!=null).collect(Collectors.toList());

		Long amount = newsaleentity.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();

		if (amount != 0L) {

			float value = ((currentMonth.getAmount() - amount) * 100) / amount;
			rsvo.setName("currentMonthSaleVsLastMonth");
			rsvo.setPercentValue(value);
			return rsvo;

		} else {

			rsvo.setName("There is No Sale for last Month");
			rsvo.setPercentValue(currentMonth.getAmount());
			return rsvo;
		}
	}

	@PersistenceContext
	EntityManager em;

	@Override
	public List<ReportVo> getTopFiveSalesByRepresentative(Long storeId, Long domainId,String name) {

		List<ReportVo> vo = new ArrayList<>();

		List<NewSaleEntity> lnesen = new ArrayList<>();
		
		
		if (domainId == DomainData.TE.getId()) {
			List<ReportVo> lRvos = new ArrayList<ReportVo>();
	
			
			LocalDate fromDate;
			LocalDate toDate;
			 switch(name){    
			   
			    
			    case "LastOneMonth": 
			    	 toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(1);
			    break;    
			    case "Last6months": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(6);
			    break; 
			    case "LastYear": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(12);
			    	
			    break; 
			    default:
			    	toDate=LocalDate.now();
				   	fromDate=LocalDate.now();
			    }    
			
			List<Object[]> ds = dsRepo.getByStoreIdAndCreatedDateBetween(storeId,fromDate,toDate);
			if (ds.size() > 0) {
			    for (int j = 0; j < ds.size(); j++) {
			        Object[] object = ds.get(j);
			        BigInteger userId = (BigInteger) object[0];
			        BigDecimal amount = (BigDecimal) object[1];
			        
			        long amountLong = amount.longValue();
			        long userIdLong =userId.longValue();
			        ReportVo r = new ReportVo();
			        r.setAmount(amountLong);
			        r.setUserId(userIdLong);
                    lRvos.add(r);
			        
			    }
			}
			
			
			List<Long> uids = lRvos.stream().map(u -> u.getUserId()).collect(Collectors.toList());
			List<UserDetailsVo> uvos = new ArrayList<>();
			try {
				uvos = getUsersForGivenIds(uids);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (uvos != null) {

				uvos.stream().forEach(s -> {

					lRvos.stream().forEach(r -> {

						if (s.getId().equals(r.getUserId())) {

							r.setName(s.getUserName());

							lRvos.add(r);

						}

					});

				});

			}
			

			return lRvos;

		} else if (domainId != DomainData.TE.getId()) {
			List<ReportVo> lRvos = new ArrayList<ReportVo>();
			LocalDate fromDate;
			LocalDate toDate;
			 switch(name){    
			   
			    
			    case "LastOneMonth": 
			    	 toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(1);
			    break;    
			    case "Last6months": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(6);
			    break; 
			    case "LastYear": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(12);
			    	
			    break; 
			    default:
			    	toDate=LocalDate.now();
				   	fromDate=LocalDate.now();
			    }    

			List<Object[]> reent = lineItemReRepo.getByStoreIdAndCreationDateBetween(storeId,fromDate,toDate);
			
			if (reent.size() > 0) {
			    for (int j = 0; j < reent.size(); j++) {
			        Object[] object = reent.get(j);
			        BigInteger userId = (BigInteger) object[0];
			        BigDecimal amount = (BigDecimal) object[1];
			        
			        long amountLong = amount.longValue();
			        long userIdLong =userId.longValue();
			        ReportVo r = new ReportVo();
			        r.setAmount(amountLong);
			        r.setUserId(userIdLong);
                    lRvos.add(r);
			        
			    }
			}

			
			List<Long> uids = vo.stream().map(u -> u.getUserId()).collect(Collectors.toList());
			List<UserDetailsVo> uvos = new ArrayList<>();
			try {
				uvos = getUsersForGivenIds(uids);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (uvos != null) {

				uvos.stream().forEach(s -> {

					lRvos.stream().forEach(r -> {

						if (s.getId().equals(r.getUserId())) {

							r.setName(s.getUserName());

							lRvos.add(r);

						}

					});

				});

			}
			
			return lRvos;

		} else

			return null;

	}

	@Override
	public List<ReportVo> getSalesByCategory(Long storeId, Long domainId,String name) {

		List<ReportVo> vo = new ArrayList<>();
		List<NewSaleEntity> nsentity = new ArrayList<NewSaleEntity>();
		LocalDate Date = LocalDate.now();
		// System.out.println("id"+lineItemId);
		if (domainId == DomainData.TE.getId()) {
			List<ReportVo> lRvos = new ArrayList<ReportVo>();

			LocalDate fromDate;
			LocalDate toDate;
			 switch(name){    
			   
			    
			    case "LastOneMonth": 
			    	 toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(1);
			    break;    
			    case "Last6months": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(6);
			    break; 
			    case "LastYear": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(12);
			    	
			    break; 
			    default:
			    	toDate=LocalDate.now();
				   	fromDate=LocalDate.now();
			    }    
			List<Object[]> lineEntity = lineItemRepo.findByStoreIdAndCreation_dateBetween(storeId,fromDate,toDate);
			if (lineEntity.size() > 0) {
			    for (int j = 0; j < lineEntity.size(); j++) {
			        Object[] object = lineEntity.get(j);
			        BigInteger section =(BigInteger)  object[0];
			        BigDecimal amount = (BigDecimal) object[1];
			        
			        long amountLong = amount.longValue();
			        long sections =section.longValue();
			        ReportVo r = new ReportVo();
			        r.setAmount(amountLong);
			        r.setCategeoryType(sections);
                    lRvos.add(r);
			        
			    }
			}
			
			
			
			
			return lRvos;
		} else if (domainId != DomainData.TE.getId()) {
			List<ReportVo> lRvos = new ArrayList<ReportVo>();

			
			LocalDate fromDate;
			LocalDate toDate;
			 switch(name){    
			   
			    
			    case "LastOneMonth": 
			    	 toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(1);
			    break;    
			    case "Last6months": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(6);
			    break; 
			    case "LastYear": 
			    	toDate=LocalDate.now();
			    	fromDate=LocalDate.now().minusMonths(12);
			    	
			    break; 
			    default:
			    	toDate=LocalDate.now();
				   	fromDate=LocalDate.now();
			    }    
			
			List<Object[]> lineReEntity = lineItemReRepo.findByStoreIdAndCreation_dateBetween(storeId,fromDate,toDate);
			
			
			if (lineReEntity.size() > 0) {
			    for (int j = 0; j < lineReEntity.size(); j++) {
			        Object[] object = lineReEntity.get(j);
			        BigInteger section =(BigInteger)  object[0];
			        BigDecimal amount = (BigDecimal) object[1];
			        
			        long amountLong = amount.longValue();
			        long sections =section.longValue();
			        ReportVo r = new ReportVo();
			        r.setAmount(amountLong);
			        r.setCategeoryType(sections);
                    lRvos.add(r);
			        
			    }
			}
			
			
			    return lRvos;
		}else
		return null;

	}

}
