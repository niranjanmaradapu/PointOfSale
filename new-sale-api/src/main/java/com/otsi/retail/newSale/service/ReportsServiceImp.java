package com.otsi.retail.newSale.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	public List<StoreVo> getStoresForGivenId(List<Long> storeIds) throws URISyntaxException{
		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getStoreDetails())).build()
				.encode().toUri();
		
		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(storeIds, headers);

		ResponseEntity<?> newsaleResponse = template.exchange(uri, HttpMethod.POST, request,
				GateWayResponse.class);

		System.out.println("Received Request to getBarcodeDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<StoreVo> bvo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<StoreVo>>() {
		});
		return bvo;
		
	}
	public List<UserDetailsVo> getUsersForGivenIds(List<Long> userIds) throws URISyntaxException{
		
		HttpHeaders headers = new HttpHeaders();
		URI uri = UriComponentsBuilder.fromUri(new URI(config.getUserDetails())).build()
				.encode().toUri();
		
		HttpEntity<List<Long>> request = new HttpEntity<List<Long>>(userIds, headers);

		ResponseEntity<?> newsaleResponse = template.exchange(uri, HttpMethod.POST, request,
				GateWayResponse.class);

		System.out.println("Received Request to getBarcodeDetails:" + newsaleResponse);
		ObjectMapper mapper = new ObjectMapper();

		GateWayResponse<?> gatewayResponse = mapper.convertValue(newsaleResponse.getBody(), GateWayResponse.class);

		List<UserDetailsVo> uvo = mapper.convertValue(gatewayResponse.getResult(), new TypeReference<List<UserDetailsVo>>() {
		});
		return uvo;
		
		
	}

	@Override
	public List<ReportVo> getInvoicesGeneratedDetails(Long storeId,Long domainId) {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId,domainId);

		List<NewSaleEntity> nen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());
		List<Month> Month = nen.stream().map(a -> a.getCreationDate().getMonth()).distinct()
				.collect(Collectors.toList());
		Month.stream().forEach(m -> {

			List<NewSaleEntity> en = nen.stream().filter(a -> a.getCreationDate().getMonth().equals(m))
					.collect(Collectors.toList());

			Long amount = en.stream().mapToLong(i -> i.getNetValue()).sum();
			ReportVo rvo = new ReportVo();
			rvo.setMonth(m);
			rvo.setAmount(amount);

			lrvo.add(rvo);
			// });
		});

		return lrvo;
	}

	@Override
	public List<ReportVo> getSaleMonthyTrendDetails() {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();
		LocalDate lastYear = Date.minusYears(1);

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
		List<NewSaleEntity> nen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		return null;

	}

	@Override
	public List<ReportVo> getTopfiveSalesByStore(Long domainId) {
		List<ReportVo> rvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByDomainId(domainId);
		List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());

		List<Long> storeIds = nen.stream().map(a -> a.getStoreId()).distinct().collect(Collectors.toList());

		storeIds.stream().forEach(n -> {
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
		List<Long> sIds =first5ElementsList.stream().map(s-> s.getStoreId()).collect(Collectors.toList());
		List<StoreVo> svos=new ArrayList<>();
		try {
			 svos = getStoresForGivenId(sIds);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (first5ElementsList.size() == svos.size()) {
			
			svos.stream().forEach( s-> {
				
				first5ElementsList.stream().forEach(r->{
					
					if(s.getId() == r.getStoreId()) {
					
						r.setName(s.getName());
					}
					
				});
				
			});
			
		}

		return first5ElementsList;
	}

	@Override
	public List<ReportVo> getsaleSummeryDetails(Long storeId,Long domainId) {

		List<ReportVo> rvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetAllListOfReturnSlips() + "?storeId=" + storeId + "?domainId=" + domainId,
				HttpMethod.GET, null, GateWayResponse.class);

		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		GateWayResponse<?> gatewayResponse = mapper.convertValue(returnSlipListResponse.getBody(),
				GateWayResponse.class);

		List<ListOfReturnSlipsVo> vo = mapper.convertValue(gatewayResponse.getResult(),
				new TypeReference<List<ListOfReturnSlipsVo>>() {
				});
		List<ListOfReturnSlipsVo> yvo = vo.stream().filter(a -> a.getCreatedInfo().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<ListOfReturnSlipsVo> mvo = yvo.stream()
				.filter(a -> a.getCreatedInfo().getMonthValue() == (Date.getMonthValue())).collect(Collectors.toList());

		Long ramount = mvo.stream().mapToLong(a -> a.getAmount()).sum();
		ReportVo revo = new ReportVo();
		revo.setName("ReturnInvoice");
		revo.setAmount(ramount);
		rvo.add(revo);

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId,domainId);
		List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());
		Long amount = nen.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();
		rsvo.setName("SaleInvoice");
		rsvo.setAmount(amount);
		rvo.add(rsvo);

		return rvo;
	}

	@Override
	public ReportVo getTodaysSale(Long storeId, Long domainId) {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId, domainId);
		List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());

		List<NewSaleEntity> ne = nen.stream().filter(a -> a.getCreationDate().getDayOfMonth() == (Date.getDayOfMonth()))
				.collect(Collectors.toList());

		Long amount = ne.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();
		rsvo.setName("today's sale");
		rsvo.setAmount(amount);

		return rsvo;
	}

	@Override
	public ReportVo getMonthlySale(Long storeId, Long domainId) {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId, domainId);
		List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());

		Long amount = nen.stream().mapToLong(a -> a.getNetValue()).sum();
		ReportVo rsvo = new ReportVo();
		rsvo.setName("currntmonth sale");
		rsvo.setAmount(amount);

		return rsvo;
	}

	@Override
	public ReportVo getcurrentMonthSalevsLastMonth(Long storeId, Long domainId) {

		ReportVo currentMonth = getMonthlySale(storeId, domainId);
		LocalDate Date = LocalDate.now().minusMonths(1);
		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId, domainId);
		List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear() == (Date.getYear()))
				.collect(Collectors.toList());

		List<NewSaleEntity> nen = nsen.stream()
				.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
				.collect(Collectors.toList());

		Long amount = nen.stream().mapToLong(a -> a.getNetValue()).sum();
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

	@Override
	public List<ReportVo> getTopFiveSalesByRepresentative(Long storeId, Long domainId) {

		List<ReportVo> vo = new ArrayList<>();

		List<NewSaleEntity> lnesen = new ArrayList<>();
		LocalDate Date = LocalDate.now();

		if (domainId == DomainData.TE.getId()) {
			List<ReportVo> lRvos = new ArrayList<ReportVo>();


			List<DeliverySlipEntity> dsEntity = dsRepo.findByStoreId(storeId);

			List<DeliverySlipEntity> desen = dsEntity.stream()
					.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
					.collect(Collectors.toList());

			List<Long> userids = desen.stream().map(a -> a.getUserId()).filter(n -> n != null).distinct()
					.collect(Collectors.toList());

			userids.stream().forEach(u -> {

				List<DeliverySlipEntity> dsen = dsRepo.findByUserId(u);
				List<NewSaleEntity> nsen = dsen.stream().map(a -> a.getOrder()).filter(n -> n != null)
						.collect(Collectors.toList());

				List<Long> orderIds = nsen.stream().map(a -> a.getOrderId()).filter(n -> n != null).distinct()
						.collect(Collectors.toList());
				orderIds.stream().forEach(d -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(d);

					lnesen.add(nen.get());

				});
				if (!lnesen.isEmpty()) {
					Long amount = lnesen.stream().mapToLong(x -> x.getNetValue()).sum();
					ReportVo v = new ReportVo();
					v.setAmount(amount);
					v.setUserId(u);

					vo.add(v);
				}

			});
			List<Long> uids = vo.stream().map(u -> u.getUserId()).collect(Collectors.toList());
			List<UserDetailsVo> uvos = new ArrayList<>();
			try {
				uvos = getUsersForGivenIds(uids);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(uvos!=null) {
			
				
				uvos.stream().forEach( s-> {
					
					vo.stream().forEach(r->{
						
						if(s.getUserId().equals(r.getUserId())) {
							
					       r.setName(s.getUserName());
							
							lRvos.add(r);
							
		
							
						}
						
					});
					
				});
			
			List<ReportVo> sorted = lRvos.stream().sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
					.collect(Collectors.toList());
			List<ReportVo> first5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());
			
				return first5ElementsList;

			}
			
			

		} else if (domainId != DomainData.TE.getId()) {
			List<ReportVo> lRvos = new ArrayList<ReportVo>();

			List<LineItemsReEntity> reent = lineItemReRepo.findByStoreId(storeId);

			List<LineItemsReEntity> desen = reent.stream()
					.filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
					.collect(Collectors.toList());
			List<Long> userids = desen.stream().map(a -> a.getUserId()).distinct().collect(Collectors.toList());

			userids.stream().forEach(u -> {

				List<LineItemsReEntity> len = lineItemReRepo.findByUserId(u);
				List<NewSaleEntity> news = len.stream().map(a -> a.getOrderId()).filter(n -> n != null).distinct()
						.collect(Collectors.toList());

				List<Long> orderIds = news.stream().map(a -> a.getOrderId()).distinct().collect(Collectors.toList());
				orderIds.stream().forEach(d -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(d);
					lnesen.add(nen.get());

				});
				Long amount = lnesen.stream().mapToLong(x -> x.getNetValue()).sum();
				ReportVo v = new ReportVo();
				v.setAmount(amount);
				v.setUserId(u);

				vo.add(v);

			});
			List<Long> uids = vo.stream().map(u -> u.getUserId()).collect(Collectors.toList());
			List<UserDetailsVo> uvos = new ArrayList<>();
			try {
				uvos = getUsersForGivenIds(uids);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(uvos!=null) {
			
				
				uvos.stream().forEach( s-> {
					
					vo.stream().forEach(r->{
						
						if(s.getUserId().equals(r.getUserId())) {
							
					       r.setName(s.getUserName());
							
							lRvos.add(r);
							
		
							
						}
						
					});
					
				});
			
			List<ReportVo> sorted = lRvos.stream().sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
					.collect(Collectors.toList());
			List<ReportVo> first5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());
			
				return first5ElementsList;

			}

		}
		
		return vo;
		

	}

	@Override
	public List<ReportVo> getSalesByCategory(Long storeId, Long domainId) {

		List<ReportVo> vo = new ArrayList<>();
		List<NewSaleEntity> nsentity = new ArrayList<NewSaleEntity>();
		LocalDate Date = LocalDate.now();
		// System.out.println("id"+lineItemId);
		if (domainId == DomainData.TE.getId()) {
			List<LineItemsEntity> lineEntity = lineItemRepo.findByStoreId(storeId);
			
			List<Long> sections = lineEntity.stream().map(a -> a.getSection()).filter(n -> n != null).distinct()
					.collect(Collectors.toList());

			sections.stream().forEach(b -> {

				Long amount = 0l;

				nsentity.clear();
				List<LineItemsEntity> data = lineItemRepo.findBySectionAndStoreId(b, storeId);
				List<LineItemsEntity> lien = data.stream()
						.filter(a -> a.getCreationDate().getMonth() == Date.getMonth()).collect(Collectors.toList());
				List<NewSaleEntity> nsen = lien.stream().map(num -> num.getDsEntity().getOrder()).filter(n -> n != null)
						.collect(Collectors.toList());

				List<Long> result = nsen.stream().map(num -> num.getOrderId()).filter(n -> n != null)
						.collect(Collectors.toList());

				List<Long> orderIds = result.stream().map(q -> q).distinct().collect(Collectors.toList());
				orderIds.stream().forEach(a -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(a);
					nsentity.add(nen.get());
				});

				amount = nsentity.stream().mapToLong(x -> x.getNetValue()).sum();
				ReportVo rvo = new ReportVo();
				rvo.setAmount(amount);
				rvo.setCategeoryType(b);
				vo.add(rvo);
			});
			return vo;

		} else if (domainId != DomainData.TE.getId()) {
			List<LineItemsReEntity> lineReEntity = lineItemReRepo.findByStoreId(storeId);
			
			List<Long> sections = lineReEntity.stream().map(a -> a.getSection()).filter(n -> n != null).distinct().collect(Collectors.toList());

			sections.stream().forEach(b -> {

				Long amount = 0l;
				nsentity.clear();

				List<LineItemsReEntity> data = lineItemReRepo.findBySectionAndStoreId(b,storeId);
				
				List<LineItemsReEntity> lien = data.stream()
						.filter(a -> a.getCreationDate().getMonth() == Date.getMonth()).collect(Collectors.toList());
				List<NewSaleEntity> newen = lien.stream().map(num -> num.getOrderId()).filter(n -> n != null)
						.collect(Collectors.toList());

				List<Long> result = newen.stream().map(num -> num.getOrderId()).filter(n -> n != null)
						.collect(Collectors.toList());

				List<Long> orderIds = result.stream().map(q -> q).distinct().collect(Collectors.toList());
				orderIds.stream().forEach(a -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(a);
					nsentity.add(nen.get());
				});

				amount = nsentity.stream().mapToLong(x -> x.getNetValue()).sum();
				ReportVo rvo = new ReportVo();
				rvo.setAmount(amount);
				rvo.setCategeoryType(b);
				vo.add(rvo);
			});
			return vo;

		}
		return vo;

	}

}
