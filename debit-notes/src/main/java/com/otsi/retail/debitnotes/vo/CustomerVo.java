package com.otsi.retail.debitnotes.vo;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;

@Data
public class CustomerVo {

	private Long customerId;

	private String name;
	
	private String email;

	private String mobileNumber;

	private String gstNumber;

	private String address;

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private Date dob;

	private String gender;

	private String altMobileNo;

}
