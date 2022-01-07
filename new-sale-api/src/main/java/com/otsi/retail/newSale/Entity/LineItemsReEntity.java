package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "lineitems_re")
@AllArgsConstructor
@NoArgsConstructor
public class LineItemsReEntity {

	@Id
	@GeneratedValue
	private Long lineItemReId;
	
	private Long storeId;

	private String barCode;

	private String section;

	private Long userId;

	private Long itemPrice;

	private int quantity;

	private Long taxValue;

	private Long cgst;

	private Long sgst;

	private Long grossValue;

	private Long discount;

	private Long netValue;

	private LocalDate creationDate;

	private LocalDate lastModified;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private NewSaleEntity orderId;

}
