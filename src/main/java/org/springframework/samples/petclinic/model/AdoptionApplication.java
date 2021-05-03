package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name="adoptionApplication")
@Data
public class AdoptionApplication extends BaseEntity{
	
	@NotBlank
	@Column(name="solicitud")
	@Lob
	private String solicitud;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	
}
