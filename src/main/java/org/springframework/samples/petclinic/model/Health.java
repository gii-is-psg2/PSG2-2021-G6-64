package org.springframework.samples.petclinic.model;

import lombok.Data;

@Data
public class Health extends BaseEntity{

	private String status;
	
}
