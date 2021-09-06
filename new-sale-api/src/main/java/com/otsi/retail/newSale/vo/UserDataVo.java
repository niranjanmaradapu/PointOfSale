package com.otsi.retail.newSale.vo;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataVo {

	private Long userId;

	private String userName;

	private Long phoneNumber;

	private char gender;

	private LocalDate dob;

	private String gstNumber;

	private String panNumber;

	private String salesManId;

	private String bankDetails;

	private LocalDate creationdate;

	private LocalDate lastmodified;

}
