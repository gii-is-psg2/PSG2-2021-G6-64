package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedHotelRoomForDateException;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class HotelRoomServiceTests {    
	
	@Autowired
	protected PetService petService;
    @Autowired
	protected OwnerService ownerService;	
    @Autowired
	protected HotelRoomService hotelRoomService;
    
	@Test
	void shouldFindHotelRoomWithCorrectId() {
		HotelRoom hotelRoom1 = this.hotelRoomService.findById(1).get();
		assertThat(hotelRoom1.getName()).startsWith("Habitación 23");
		assertThat(hotelRoom1.getStartDate().toString()).isEqualTo("2013-01-04");
		assertThat(hotelRoom1.getFinishDate().toString()).isEqualTo("2013-01-04");
	}

	@Test
	void shouldFindAllHotelRoomsByName() {
		HotelRoom hotelRoom1 = this.hotelRoomService.findById(1).get();
		HotelRoom hotelRoom3 = this.hotelRoomService.findById(3).get();
		Collection<HotelRoom> hotelRooms = this.hotelRoomService.findAllByHotelRoomName("Habitación 23");
		assertTrue(hotelRooms.contains(hotelRoom1));
		assertTrue(hotelRooms.contains(hotelRoom3));
	}

	@Test
	void shouldFindAllBookedHotelRoomsByPetId() {
		HotelRoom hotelRoom1 = this.hotelRoomService.findById(1).get();
		HotelRoom hotelRoom2 = this.hotelRoomService.findById(2).get();
		Collection<HotelRoom> hotelRooms = this.hotelRoomService.findBookedRoomsByPetId(1);
		assertTrue(hotelRooms.contains(hotelRoom1));
		assertTrue(hotelRooms.contains(hotelRoom2));
	}
	
	@Test
	void shouldFindAllHotelRooms() {
		//TODO
	}
	
	@Test
	@Transactional
	public void shouldInsertHotelRoomIntoDatabaseAndGenerateId() {
		Pet pet1 = this.petService.findPetById(1);
		
		HotelRoom hotelRoom = new HotelRoom();
		hotelRoom.setName("Habitación 42");
		hotelRoom.setStartDate(LocalDate.now().plusDays(7));
		hotelRoom.setFinishDate(LocalDate.now().plusDays(8));
		hotelRoom.setPet(pet1);
		
		try {
			this.hotelRoomService.saveHotelRoom(hotelRoom);
		} catch (DuplicatedHotelRoomForDateException exception) {
            Logger.getLogger(HotelRoomServiceTests.class.getName()).log(Level.SEVERE, null, exception);
        }
		
		HotelRoom hotelRoom4 = this.hotelRoomService.findById(4).get();
		assertThat(hotelRoom4).isEqualTo(hotelRoom);
		assertThat(hotelRoom4.getPet()).isEqualTo(pet1);
		assertThat(hotelRoom4.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionHotelRoomIsAlreadyBooked() throws DataAccessException, DuplicatedHotelRoomForDateException {

		Pet pet1 = this.petService.findPetById(1);
		
		HotelRoom hotelRoomForPet1 = new HotelRoom();
		hotelRoomForPet1.setName("Habitación 42");
		hotelRoomForPet1.setStartDate(LocalDate.now());
		hotelRoomForPet1.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoomForPet1.setPet(pet1);
		
		Pet pet2 = this.petService.findPetById(2);
		
		HotelRoom hotelRoomForPet2 = new HotelRoom();
		hotelRoomForPet2.setName("Habitación 42");
		hotelRoomForPet2.setStartDate(LocalDate.now());
		hotelRoomForPet2.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoomForPet2.setPet(pet2);
		
		this.hotelRoomService.saveHotelRoom(hotelRoomForPet1);

		Assertions.assertThrows(DuplicatedHotelRoomForDateException.class, () ->{
			this.hotelRoomService.saveHotelRoom(hotelRoomForPet2);
		});		
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionHotelRoomPetHasBookedRoomForSelectedDate() throws DataAccessException, DuplicatedHotelRoomForDateException {
		Pet pet1 = this.petService.findPetById(1);
		
		HotelRoom hotelRoom1 = new HotelRoom();
		hotelRoom1.setName("Habitación 31");
		hotelRoom1.setStartDate(LocalDate.now());
		hotelRoom1.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoom1.setPet(pet1);
				
		HotelRoom hotelRoom2 = new HotelRoom();
		hotelRoom2.setName("Habitación 42");
		hotelRoom2.setStartDate(LocalDate.now());
		hotelRoom2.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoom2.setPet(pet1);
		
		this.hotelRoomService.saveHotelRoom(hotelRoom1);

		Assertions.assertThrows(DuplicatedHotelRoomForDateException.class, () ->{
			this.hotelRoomService.saveHotelRoom(hotelRoom2);
		});	
	}
}
