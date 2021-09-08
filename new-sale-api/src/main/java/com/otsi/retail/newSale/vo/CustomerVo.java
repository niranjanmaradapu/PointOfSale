package com.otsi.retail.newSale.vo;

import java.time.LocalDate;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class CustomerVo {
	
	private Long customerId;

	@Pattern(regexp = "[a-zA-Z]+")
	@NotBlank(message = "Name must be filled")
	private String name;

	@Size(min = 10, max = 11, message = "mobile number must be filled and it is in between {min} and {max} letters")
	@Pattern(regexp = "[0-9]+")
	private String mobileNumber;

	private String gstNumber;

	private String address;

	private String email;
	
	// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dob;

	@NotBlank(message = "Gender must be filled")
	@Pattern(regexp = "[a-zA-Z]+")
	private String gender;

	@Size(min = 10, max = 11)
	@Pattern(regexp = "[0-9]+")
	private String altMobileNo;

}
