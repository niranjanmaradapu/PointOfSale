package com.otsi.mapping.vo;

import java.util.List;

import com.otsi.mapping.model.StoreModel;


public class UserVo {

	private Long Id;
	private String Name;
	private String Email;
	private String Mobile;
	private String AllowedIps;
	private String Status;
	private String Actions;
	private List<StoreModel> Stores;

	

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getAllowedIps() {
		return AllowedIps;
	}

	public void setAllowedIps(String allowedIps) {
		AllowedIps = allowedIps;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getActions() {
		return Actions;
	}

	public void setActions(String actions) {
		Actions = actions;
	}
	public List<StoreModel> getStores() {
		return Stores;
	}

	public void setStores(List<StoreModel> list) {
		Stores = list;
	}

}
