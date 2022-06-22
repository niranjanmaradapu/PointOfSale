package com.otsi.retail.newSale.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	
	/*@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;*/

	@Column(name = "created_by")
	    private Long createdBy;

	    @Column(name = "created_date", updatable = false)
	    @CreatedDate
	    private LocalDateTime createdDate = LocalDateTime.now();

	    @Column(name = "modified_by")
	    private Long modifiedBy;

	    @Column(name = "last_modified_date")
	    @LastModifiedDate
	    private LocalDateTime lastModifiedDate = LocalDateTime.now();

	
}
