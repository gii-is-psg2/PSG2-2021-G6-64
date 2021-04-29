package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Donation extends BaseEntity{
	
	@NotEmpty
	private String description;
	
	@NotNull
	@Digits(integer = 10, fraction = 2)
	@Positive
	private Double amount;
	
	@ManyToOne(optional = false)
	private Cause cause;
	
	@ManyToOne(optional = false)
	private Owner owner;
}
