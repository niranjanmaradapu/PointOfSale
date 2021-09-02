package com.otsi.kalamandhir.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reason")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reason {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long reasonId;
	private String reason;
	private LocalDate createdDate;
	private LocalDate modifiedDate;
	
}
