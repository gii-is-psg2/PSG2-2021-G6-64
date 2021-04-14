package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoomBooking;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelRoomBookingService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedHotelRoomForDateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HotelRoomBookingController {

	@Autowired
	private OwnerService ownerService;
	@Autowired
	private PetService petService;
	@Autowired
	private HotelRoomBookingService hotelRoomBookingService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String initNewHotelRoomForm(@PathVariable("petId") final int petId, @PathVariable("ownerId") final int ownerId, final ModelMap modelMap) {
		if(!this.ownerService.findCurrentOwner().equals(this.ownerService.findOwnerById(ownerId))) {
			return "redirect:/owners/{ownerId}";
		}
			
		HotelRoomBooking hotelRoom = new HotelRoomBooking();
		Pet pet = this.petService.findPetById(petId);
		hotelRoom.setPet(pet);
		List<HotelRoomBooking> hotelRooms = new ArrayList<>(this.hotelRoomBookingService.findByPetId(pet.getId()));
		
		modelMap.addAttribute("hotelRoom", hotelRoom);
		modelMap.addAttribute("hotelRooms", hotelRooms);
		modelMap.addAttribute("pet", pet);

		return "pets/createOrUpdateHotelRoomForm";
	}
	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String processNewHotelRoomForm(@PathVariable("petId") final int petId, @Valid final HotelRoomBooking hotelRoom, 
			final BindingResult result, final ModelMap modelMap) throws DataAccessException, DuplicatedHotelRoomForDateException {
		List<HotelRoomBooking> hotelRooms = new ArrayList<>(this.hotelRoomBookingService.findByPetId(petId));
		
		modelMap.addAttribute("hotelRooms", hotelRooms);

		if (result.hasErrors()) {
			return "pets/createOrUpdateHotelRoomForm";
		} else if(hotelRoom.getFinishDate().isBefore(hotelRoom.getStartDate())) {
			result.rejectValue("startDate", "error.startDate", "La fecha de inicio no puede ser anterior a la fecha de finalización");
			return "pets/createOrUpdateHotelRoomForm";
		} else if(roomIsBooked(hotelRoom)) {
			result.rejectValue("startDate", "error.startDate", "La habitación esta reservada para la fecha indicada");
			return "pets/createOrUpdateHotelRoomForm";
		} else if(petHasBookedRoom(hotelRoom)) {
			result.rejectValue("startDate", "error.startDate", "Esta mascota ya tiene una habitación reservada para la fecha indicada");
			return "pets/createOrUpdateHotelRoomForm";
		} else {
			this.hotelRoomBookingService.saveHotelRoom(hotelRoom);
			return "redirect:/owners/{ownerId}";
		}
	}
		
	private boolean roomIsBooked(HotelRoomBooking newHotelRoom) {
		boolean result = false;
		List<HotelRoomBooking> roomsWithTheSameName = new ArrayList<>(hotelRoomBookingService.findAllByHotelRoomName(newHotelRoom.getName()));
		
		for(HotelRoomBooking room: roomsWithTheSameName) {
			if(newHotelRoom.getStartDate().isBefore(room.getFinishDate()) || newHotelRoom.getStartDate().isEqual(room.getFinishDate())) {
				result = true;
			}
		}
				
		return result;
	}
	
	private boolean petHasBookedRoom(HotelRoomBooking newHotelRoom) {
		boolean result = false;
		List<HotelRoomBooking> bookedRoomsByPetId = new ArrayList<>(hotelRoomBookingService.findBookedRoomsByPetId(newHotelRoom.getPet().getId()));

		for(HotelRoomBooking room: bookedRoomsByPetId) {
			if(newHotelRoom.getStartDate().isBefore(room.getFinishDate()) || newHotelRoom.getStartDate().isEqual(room.getFinishDate())) {
				result = true;
			}
		}
		
		return result;
	}
}
