package com.customer.CustomerDetails.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "customer_details")
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CustomerDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String name;

	private String mobileNumber;

	private String gstNumber;

	private String address;

	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate dob;

	private String gender;

	private String altMobileNo;

}
