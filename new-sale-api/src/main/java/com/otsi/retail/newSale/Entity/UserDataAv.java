package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataAv {

	@Id
	@GeneratedValue
	private Long userAvId;

	private int type;
	
	private int integerValue;
	
	private String name;

	private String stringValue;

	private LocalDate dateValue;

	private LocalDate lastModified;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserData userData;
}
