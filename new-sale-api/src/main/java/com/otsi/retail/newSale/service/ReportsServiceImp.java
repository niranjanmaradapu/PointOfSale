package com.otsi.retail.newSale.service;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

	@Override
	public List<ReportVo> getInvoicesGeneratedDetails(Long storeId) {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreId(storeId);

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
	public List<ReportVo> getTopfiveSalesByStore() {
		List<ReportVo> rvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
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

		return first5ElementsList;
	}

	@Override
	public List<ReportVo> getsaleSummeryDetails() {

		List<ReportVo> rvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		ResponseEntity<?> returnSlipListResponse = template.exchange(config.getGetAllListOfReturnSlips(),
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

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
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
	public ReportVo getTodaysSale(Long storeId,Long domainId) {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId,domainId);
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
	public ReportVo getMonthlySale(Long storeId,Long domainId) {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId,domainId);
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
	public ReportVo getcurrentMonthSalevsLastMonth(Long storeId,Long domainId) {

		ReportVo currentMonth = getMonthlySale(storeId,domainId);
		LocalDate Date = LocalDate.now().minusMonths(1);
		List<NewSaleEntity> nsentity = newsaleRepo.findByStoreIdAndDomainId(storeId,domainId);
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
		LocalDate Date =LocalDate.now();
		
		if (domainId == DomainData.TE.getId()) {
			

			List<DeliverySlipEntity> dsEntity = dsRepo.findByStoreId(storeId);
			
			List<DeliverySlipEntity> desen = dsEntity.stream().filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
					.collect(Collectors.toList());

			List<Long> userids = desen.stream().map(a -> a.getUserId()).filter(n -> n != null).distinct().collect(Collectors.toList());

			userids.stream().forEach(u -> {

				List<DeliverySlipEntity> dsen = dsRepo.findByUserId(u);
			List<NewSaleEntity> nsen =	dsen.stream().map(a -> a.getOrder()).filter(n -> n != null)
				.collect(Collectors.toList());

				List<Long> orderIds = nsen.stream().map(a -> a.getOrderId()).filter(n -> n != null).distinct()
						.collect(Collectors.toList());
				orderIds.stream().forEach(d -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(d);
					
					lnesen.add(nen.get());
					

				});
				if(!lnesen.isEmpty()) {
				Long amount = lnesen.stream().mapToLong(x -> x.getNetValue()).sum();
				ReportVo v = new ReportVo();
				v.setAmount(amount);
				v.setUserId(u);

				vo.add(v);
				}

			});
			List<ReportVo> sorted = vo.stream().sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
					.collect(Collectors.toList());
			List<ReportVo> first5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());

			return first5ElementsList;

		}else if (domainId != DomainData.TE.getId()) {
			
			List<LineItemsReEntity> reent = lineItemReRepo.findByStoreId(storeId);
			
			List<LineItemsReEntity> desen = reent.stream().filter(a -> a.getCreationDate().getMonthValue() == (Date.getMonthValue()))
					.collect(Collectors.toList());
			List<Long> userids = desen.stream().map(a -> a.getUserId()).distinct().collect(Collectors.toList());
			
			userids.stream().forEach(u -> {

				List<LineItemsReEntity> len = lineItemReRepo.findByUserId(u);

				List<Long> orderIds = len.stream().map(a -> a.getOrderId().getOrderId()).distinct()
						.collect(Collectors.toList());
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
			List<ReportVo> sorted = vo.stream().sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
					.collect(Collectors.toList());
			List<ReportVo> top5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());

			return top5ElementsList;
			
			
			
			
			
			
		}else {
			ReportVo v = new ReportVo();
			
			v.setName("There is no sale for today");
			vo.add(v);
			
			return vo;
		}
			
		
	}

	@Override
	public List<ReportVo> getSalesByCategory(Long storeId, Long domainId) {
		
		
			List<ReportVo> vo = new ArrayList<>();
			List<NewSaleEntity> nsentity = new ArrayList<NewSaleEntity>();
			LocalDate Date = LocalDate.now();
			// System.out.println("id"+lineItemId);
			if (domainId == DomainData.TE.getId()) {
				List<LineItemsEntity> lineEntity = lineItemRepo.findByStoreId(storeId);
				List<LineItemsEntity> lien = lineEntity.stream()
						.filter(a -> a.getCreationDate().getMonth() == Date.getMonth()).collect(Collectors.toList());
				List<Long> sections = lien.stream().map(a -> a.getSection()).distinct().collect(Collectors.toList());

				sections.stream().forEach(b -> {

					Long amount = 0l;

					List<LineItemsEntity> data = lineItemRepo.findBySection(b);

					List<Long> result = data.stream().map(num -> num.getDsEntity().getOrder().getOrderId())
							.filter(n -> n != null).collect(Collectors.toList());

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

			}else if (domainId != DomainData.TE.getId()) {
				List<LineItemsReEntity> lineReEntity = lineItemReRepo.findByStoreId(storeId);
				List<LineItemsReEntity> lien = lineReEntity.stream()
						.filter(a -> a.getCreationDate().getMonth() == Date.getMonth()).collect(Collectors.toList());
				List<Long> sections = lien.stream().map(a -> a.getSection()).distinct().collect(Collectors.toList());

				sections.stream().forEach(b -> {

					Long amount = 0l;

					List<LineItemsReEntity> data = lineItemReRepo.findBySection(b);

					List<Long> result = data.stream().map(num -> num.getOrderId().getOrderId())
							.filter(n -> n != null).collect(Collectors.toList());

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

		
	


