package com.otsi.kalamandir.vo;



import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;




public class DelivarySlipVo {
	
	private int spId;
	private  String spName;
	private String barcode;
	private String unitsOfMeasurements;
//	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
//	private LocalDateTime dsCreatedDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private LocalDateTime dsOpenDate;
	
	private boolean isOpen;
	public int getSpId() {
		return spId;
	}
	public void setSpId(int spId) {
		this.spId = spId;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getUnitsOfMeasurements() {
		return unitsOfMeasurements;
	}
	public void setUnitsOfMeasurements(String unitsOfMeasurements) {
		this.unitsOfMeasurements = unitsOfMeasurements;
	}

	
	
	
	
	public LocalDateTime getDsOpenDate() {
		return dsOpenDate;
	}
	public void setDsOpenDate(LocalDateTime dsOpenDate) {
		this.dsOpenDate = dsOpenDate;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	
	
		
	

}
