package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HotelRoomController {

	private final PetService petService;

	@Autowired
	public HotelRoomController(PetService petService) {
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("hotelRoom")
	public HotelRoom loadPetWithHotelRoom(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		HotelRoom hotelRoom = new HotelRoom();
		pet.addHotelRooms(hotelRoom);
		return hotelRoom;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/hotel-rooms/new")
	public String initNewHotelRoomForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateHotelRoomForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hotel-rooms/new")
	public String processNewHotelRoomForm(@Valid HotelRoom hotelRoom, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateHotelRoomForm";
		}
		else {
			this.petService.saveHotelRoom(hotelRoom);
			return "redirect:/owners/{ownerId}";
		}
	}

//	@GetMapping(value = "/owners/*/pets/{petId}/hotel-rooms")
//	public String showHotelRooms(@PathVariable int petId, Map<String, Object> model) {
//		model.put("hotelRooms", this.petService.findPetById(petId).getHotelRooms());
//		return "hotelRoomsList";
//	}

}
