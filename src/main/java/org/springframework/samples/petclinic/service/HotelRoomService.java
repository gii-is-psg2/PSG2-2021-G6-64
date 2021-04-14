package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.repository.HotelRoomRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedHotelRoomForDateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelRoomService {

	private HotelRoomRepository hotelRoomRepository;
	
	@Autowired
	public HotelRoomService(HotelRoomRepository hotelRoomRepository) {
		this.hotelRoomRepository = hotelRoomRepository;
	}


	@Transactional
	public void saveHotelRoom(HotelRoom hotelRoom) throws DataAccessException, DuplicatedHotelRoomForDateException {
		Collection<HotelRoom> roomsWithTheSameName = this.findAllByHotelRoomByName(hotelRoom.getName());
		Collection<HotelRoom> roomsWithTheSameNumber = this.findAllByHotelRoomByNumber(hotelRoom.getNumber());
		
		if(roomsWithTheSameName.isEmpty() && roomsWithTheSameNumber.isEmpty()) {
			hotelRoomRepository.save(hotelRoom);
		} else {
        	throw new DuplicatedHotelRoomForDateException();
		}
	}

	@Transactional
	public Collection<HotelRoom> findAll() {
		return hotelRoomRepository.findAll();
	}
	
	@Transactional
	public Optional<HotelRoom> findById(int id) {
		return hotelRoomRepository.findById(id);
	}
	
	@Transactional
	public Collection<HotelRoom> findAllByHotelRoomByName(String name) {
		return hotelRoomRepository.findAllByHotelRoomByName(name);
	}
	
	@Transactional
	public Collection<HotelRoom> findAllByHotelRoomByNumber(Integer number) {
		return hotelRoomRepository.findAllByHotelRoomByNumber(number);
	}
}
