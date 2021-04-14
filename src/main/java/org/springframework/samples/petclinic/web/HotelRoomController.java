package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.HotelRoomService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedHotelRoomForDateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HotelRoomController {

	@Autowired
	private HotelRoomService hotelRoomService;
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/hotel/rooms/new")
	public String initNewHotelRoomForm(final ModelMap modelMap) {
		HotelRoom hotelRoom = new HotelRoom();
		
		modelMap.addAttribute("hotelRoom", hotelRoom);

		return "hotel/rooms/createOrUpdateHotelRoomForm";
	}

	@PostMapping(value = "/hotel/rooms/new")
	public String processNewHotelRoomForm(@Valid final HotelRoom hotelRoom, final BindingResult result, final ModelMap modelMap)
			throws DataAccessException, DuplicatedHotelRoomForDateException {
		
		List<HotelRoom> hotelRooms = new ArrayList<>(this.hotelRoomService.findAll());

		modelMap.addAttribute("hotelRooms", hotelRooms);
		
		if (result.hasErrors()) {
			return "hotel/rooms/createOrUpdateHotelRoomForm";
		} else if (!hotelRoomService.findAllByHotelRoomByName(hotelRoom.getName()).isEmpty()) {
			result.rejectValue("name", "error.name",
					"Ya existe una habitación con este nombre");
			return "hotel/rooms/createOrUpdateHotelRoomForm";
		} else if (!hotelRoomService.findAllByHotelRoomByNumber(hotelRoom.getNumber()).isEmpty()) {
			result.rejectValue("number", "error.number",
					"Ya existe una habitación con este número");
			return "hotel/rooms/createOrUpdateHotelRoomForm";
		} else {
			System.out.println("envia");
			this.hotelRoomService.saveHotelRoom(hotelRoom);
			return "redirect:/hotel/rooms";
		}
	}
	
	@GetMapping(value = { "/hotel/rooms" })
	public String showVetList(Map<String, Object> model) {
		List<HotelRoom> hotelRooms = new ArrayList<>(this.hotelRoomService.findAll());
		model.put("hotelRooms", hotelRooms);
		return "hotel/rooms/roomList";
	}
}
