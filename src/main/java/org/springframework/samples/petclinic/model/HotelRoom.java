package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "hotel_rooms")
public class HotelRoom extends BaseEntity {

	@NotEmpty(message = "No puede estar vacio")
	@Column(name = "name")
	private String name;
	
	@NotNull(message = "No puede estar vacio")
	@FutureOrPresent(message = "Debe estar en presente o en futuro")
	@Column(name = "start_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate;
	
	@NotNull(message = "No puede estar vacio")
	@FutureOrPresent(message = "Debe estar en presente o en futuro")
	@Column(name = "finish_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate finishDate;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

//	public HotelRoom() {
//		this.startDate = LocalDate.now();
//		this.finishDate = LocalDate.now();
//	}
//	
//	public String getName() {
//		return this.name;
//	}
//
//	public void setDescription(String name) {
//		this.name = name;
//	}
//	
//	public LocalDate getStartDate() {
//		return this.startDate;
//	}
//
//	public void setDate(LocalDate date) {
//		this.startDate = date;
//	}
//
//	public LocalDate getFinishDate() {
//		return this.finishDate;
//	}
//
//	public void setFinishDate(LocalDate date) {
//		this.finishDate = date;
//	}
//	
//
//	public Pet getPet() {
//		return this.pet;
//	}
//
//	public void setPet(Pet pet) {
//		this.pet = pet;
//	}
//
//	@Override
//	public String toString() {
//		return "HotelRoom [name=" + name + ", startDate=" + startDate + ", finishDate=" + finishDate + "]";
//	}
	
}
