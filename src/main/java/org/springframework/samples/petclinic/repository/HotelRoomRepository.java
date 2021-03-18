package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.HotelRoom;

public interface HotelRoomRepository extends CrudRepository<HotelRoom, Integer> {

	@Query("SELECT ALL h from HotelRoom h where h.pet.id =:petId")
	List<HotelRoom> findByPetId(Integer petId);

	@Query("SELECT ALL h from HotelRoom h")
	List<HotelRoom> findAll();
		
	@Query("SELECT ALL h from HotelRoom h where h.name =:name")
	Collection<HotelRoom> findAllByHotelName(@Param("name") String name) throws DataAccessException;
	
}
