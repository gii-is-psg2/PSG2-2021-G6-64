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
import org.springframework.samples.petclinic.model.HotelRoomBooking;
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
    @Autowired
	protected HotelRoomBookingService hotelRoomBookingService;
    
	@Test
	void shouldFindHotelRoomWithCorrectId() {
		HotelRoomBooking hotelRoomBooking1 = this.hotelRoomBookingService.findById(1).get();
		assertThat(hotelRoomBooking1.getHotelRoom().getName()).hasToString("Habitacion individual");
		assertThat(hotelRoomBooking1.getHotelRoom().getNumber()).isEqualTo(1);
		assertThat(hotelRoomBooking1.getStartDate().toString()).hasToString("2013-01-04");
		assertThat(hotelRoomBooking1.getFinishDate().toString()).hasToString("2013-01-04");
	}

	@Test
	void shouldFindAllHotelRoomBookingsByName() {
		HotelRoomBooking hotelRoom1 = this.hotelRoomBookingService.findById(1).get();
		HotelRoomBooking hotelRoom2 = this.hotelRoomBookingService.findById(2).get();
		Collection<HotelRoomBooking> hotelRooms = this.hotelRoomBookingService.findAllByHotelRoomName("Habitacion individual");
		assertTrue(hotelRooms.contains(hotelRoom1));
		assertTrue(hotelRooms.contains(hotelRoom2));
	}

	@Test
	void shouldFindAllBookedHotelRoomsByPetId() {
		HotelRoomBooking hotelRoom1 = this.hotelRoomBookingService.findById(1).get();
		HotelRoomBooking hotelRoom2 = this.hotelRoomBookingService.findById(2).get();
		Collection<HotelRoomBooking> hotelRooms = this.hotelRoomBookingService.findBookedRoomsByPetId(1);
		assertTrue(hotelRooms.contains(hotelRoom1));
		assertTrue(hotelRooms.contains(hotelRoom2));
	}
	
	@Test
	@Transactional
	public void shouldInsertHotelRoomIntoDatabaseAndGenerateId() {
		Pet pet1 = this.petService.findPetById(1);
		HotelRoom hotelRoom = this.hotelRoomService.findById(1).get();
		HotelRoomBooking hotelRoomBooking = new HotelRoomBooking();
		hotelRoomBooking.setHotelRoom(hotelRoom);
		hotelRoomBooking.setStartDate(LocalDate.now().plusDays(7));
		hotelRoomBooking.setFinishDate(LocalDate.now().plusDays(8));
		hotelRoomBooking.setPet(pet1);
		
		try {
			this.hotelRoomBookingService.saveHotelRoom(hotelRoomBooking);
		} catch (DuplicatedHotelRoomForDateException exception) {
            Logger.getLogger(HotelRoomServiceTests.class.getName()).log(Level.SEVERE, null, exception);
        }
		
		HotelRoomBooking hotelRoom4 = this.hotelRoomBookingService.findById(4).get();
		assertThat(hotelRoom4.getHotelRoom()).isEqualTo(hotelRoom);
		assertThat(hotelRoom4.getPet()).isEqualTo(pet1);
		assertThat(hotelRoom4.getId()).isNotNull();
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionHotelRoomIsAlreadyBooked() throws DataAccessException, DuplicatedHotelRoomForDateException {

		Pet pet1 = this.petService.findPetById(1);
		HotelRoom hotelRoom2 = this.hotelRoomService.findById(2).get();

		HotelRoomBooking hotelRoomForPet1 = new HotelRoomBooking();
		hotelRoomForPet1.setHotelRoom(hotelRoom2);
		hotelRoomForPet1.setStartDate(LocalDate.now());
		hotelRoomForPet1.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoomForPet1.setPet(pet1);
		
		Pet pet2 = this.petService.findPetById(2);
		
		HotelRoomBooking hotelRoomForPet2 = new HotelRoomBooking();
		hotelRoomForPet2.setHotelRoom(hotelRoom2);
		hotelRoomForPet2.setStartDate(LocalDate.now());
		hotelRoomForPet2.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoomForPet2.setPet(pet2);
		
		this.hotelRoomBookingService.saveHotelRoom(hotelRoomForPet1);
		Assertions.assertThrows(DuplicatedHotelRoomForDateException.class, () ->{
			this.hotelRoomBookingService.saveHotelRoom(hotelRoomForPet2);
		});		
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionHotelRoomPetHasBookedRoomForSelectedDate() throws DataAccessException, DuplicatedHotelRoomForDateException {
		Pet pet1 = this.petService.findPetById(1);
		HotelRoom hotelRoom1 = this.hotelRoomService.findById(1).get();
		HotelRoom hotelRoom2 = this.hotelRoomService.findById(2).get();

		HotelRoomBooking hotelRoomBooking1 = new HotelRoomBooking();
		hotelRoomBooking1.setHotelRoom(hotelRoom1);
		hotelRoomBooking1.setStartDate(LocalDate.now());
		hotelRoomBooking1.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoomBooking1.setPet(pet1);
				
		HotelRoomBooking hotelRoomBooking2 = new HotelRoomBooking();
		hotelRoomBooking2.setHotelRoom(hotelRoom2);
		hotelRoomBooking2.setStartDate(LocalDate.now());
		hotelRoomBooking2.setFinishDate(LocalDate.now().plusDays(1));
		hotelRoomBooking2.setPet(pet1);
		
		this.hotelRoomBookingService.saveHotelRoom(hotelRoomBooking1);

		Assertions.assertThrows(DuplicatedHotelRoomForDateException.class, () ->{
			this.hotelRoomBookingService.saveHotelRoom(hotelRoomBooking2);
		});	
	}
}
