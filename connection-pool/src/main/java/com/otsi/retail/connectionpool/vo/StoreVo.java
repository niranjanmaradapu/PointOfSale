package com.otsi.retail.connectionpool.vo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;



import lombok.Data;

@Data
@Component
public class StoreVo {

	private long id;
	private String name;
	private String location;
	private LocalDate createdDate;
	private LocalDate lastModifyedDate;
	private long createdBy;
	private UserDetailsVo storeOwner;
	private boolean isAsigned;
	private List<ClientDomianVo> domainId;
	private List<UserDetailsVo> storeUsers;

}
