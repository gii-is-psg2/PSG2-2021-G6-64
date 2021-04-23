package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
@Entity
@Table(name = "hotel_rooms")
public class HotelRoom extends BaseEntity {

	@NotEmpty(message = "No puede estar vacio")
	@Column(name = "name")
	private String name;
	
	@NotNull(message = "No puede estar vacio")
	@Column(name = "number")
	@Range(min = 001, max = 999)
	private Integer number;
}
