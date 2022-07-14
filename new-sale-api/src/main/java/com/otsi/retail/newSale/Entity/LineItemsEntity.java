package com.otsi.retail.newSale.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "line_items")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LineItemsEntity extends BaseEntity {

	@Id
	@GeneratedValue
	private Long lineItemId;
	
	private Long storeId;

	private Long section;
	
	private Long subSection;
	
	private Long division;

	private String barCode;

	private Long itemPrice;

	private int quantity;

	private Long grossValue;

	private String hsnCode;

	private Long actualValue;

	private Long taxValue;

	private Float cgst;

	private Float sgst;
	
	private Float igst;
	
	private Float cess;

	private Long manualDiscount;
	
	private Long promoDiscount;

	private Long netValue;
	
	private Long salesManId;
	
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private NewSaleEntity orderId;

	@ManyToOne
	@JoinColumn(name = "dsId")
	private DeliverySlipEntity dsEntity;
}
