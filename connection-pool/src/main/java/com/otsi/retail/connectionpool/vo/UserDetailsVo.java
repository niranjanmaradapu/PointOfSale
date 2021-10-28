package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;
import java.util.List;



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
	
	  private List<UserAv> userAv; 
	  private List<StoreVo> stores;
	 private StoreVo ownerOf;
	//private List<StoreVo> stores;
	//private StoreVo ownerOf;

}
