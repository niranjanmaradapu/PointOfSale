/**
 * 
 */
package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.List;

import com.otsi.retail.newSale.common.BillType;

import lombok.Data;

/**
 * @author ashok
 *
 */
@Data
public class SaleReportVo {
	
private LocalDate dateFrom;
	
	private LocalDate dateTo;
	
	private BillType billType;
	
	private Long TotalMrp;
	
	private Long billValue;
	
	private Long totalDiscount;
	
	private float totalTaxableAmount;
	
	private float totalTaxAmount;
	
	private float totalSgst;
	
	private float totalCgst;
	
	private float totalIgst;
	
	private String taxDescription;
	
	private ReturnSummeryVo retunSummery;
	
	private SalesSummeryVo salesSummery;
	
	private List<BarcodeVo> barcodes;
	


}
