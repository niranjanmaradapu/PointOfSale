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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.otsi.retail.newSale.Entity.DeliverySlipEntity;
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
	private LineItemReRepo lineItemReRepo;

	@Autowired
	private RestTemplate template;

	@Autowired
	private Config config;

	@Override
	public List<ReportVo> getInvoicesGeneratedDetails() {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();

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
	public ReportVo getTodaysSale() {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
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
	public ReportVo getMonthlySale() {

		LocalDate Date = LocalDate.now();

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
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
	public ReportVo getcurrentMonthSalevsLastMonth() {

		ReportVo currentMonth = getMonthlySale();
		LocalDate Date = LocalDate.now().minusMonths(1);
		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
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
	public List<ReportVo> getTopFiveSalesByRepresentative(Long StoreId, Long DomainId) {

		List<ReportVo> vo = new ArrayList<>();
		LocalDate Date =LocalDate.now();
		
		if (DomainId == DomainData.TE.getId()) {
			

			List<DeliverySlipEntity> dsEntity = dsRepo.findByStoreId(StoreId);
			
			List<DeliverySlipEntity> desen = dsEntity.stream().filter(a -> a.getCreationDate().getDayOfMonth() == (Date.getDayOfMonth()))
					.collect(Collectors.toList());

			List<Long> userids = desen.stream().map(a -> a.getUserId()).distinct().collect(Collectors.toList());

			userids.stream().forEach(u -> {

				List<DeliverySlipEntity> dsen = dsRepo.findByUserId(u);

				List<Long> orderIds = dsen.stream().map(a -> a.getOrder().getOrderId()).distinct()
						.collect(Collectors.toList());
				orderIds.stream().forEach(d -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(d);

					Long amount = nen.get().getNetValue();
					ReportVo v = new ReportVo();
					v.setAmount(amount);
					v.setUserId(u);

					vo.add(v);

				});

			});
			List<ReportVo> sorted = vo.stream().sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
					.collect(Collectors.toList());
			List<ReportVo> first5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());

			return first5ElementsList;

		}else if (DomainId != DomainData.TE.getId()) {
			
			List<LineItemsReEntity> reent = lineItemReRepo.findByStoreId(StoreId);
			
			List<LineItemsReEntity> desen = reent.stream().filter(a -> a.getCreationDate().getDayOfMonth() == (Date.getDayOfMonth()))
					.collect(Collectors.toList());
			List<Long> userids = desen.stream().map(a -> a.getUserId()).distinct().collect(Collectors.toList());
			
			userids.stream().forEach(u -> {

				List<LineItemsReEntity> len = lineItemReRepo.findByUserId(u);

				List<Long> orderIds = len.stream().map(a -> a.getOrderId().getOrderId()).distinct()
						.collect(Collectors.toList());
				orderIds.stream().forEach(d -> {
					Optional<NewSaleEntity> nen = newsaleRepo.findByOrderId(d);

					Long amount = nen.get().getNetValue();
					ReportVo v = new ReportVo();
					v.setAmount(amount);
					v.setUserId(u);

					vo.add(v);

				});
				

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
	
}

		
	


