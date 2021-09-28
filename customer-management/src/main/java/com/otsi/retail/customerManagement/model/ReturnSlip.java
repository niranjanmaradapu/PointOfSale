package com.otsi.retail.customerManagement.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lakshmi
 *
 */
@Entity
@Table(name = "returnSlips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnSlip {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int rsId;
	private String rtNo;
	private String crNo;
	private String settelmentInfo;
	private Long amount;
	private String mobileNumber;
	private Boolean isReviewed;
	private int rtStatus;
	private LocalDate createdDate;
	private LocalDate modifiedDate;
	private String reviewedBy;
	private String createdBy;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "rs_id")
	private List<TaggedItems> taggedItems;

}
