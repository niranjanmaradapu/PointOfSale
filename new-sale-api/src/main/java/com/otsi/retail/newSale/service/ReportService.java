package com.otsi.retail.newSale.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.otsi.retail.newSale.vo.ReportVo;

@Component
public interface ReportService {

	List<ReportVo> getInvoicesGeneratedDetails();

	List<ReportVo> getSaleMonthyTrendDetails();

	List<ReportVo> getTopfiveSalesByStore();

	List<ReportVo> getsaleSummeryDetails();
	

}
