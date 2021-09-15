package com.otsi.retail.customerManagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class TaggedItems {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long taggedItemId;
	private String barCode;
	}
