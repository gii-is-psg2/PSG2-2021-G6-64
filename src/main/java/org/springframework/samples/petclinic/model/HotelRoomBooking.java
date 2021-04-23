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
@Table(name = "hotel_room_bookings")
public class HotelRoomBooking extends BaseEntity {
	
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
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "hotel_room_id")
	private HotelRoom hotelRoom;	
}
