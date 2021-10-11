//package com.otsi.retail.connectionpool.entity;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.ManyToMany;
//import javax.persistence.Table;
//
//import lombok.Data;
//
//@Entity
//@Data
//@Table(name = "stores")
//public class StoresEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long storeId;
//
//	private String storeName;
//
//	private String storeDescription;
//
//	private LocalDate createdDate;
//
//	private LocalDate lastModifiedDate;
//
//	/**
//	 * @Many_To_Many mapping from Stores to Promotions
//	 */
//
//	@ManyToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL)
//	private List<PromotionsEntity> promoEntity;
//
//}
