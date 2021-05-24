package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="adoptionApplication")
@Data
public class Health extends BaseEntity{

	private String status;
	
}
