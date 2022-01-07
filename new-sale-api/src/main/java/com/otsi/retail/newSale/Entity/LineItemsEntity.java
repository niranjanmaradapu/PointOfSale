package com.otsi.retail.newSale.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.naming.Name;
import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "line_items")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LineItemsEntity {

	@Id
	@GeneratedValue
	private Long lineItemId;
	
	private Long storeId;

	private String section;

	private String barCode;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private String hsnCode;

	private Long actualValue;

	private Long taxValue;

	private Long cgst;

	private Long sgst;

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
