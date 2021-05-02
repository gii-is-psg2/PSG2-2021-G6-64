package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdoptionController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
class AdoptionControllerTests {
	
	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private PetService petService;
	
	@MockBean
	private AdoptionService adoptService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static final int TEST_PET_ID = 1;
	
	private Owner dummyOwner() {
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
	
	private Pet dummyPet() {
		PetType type = new PetType();
		type.setId(1);
		type.setName("dummy");
		
		Pet pet = new Pet();
		pet.setId(1);
		pet.setBirthDate(LocalDate.now().minusDays(365));
		pet.setName("Dummy pet");
		pet.setType(type);
		pet.setAdoption();
		
		return pet;
	}
	
	private Owner dummyOwner;
	private Pet dummyPet;
	private List<Pet> dummyListPet;
	
	@BeforeEach
	void setup() {
		dummyOwner = dummyOwner();
		dummyPet = dummyPet();
		dummyOwner.addPet(dummyPet);
		dummyListPet = new ArrayList<Pet>();
		dummyListPet.add(dummyPet);
		
		given(this.ownerService.findCurrentOwner()).willReturn(dummyOwner);
		given(this.petService.findPetById(TEST_PET_ID)).willReturn(dummyPet);
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testInitApplyForm() throws Exception {
			
	    mockMvc.perform(get("/adoptions/{petId}/apply", TEST_PET_ID)
	            .with(csrf())
	            .param("pet.id", Integer.toString(TEST_PET_ID)))
		        .andExpect(status().isOk())
				.andExpect(view().name("adoptions/adoptionPetForm")
	        );
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessApplyForm() throws Exception {
			
	    mockMvc.perform(post("/adoptions/{petId}/apply", TEST_PET_ID)
	            .with(csrf())
	            .param("solicitud", "Adoption Test"))
			    .andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/adoptions"));
		        
	}

}
