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

import com.otsi.retail.newSale.common.DSStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "delivery_slip")
public class DeliverySlipEntity  {

	
	@Id
	@GeneratedValue
	private Long dsId;

	private String dsNumber;
	
	private int qty;

	private String type;

	private Long mrp;

	private Long promoDisc;

	private Long netAmount;

	private DSStatus status;
	
	private Long salesMan;

	private LocalDate createdDate;

	private LocalDateTime lastModified;

	@OneToMany( mappedBy = "deliverySlip", cascade = CascadeType.ALL)
	private List<BarcodeEntity> barcodes;

	@ManyToOne
	@JoinColumn(name = "newsale_id")
	private NewSaleEntity newsale;
	

}
