package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	
	private OwnerService ownerService;

	@Autowired
	public HotelRoomService(HotelRoomRepository hotelRoomRepository,
			OwnerService ownerService) {
		this.hotelRoomRepository = hotelRoomRepository;
		this.ownerService = ownerService;
	}


	@Transactional
	public void saveHotelRoom(HotelRoom hotelRoom) throws DataAccessException, DuplicatedHotelRoomForDateException {
		boolean checkRoomIsBooked = false;
		boolean checkPetHasBookedRoomForDate = false;
		Collection<HotelRoom> roomsWithTheSameName = this.findAllByHotelRoomName(hotelRoom.getName());
		Collection<HotelRoom> bookedRoomsByPetId = this.findBookedRoomsByPetId(hotelRoom.getPet().getId());

		for(HotelRoom room: roomsWithTheSameName) {
			if(hotelRoom.getStartDate().isBefore(room.getFinishDate()) || hotelRoom.getStartDate().isEqual(room.getFinishDate())) {
				checkRoomIsBooked = true;
			}
		}
		
		for(HotelRoom room: bookedRoomsByPetId) {
			if(hotelRoom.getStartDate().isBefore(room.getFinishDate()) || hotelRoom.getStartDate().isEqual(room.getFinishDate())) {
				checkPetHasBookedRoomForDate = true;
			}
		}
		
		if(checkRoomIsBooked) {
        	throw new DuplicatedHotelRoomForDateException();
		} else if(checkPetHasBookedRoomForDate) {
        	throw new DuplicatedHotelRoomForDateException();
        } else {
			hotelRoomRepository.save(hotelRoom);
		}
	}

	@Transactional
	public Collection<HotelRoom> findBookedRoomsByPetId(int petId) {
		return hotelRoomRepository.findByPetId(petId);
	}
	
	@Transactional
	public Optional<HotelRoom> findById(int id) {
		return hotelRoomRepository.findById(id);
	}
	
	@Transactional
	public Collection<HotelRoom> findAll() {
		return hotelRoomRepository.findAll();
	}
	
	@Transactional
	public Collection<HotelRoom> findByPetId(Integer petId) {
		return hotelRoomRepository.findByPetId(petId);
	}
	
	@Transactional
	public Collection<HotelRoom> findAllByHotelRoomName(String name) {
		return hotelRoomRepository.findAllByHotelName(name);
	}
}
