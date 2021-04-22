package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoomBooking;
import org.springframework.samples.petclinic.repository.HotelRoomBookingRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedHotelRoomForDateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelRoomBookingService {

	private HotelRoomBookingRepository hotelRoomBookingRepository;
	
	private OwnerService ownerService;

	@Autowired
	public HotelRoomBookingService(HotelRoomBookingRepository hotelRoomBookingRepository,
			OwnerService ownerService) {
		this.hotelRoomBookingRepository = hotelRoomBookingRepository;
		this.ownerService = ownerService;
	}


	@Transactional
	public void saveHotelRoom(HotelRoomBooking hotelRoomBooking) throws DataAccessException, DuplicatedHotelRoomForDateException {
		boolean checkRoomIsBooked = false;
		boolean checkPetHasBookedRoomForDate = false;
		Collection<HotelRoomBooking> roomsWithTheSameNameAndNumber = this.findAllByHotelRoomByNameAndNumber(hotelRoomBooking.getHotelRoom().getName(), hotelRoomBooking.getHotelRoom().getNumber());
		Collection<HotelRoomBooking> bookedRoomsByPetId = this.findBookedRoomsByPetId(hotelRoomBooking.getPet().getId());

		for(HotelRoomBooking room: roomsWithTheSameNameAndNumber) {
			if(hotelRoomBooking.getStartDate().isBefore(room.getFinishDate()) || hotelRoomBooking.getStartDate().isEqual(room.getFinishDate())) {
				checkRoomIsBooked = true;
			}
		}

		for(HotelRoomBooking room: bookedRoomsByPetId) {
			if(hotelRoomBooking.getStartDate().isBefore(room.getFinishDate()) || hotelRoomBooking.getStartDate().isEqual(room.getFinishDate())) {
				checkPetHasBookedRoomForDate = true;
			}
		}

		if(checkRoomIsBooked) {
        	throw new DuplicatedHotelRoomForDateException();
		} else if(checkPetHasBookedRoomForDate) {
        	throw new DuplicatedHotelRoomForDateException();
        } else {
        	hotelRoomBookingRepository.save(hotelRoomBooking);
		}
	}

	@Transactional
	public Collection<HotelRoomBooking> findBookedRoomsByPetId(int petId) {
		return hotelRoomBookingRepository.findByPetId(petId);
	}
	
	@Transactional
	public Optional<HotelRoomBooking> findById(int id) {
		return hotelRoomBookingRepository.findById(id);
	}
	
	@Transactional
	public Collection<HotelRoomBooking> findAll() {
		return hotelRoomBookingRepository.findAll();
	}
	
	@Transactional
	public Collection<HotelRoomBooking> findByPetId(Integer petId) {
		return hotelRoomBookingRepository.findByPetId(petId);
	}
	
	@Transactional
	public Collection<HotelRoomBooking> findAllByHotelRoomName(String name) {
		return hotelRoomBookingRepository.findAllByHotelRoomName(name);
	}
	
	@Transactional
	public Collection<HotelRoomBooking> findAllByHotelRoomByNameAndNumber(String name, Integer number) {
		return hotelRoomBookingRepository.findAllByHotelRoomByNameAndNumber(name, number);
	}
}
