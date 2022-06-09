package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.otsi.retail.newSale.common.ReturnSlipStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "returnSlips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnSlip extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long rsId;
	private String rtNo;
	private Long storeId;
	private String settelmentInfo;
	private String invoiceNumber;
	private Long customerId;
	private Long amount;
	private String mobileNumber;
	private String reason;
	private Boolean isReviewed;
	private ReturnSlipStatus rtStatus;
	private String reviewedBy;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "rs_id")
	private List<TaggedItems> taggedItems;

}
