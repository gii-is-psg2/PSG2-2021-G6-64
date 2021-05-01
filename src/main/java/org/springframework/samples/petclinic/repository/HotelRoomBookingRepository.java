package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.HotelRoomBooking;

public interface HotelRoomBookingRepository extends CrudRepository<HotelRoomBooking, Integer> {

	@Query("SELECT ALL h from HotelRoomBooking h")
	List<HotelRoomBooking> findAll();
	
	@Query("SELECT ALL h from HotelRoomBooking h where h.id = :id")
	Optional<HotelRoomBooking> findById(Integer id);
	
	@Query("SELECT ALL h from HotelRoomBooking h where h.pet.id = :petId")
	List<HotelRoomBooking> findByPetId(Integer petId);
		
	@Query("SELECT ALL h from HotelRoomBooking h where h.hotelRoom.name = :name")
	Collection<HotelRoomBooking> findAllByHotelRoomName(@Param("name") String name) throws DataAccessException;
	
	@Query("SELECT ALL h from HotelRoomBooking h where h.hotelRoom.name = :name and h.hotelRoom.number = :number")
	Collection<HotelRoomBooking> findAllByHotelRoomByNameAndNumber(@Param("name") String name, @Param("number") Integer number) throws DataAccessException;

}
