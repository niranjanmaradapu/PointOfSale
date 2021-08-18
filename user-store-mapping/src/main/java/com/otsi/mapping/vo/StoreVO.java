package com.otsi.mapping.vo;

import java.util.List;

import com.otsi.mapping.model.UserModel;

public class StoreVO {

	private long id;
	private String storeName;
	private List<UserModel> users;

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public List<UserModel> getUsers() {
		return users;
	}

	public void setUsers(List<UserModel> users) {
		this.users = users;
	}

}
