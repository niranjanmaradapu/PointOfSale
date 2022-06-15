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
import javax.persistence.Transient;

import com.otsi.retail.newSale.common.NoteType;
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
public class NewSaleEntity  extends BaseEntity{

	@Id
	@GeneratedValue
	private Long orderId;

	private SaleNature natureOfSale;

	private Long userId;// Customer Id from user data table

	private Long storeId;
	
	private NoteType note;

	private Long domainId;// Which type of store (Textail,Retail etc., )

	// private Long orderTransactionId;// Payment related Id (orderTransaction
	// table)

	@OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
	private List<PaymentAmountType> paymentTransaction;

	private Long grossValue;

	private Long promoDisc;

	private Long manualDisc;

	private String discType;

	private String discApprovedBy;

	private Long netValue;

	private Long taxValue;

	//private String createdBy;// Application User(Cashier)

	private OrderStatus status;

	private String orderNumber;

	private Long offlineNumber;

	//private LocalDate creationDate;

	//private LocalDate lastModified;

	@OneToMany(targetEntity = DeliverySlipEntity.class, mappedBy = "order", cascade = CascadeType.ALL)
	private List<DeliverySlipEntity> dlSlip;

	@OneToMany(mappedBy = "order") // , cascade = CascadeType.ALL)
	private List<BarcodeEntity> lineItems;

	// added by lakshmi
	@OneToMany(targetEntity = LineItemsReEntity.class, mappedBy = "orderId", cascade = CascadeType.ALL)
	private List<LineItemsReEntity> lineItemsRe;

	@ManyToOne
	@JoinColumn(name = "customerId")
	// @Transient
	private CustomerDetailsEntity customerDetails;

}
