package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.otsi.retail.newSale.common.DSStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "delivery_slip")
@ToString
public class DeliverySlipEntity  {

	
	@Id
	@GeneratedValue
	private Long dsId;

	private String dsNumber;
	
	@Transient
	private int qty;
	
	@Transient
	private String type;

	@Transient
	private Long mrp;

	@Transient
	private Long promoDisc;

	@Transient
	private Long netAmount;

	private DSStatus status;
	
	private Long salesMan;

	private LocalDate creationDate;

	private LocalDate lastModified;

	@Transient
	@OneToMany( mappedBy  = "deliverySlip", cascade = CascadeType.ALL)
	private List<BarcodeEntity> barcodes;
	
	@OneToMany( mappedBy  = "dsEntity", cascade = CascadeType.ALL)
	private List<LineItemsEntity> lineItems;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private NewSaleEntity order;
}
