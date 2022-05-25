package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


import lombok.Data;


@Data
public class StoreVo {
	
	
	private long id;
	private String name;
	@JsonIgnore
	private long stateId;
	@JsonIgnore
	private String stateCode;
	@JsonIgnore
	private long districtId;
	@JsonIgnore
	private String cityId;
	@JsonIgnore
	private String area;
	@JsonIgnore
	private String address;
	@JsonIgnore
	private String phoneNumber;
	@JsonIgnore
	private LocalDate createdDate;
	@JsonIgnore
	private LocalDate lastModifiedDate;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private String modifiedBy;
	@JsonIgnore
	private Boolean isActive;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "store_owner")
	private UserDetailsVo storeOwner;
	@JsonIgnore
//	@ManyToMany(mappedBy = "store",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@ManyToOne
	@JoinColumn(name = "domianId")
	private ClientDomains clientDomianlId;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "stores",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<UserDetailsVo> storeUsers;

}
