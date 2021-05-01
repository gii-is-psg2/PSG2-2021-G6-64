package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.model.HotelRoomBooking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelRoomBookingService;
import org.springframework.samples.petclinic.service.HotelRoomService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedHotelRoomForDateException;
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
	private HotelRoomService hotelRoomService;
	@Autowired
	private HotelRoomBookingService hotelRoomBookingService;
	
	private final String CREATE_OR_UPDATE_FORM = "pets/createOrUpdateHotelRoomBookingForm";

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String initNewHotelRoomForm(@PathVariable("petId") final int petId,
			@PathVariable("ownerId") final int ownerId, final ModelMap modelMap) {
		if (!this.ownerService.findCurrentOwner().equals(this.ownerService.findOwnerById(ownerId))) {
			return "redirect:/owners/{ownerId}";
		}

		HotelRoomBooking hotelRoomBooking = new HotelRoomBooking();
		Pet pet = this.petService.findPetById(petId);
		hotelRoomBooking.setPet(pet);
		List<HotelRoomBooking> hotelRoomBookings = new ArrayList<>(
				this.hotelRoomBookingService.findByPetId(pet.getId()));
		List<HotelRoom> hotelRooms = new ArrayList<>(this.hotelRoomService.findAll());

		modelMap.addAttribute("hotelRoomBookings", hotelRoomBookings);
		modelMap.addAttribute("hotelRoomBooking", hotelRoomBooking);
		modelMap.addAttribute("hotelRooms", hotelRooms);
		modelMap.addAttribute("pet", pet);

		return CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String processNewHotelRoomForm(@PathVariable("petId") final int petId,
			@Valid final HotelRoomBooking hotelRoomBooking, final BindingResult result, final ModelMap modelMap)
			throws DataAccessException, DuplicatedHotelRoomForDateException {
		List<HotelRoomBooking> hotelRoomBookings = new ArrayList<>(this.hotelRoomBookingService.findByPetId(petId));
		List<HotelRoom> hotelRooms = new ArrayList<>(this.hotelRoomService.findAll());

		modelMap.addAttribute("hotelRoomBookings", hotelRoomBookings);
		modelMap.addAttribute("hotelRooms", hotelRooms);
		
		final String ERROR_STARTDATE = "error.startDate";
		
		if (result.hasErrors()) {
			return CREATE_OR_UPDATE_FORM;
		} else if (hotelRoomBooking.getFinishDate().isBefore(hotelRoomBooking.getStartDate())) {
			result.rejectValue("startDate", ERROR_STARTDATE,
					"La fecha de inicio no puede ser anterior a la fecha de finalización");
			return CREATE_OR_UPDATE_FORM;
		} else if (roomIsBooked(hotelRoomBooking)) {
			result.rejectValue("hotelRoom", ERROR_STARTDATE, "La habitación esta reservada para la fecha indicada");
			return CREATE_OR_UPDATE_FORM;
		} else if (petHasBookedRoom(hotelRoomBooking)) {
			result.rejectValue("startDate", ERROR_STARTDATE,
					"Esta mascota ya tiene una habitación reservada para la fecha indicada");
			return CREATE_OR_UPDATE_FORM;
		} else {
			this.hotelRoomBookingService.saveHotelRoom(hotelRoomBooking);
			return "redirect:/owners/{ownerId}";
		}
	}

	private boolean roomIsBooked(HotelRoomBooking newHotelRoomBooking) {
		boolean result = false;
		List<HotelRoomBooking> roomsWithTheSameNameAndNumber = new ArrayList<>(
				hotelRoomBookingService.findAllByHotelRoomByNameAndNumber(newHotelRoomBooking.getHotelRoom().getName(),
						newHotelRoomBooking.getHotelRoom().getNumber()));

		for (HotelRoomBooking room : roomsWithTheSameNameAndNumber) {
			if (newHotelRoomBooking.getStartDate().isBefore(room.getFinishDate())
					|| newHotelRoomBooking.getStartDate().isEqual(room.getFinishDate())) {
				result = true;
			}
		}

		return result;
	}

	private boolean petHasBookedRoom(HotelRoomBooking newHotelRoomBooking) {
		boolean result = false;
		List<HotelRoomBooking> bookedRoomsByPetId = new ArrayList<>(
				hotelRoomBookingService.findBookedRoomsByPetId(newHotelRoomBooking.getPet().getId()));

		for (HotelRoomBooking room : bookedRoomsByPetId) {
			if (newHotelRoomBooking.getStartDate().isBefore(room.getFinishDate())
					|| newHotelRoomBooking.getStartDate().isEqual(room.getFinishDate())) {
				result = true;
			}
		}

		return result;
	}
}
