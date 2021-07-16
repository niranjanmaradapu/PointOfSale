package com.otsi.kalamandir.model;



import java.time.LocalDate;
import java.time.LocalDateTime;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
@Entity
@Table(name = "delivery_slip")
public class DeliverySlipModel {
	@Id
	
	@GeneratedValue
	private int spId;
	private  String spName;
	private String barcode;
	private String unitsOfMeasurements;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private LocalDateTime dsCreatedDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private LocalDateTime dsOpenDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate closeDate;
	
	
	
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
	public LocalDateTime getDsCreatedDate() {
		return dsCreatedDate;
	}
	public void setDsCreatedDate(LocalDateTime dsCreatedDate) {
		this.dsCreatedDate = dsCreatedDate;
	}
	
	
	
	
	
	
	public LocalDateTime getDsOpenDate() {
		return dsOpenDate;
	}
	public void setDsOpenDate(LocalDateTime dsOpenDate) {
		this.dsOpenDate = dsOpenDate;
	}
	public LocalDate getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(LocalDate closeDate) {
		this.closeDate = closeDate;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	
	
	

}
