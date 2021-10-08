package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.otsi.retail.newSale.common.OrderStatus;
import com.otsi.retail.newSale.common.SaleNature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "order_table")
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class NewSaleEntity {

	@Id
	@GeneratedValue
	// @Column(name = "orderId")
	private Long orderId;

	private SaleNature natureOfSale;

	private Long userId;// Customer Id from user data table

	private Long storeId;

	private Long domainId;// Which type of store (Textail,Retail etc., )

	private Long orderTransactionId;// Payment related Id (orderTransaction table)

	// private PaymentType payType;

	private Long grossValue;

	private Long promoDisc;

	private Long manualDisc;

	private String discType;

	private String discApprovedBy;

	// private float roundOff;

	private Long netValue;

	private Long taxValue;

	// private String billNumber;

	private String createdBy;// Application User(Cashier)

	private OrderStatus status;

	private String orderNumber;

	private Long offlineNumber;

	private LocalDate creationDate;

	private LocalDate lastModified;

	// private String approvedBy;

	// private Long recievedAmount;

	// private String reason;//Needed to move to AV table

	@OneToMany(targetEntity = DeliverySlipEntity.class, mappedBy = "order", cascade = CascadeType.ALL)
	private List<DeliverySlipEntity> dlSlip;
	
	@OneToMany( mappedBy  = "order", cascade = CascadeType.ALL)
	private List<BarcodeEntity> lineItems;
	

	@ManyToOne
	@JoinColumn(name = "customerId")
	private CustomerDetailsEntity customerDetails;

//	 @OneToMany(targetEntity = PaymentAmountType.class, mappedBy = "newsaleId",
//	 cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	 private List<PaymentAmountType> paymentType;

}
