package com.otsi.retail.newSale.vo;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDetailsVo {
	private Long userId;
	private String userName;
	private String phoneNumber;
	private String gender;
	private LocalDate createdDate;
	private LocalDate lastModifyedDate;
	private long createdBy;
	private boolean isActive;
	@JsonIgnore
	private Role role;
	@JsonIgnore
	private List<ClientDomains> clientDomians;
	private List<UserAv> userAv;
	private List<StoreVo> stores;
	private StoreVo ownerOf;
	private boolean isSuperAdmin;
	private boolean isCustomer;

}
