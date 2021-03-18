package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.HotelRoomService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HotelRoomController {

	@Autowired
	private PetService petService;
	@Autowired
	private HotelRoomService hotelRoomService;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String initNewHotelRoomForm(@PathVariable("petId") final int petId, final ModelMap modelMap) {
		HotelRoom hotelRoom = new HotelRoom();	
		Pet pet = this.petService.findPetById(petId);
		hotelRoom.setPet(pet);
		List<HotelRoom> hotelRooms = new ArrayList<>(this.hotelRoomService.findByPetId(pet.getId()));
		
		modelMap.addAttribute("hotelRoom", hotelRoom);
		modelMap.addAttribute("hotelRooms", hotelRooms);
		modelMap.addAttribute("petId", petId);
		
		return "pets/createOrUpdateHotelRoomForm";
	}
	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String processNewHotelRoomForm(@PathVariable("petId") final int petId, @Valid final HotelRoom hotelRoom, 
			final BindingResult result, final ModelMap modelMap) {
		Pet pet = this.petService.findPetById(petId);
		hotelRoom.setPet(pet);
		List<HotelRoom> hotelRooms = new ArrayList<>(this.hotelRoomService.findByPetId(pet.getId()));
		modelMap.addAttribute("hotelRooms", hotelRooms);

		if (result.hasErrors()) {
			return "pets/createOrUpdateHotelRoomForm";
		} else if(hotelRoom.getFinishDate().isBefore(hotelRoom.getStartDate())) {
			result.rejectValue("startDate", "error.startDate", "La fecha de inicio no puede ser anterior a la fecha de finalización");
			return "pets/createOrUpdateHotelRoomForm";
		} else if(roomIsBooked(hotelRoom)) {
			result.rejectValue("startDate", "error.startDate", "La habitación esta reservada para la fecha indicada");
			return "pets/createOrUpdateHotelRoomForm";
		} else {
			this.hotelRoomService.saveHotelRoom(hotelRoom);
			return "redirect:/owners/{ownerId}";
		}
	}
		
	public boolean roomIsBooked(HotelRoom newHotelRoom) {
		boolean result = false;
		
		List<HotelRoom> roomsWithTheSameName = new ArrayList<>(hotelRoomService.findAllByHotelName(newHotelRoom.getName()));
		
		for(HotelRoom room: roomsWithTheSameName) {
			if(newHotelRoom.getStartDate().isBefore(room.getFinishDate()) || newHotelRoom.getStartDate().isEqual(room.getFinishDate())) {
				result = true;
			}
		}
		
		System.out.println("resultado de todo "+result);
		
		return result;
	}
}
