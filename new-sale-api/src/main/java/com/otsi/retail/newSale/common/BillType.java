package com.otsi.retail.newSale.common;

public enum BillType {
	
	

			ALL_BILLS(1L, "all bills"), B2C_BILLS(2L, "b2c bills"), B2B_BILLS(3L, "b2b bills");

			private Long id;
			private String name;

			private BillType(Long id, String name) {
				this.id = id;
				this.name = name;
			}

			public Long getId() {
				return id;
			}

			public void setId(Long id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

		}

		 



