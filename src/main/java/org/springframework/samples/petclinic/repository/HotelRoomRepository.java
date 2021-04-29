package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.HotelRoom;

public interface HotelRoomRepository extends CrudRepository<HotelRoom, Integer> {

	@Query("SELECT ALL h from HotelRoom h order by h.number")
	List<HotelRoom> findAll();
	
	@Query("SELECT ALL h from HotelRoom h where h.id = :id")
	Optional<HotelRoom> findById(Integer id);
			
	@Query("SELECT ALL h from HotelRoom h where h.name = :name")
	Collection<HotelRoom> findAllByHotelRoomByName(@Param("name") String name) throws DataAccessException;
	
	@Query("SELECT ALL h from HotelRoom h where h.number = :number")
	Collection<HotelRoom> findAllByHotelRoomByNumber(@Param("number") Integer number) throws DataAccessException;
}
