package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.vo.ReportVo;

@Component
public interface ReportService {

	List<ReportVo> getInvoicesGeneratedDetails(Long storeId);

	List<ReportVo> getSaleMonthyTrendDetails();

	List<ReportVo> getTopfiveSalesByStore();

	List<ReportVo> getsaleSummeryDetails();

	ReportVo getTodaysSale(Long storeId, Long domainId);

	ReportVo getMonthlySale(Long storeId, Long domainId);

	ReportVo getcurrentMonthSalevsLastMonth(Long storeId, Long domainId);

	List<ReportVo> getTopFiveSalesByRepresentative(Long storeId, Long domainId);
	

}
