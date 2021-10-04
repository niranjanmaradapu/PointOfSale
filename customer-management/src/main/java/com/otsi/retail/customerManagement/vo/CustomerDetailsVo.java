package com.otsi.retail.customerManagement.vo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerDetailsVo {
	private Long customerId;

	private String name;
	private String mobileNumber;
	private String gstNumber;
	private String address;
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dob;
	private String gender;
	private String altMobileNo;
	private String email;

	
}