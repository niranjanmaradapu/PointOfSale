package com.otsi.retail.promoexchange.Entity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.LongStream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.otsi.retail.promoexchange.common.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "promoexchange")
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PromoExchangeEntity {

	@Id
	@GeneratedValue
	private Long promoExchangeId;

	private PaymentType payType;

	private String custmerId;

	private Long grossAmount;

	private Long totalPromoDisc;

	private Long totalManualDisc;

	private float roundOff;

	private Long netPayableAmount;

	private Long taxAmount;

	private String billNumber;
    
	private String biller;
	
	private String billStatus;
	
	private Long invoiceNumber;

	private LocalDate createdDate;
	
	private Long offlineNumber;
	
	private String approvedBy;
	
	private String reason;
	
	private Long balanceAmount;
	
	private Long recievedAmount;

	@OneToMany(targetEntity = DeliverySlipEntity.class, mappedBy = "promoexchange", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<DeliverySlipEntity> dlSlip;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private CustomerDetailsEntity customerDetails;


}
