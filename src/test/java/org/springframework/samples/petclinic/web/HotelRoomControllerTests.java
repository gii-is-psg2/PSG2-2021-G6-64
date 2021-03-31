package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.HotelRoomService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HotelRoomController.class,
			excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
			excludeAutoConfiguration= SecurityConfiguration.class)
class HotelRoomControllerTests {

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;

	
	@Autowired
	private HotelRoomController hotelRoomController;

	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private PetService petService;

	
	@MockBean
	private HotelRoomService hotelRoomService;
	
	@Autowired
	private MockMvc mockMvc;

	private Owner dummyOwner(Integer ownerId) {
		User user = new User();
		user.setUsername("dummyOwner");
		user.setPassword("dummyOwnerPassword");
		user.setEnabled(true);
		
		Authorities auths = new Authorities();
		auths.setId(1);
		auths.setUser(user);
		auths.setAuthority("owner");
		
		Owner owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Dummy");
		owner.setLastName("Owner 1");
		owner.setAddress("Address");
		owner.setCity("City");
		owner.setTelephone("123456789");
		owner.setUser(user);
		
		return owner;
	}
	
	private Pet dummyPet(Integer petId) {
		PetType type = new PetType();
		type.setId(1);
		type.setName("dummy");
		
		Pet pet = new Pet();
		pet.setId(1);
		pet.setBirthDate(LocalDate.now().minusDays(365));
		pet.setName("Dummy pet");
		pet.setType(type);
		
		return pet;
	}
	
	private Owner dummyOwner;
	private Owner badOwner;
	private Pet dummyPet;

	@BeforeEach
	void setup() {
		dummyOwner = dummyOwner(TEST_OWNER_ID);
		dummyPet = dummyPet(TEST_PET_ID);
		dummyOwner.addPet(dummyPet);
		badOwner = dummyOwner(2);
		badOwner.getUser().setUsername("badUsername");
		
		given(this.ownerService.findOwnerById(TEST_OWNER_ID)).willReturn(dummyOwner);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(dummyPet);
		given(this.hotelRoomService.findByPetId(TEST_PET_ID)).willReturn(Collections.emptyList());
	}

    
    @WithMockUser(value = "spring")
	    @Test
	void testInitNewHotelRoomForm() throws Exception {
		given(this.ownerService.findCurrentOwner()).willReturn(dummyOwner);
    		
        mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/hotel-rooms/new", TEST_OWNER_ID, TEST_PET_ID)
                .with(csrf())
                .param("hotelRoom.pet.owner.id", Integer.toString(TEST_OWNER_ID))
                .param("hotelRoom.pet.id", Integer.toString(TEST_PET_ID))
                .param("hotelRoom.pet.owner.user.username", "dummyUsername")
                .param("hotelRoom.pet.name", "Dummy pet"))
		        .andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateHotelRoomForm")
            );
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testInitNewHotelRoomFormBadUser() throws Exception {
		Owner badOwner = dummyOwner(2);
		badOwner.getUser().setUsername("badUsername");
		
		given(this.ownerService.findCurrentOwner()).willReturn(badOwner);
		
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/hotel-rooms/new", TEST_OWNER_ID, TEST_PET_ID))
	        .andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/owners/{ownerId}"));
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testProcessNewHotelRoomFormSuccess() throws Exception {
	given(this.ownerService.findCurrentOwner()).willReturn(dummyOwner);
		
	mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/hotel-rooms/new", TEST_OWNER_ID, TEST_PET_ID)
			.with(csrf())
			.param("name", "Habitación 18")
			.param("startDate", "2030/01/04")
			.param("finishDate", "2030/01/04")
			.param("pet.id", dummyPet.getId().toString()))                                
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
    }
    
    @WithMockUser(value = "spring")
	    @Test
	void testProcessNewHotelRoomFromPastDate() throws Exception {
	given(this.ownerService.findCurrentOwner()).willReturn(dummyOwner);
		
	mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/hotel-rooms/new", TEST_OWNER_ID, TEST_PET_ID)
			.with(csrf())
			.param("name", "Habitación 18")
			.param("startDate", "2010/01/04")
			.param("finishDate", "2010/01/04")
			.param("pet.id", dummyPet.getId().toString()))                                
			.andExpect(model().attributeHasErrors("hotelRoom"))
		    .andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateHotelRoomForm"));
	}
}