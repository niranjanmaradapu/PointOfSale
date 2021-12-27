package com.otsi.retail.newSale.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.otsi.retail.newSale.Entity.NewSaleEntity;
import com.otsi.retail.newSale.repository.NewSaleRepository;
import com.otsi.retail.newSale.vo.ReportVo;

@Service
public class ReportsServiceImp implements ReportService {

	@Autowired
	private NewSaleRepository newsaleRepo;

	@Override
	public List<ReportVo> getInvoicesGeneratedDetails() {

		List<ReportVo> lrvo = new ArrayList<ReportVo>();
		
		LocalDate Date= LocalDate.now();
	

		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
		
		List<NewSaleEntity> nen = nsentity.stream().filter(a -> a.getCreationDate().getYear()==(Date.getYear()))
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
		List<NewSaleEntity> nen = nsentity.stream().filter(a -> a.getCreationDate().getYear()==(Date.getYear()))
				.collect(Collectors.toList());
		
		

		return null;

	}

	@Override
	public List<ReportVo> getTopfiveSalesByStore() {
		List<ReportVo> rvo=new ArrayList<ReportVo>();
		
		LocalDate Date= LocalDate.now();

		
		List<NewSaleEntity> nsentity = newsaleRepo.findAll();
		List<NewSaleEntity> nsen = nsentity.stream().filter(a -> a.getCreationDate().getYear()==(Date.getYear()))
				.collect(Collectors.toList());
		
		List<NewSaleEntity> nen = nsen.stream().filter(a -> a.getCreationDate().getMonthValue()==(Date.getMonthValue()))
				.collect(Collectors.toList());
		
		List<Long> storeIds = nen.stream().map(a -> a.getStoreId()).distinct()
				.collect(Collectors.toList());
		
		storeIds.stream().forEach(n->{
			ReportVo vo= new ReportVo();
			//List<NewSaleEntity> newsaleDetails = newsaleRepo.findByStoreId(n);
			List<NewSaleEntity> en = nen.stream().filter(a -> a.getStoreId().equals(n))
					.collect(Collectors.toList());
	    Long amount = en.stream().mapToLong(a->a.getNetValue()).sum();
	    vo.setAmount(amount);
	    vo.setStoreId(n);
			
		rvo.add(vo)	;
		});
	
        List<ReportVo> sorted = rvo.stream()
                .sorted(Comparator.comparingLong(ReportVo::getAmount).reversed())
                .collect(Collectors.toList());
        List<ReportVo> first5ElementsList = sorted.stream().limit(5).collect(Collectors.toList());

		return first5ElementsList;
	}
	

}
