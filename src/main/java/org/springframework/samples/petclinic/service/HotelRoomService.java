package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.HotelRoomRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class HotelRoomService {

	private PetRepository petRepository;
		
	private HotelRoomRepository hotelRoomRepository;

	@Autowired
	public HotelRoomService(PetRepository petRepository,
			HotelRoomRepository hotelRoomRepository) {
		this.petRepository = petRepository;
		this.hotelRoomRepository = hotelRoomRepository;
	}


	@Transactional
	public void saveHotelRoom(HotelRoom hotelRoom) throws DataAccessException {
		hotelRoomRepository.save(hotelRoom);
	}

	@Transactional
	public Collection<HotelRoom> findBookedRoomsByPetId(int petId) {
		return hotelRoomRepository.findByPetId(petId);
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
	public Collection<HotelRoom> findAllByHotelName(String name) {
		return hotelRoomRepository.findAllByHotelName(name);
	}
}
