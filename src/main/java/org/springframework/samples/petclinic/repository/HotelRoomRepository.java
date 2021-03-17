package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.HotelRoom;

public interface HotelRoomRepository extends Repository<HotelRoom, Integer> {
	
	void save(HotelRoom hotelRoom) throws DataAccessException;

	List<HotelRoom> findByPetId(Integer petId);

}
