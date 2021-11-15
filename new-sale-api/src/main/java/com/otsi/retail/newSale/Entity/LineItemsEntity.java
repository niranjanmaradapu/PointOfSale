package com.otsi.retail.newSale.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "line_items")
@AllArgsConstructor
@NoArgsConstructor
public class LineItemsEntity {

	@Id
	@GeneratedValue
	private Long lineItemId;
	
	private String section;

	private String barCode;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private Long discount;

	private Long netValue;

	private LocalDate creationDate;

	private LocalDate lastModified;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private NewSaleEntity orderId;

	@ManyToOne
	@JoinColumn(name = "dsId")
	private DeliverySlipEntity dsEntity;
}
