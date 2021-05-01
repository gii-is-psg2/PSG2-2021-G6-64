package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DonationController.class,
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
public class DonationControllerTests {
	
	@MockBean
	private DonationService donationService;
	
	@MockBean
	private CauseService causeService;
	
	@MockBean
	private OwnerService ownerService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static final int TEST_CAUSE_ID = 1;

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
	
	private Cause dummyCause() {
		
		Cause cause = new Cause();
		cause.setId(1);
		cause.setName("Test Name");
		cause.setOrganization("Test Org");
		cause.setDescription("Test description");
		cause.setBudgetTarget(1200.0);
		cause.setDonations(new ArrayList<>());
		return cause;
	}
	

	private Owner dummyOwner;
	private Cause dummyCause;
	
	@BeforeEach
	void setup() {
		dummyOwner = dummyOwner();
		dummyCause = dummyCause();
		
		given(this.causeService.findCauseById(TEST_CAUSE_ID)).willReturn(dummyCause);
		given(this.ownerService.findCurrentOwner()).willReturn(dummyOwner);

	}
	
	@WithMockUser(value = "spring")
    @Test
	void testInitNewDonationForm() throws Exception {
		given(causeService.findCauseById(TEST_CAUSE_ID)).willReturn(dummyCause);
		
	    mockMvc.perform(get("/causes/{causeId}/donations/new", TEST_CAUSE_ID)
	            .with(csrf()))
		        .andExpect(status().isOk())
				.andExpect(view().name("donations/createDonationForm")
        );
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessNewDonationFormSuccess() throws Exception {
		
	mockMvc.perform(post("/causes/{causeId}/donations/new", TEST_CAUSE_ID)
			.with(csrf())
			.param("description", "Test Desc")
			.param("amount", "10.02"))                                
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/causes/"+TEST_CAUSE_ID));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessNewDonationFormFail() throws Exception {
		
	mockMvc.perform(post("/causes/{causeId}/donations/new", TEST_CAUSE_ID)
			.with(csrf())
			.param("description", "Test Desc")
			.param("amount", "10.0211"))  
			.andExpect(model().attributeHasErrors("donation"))
			.andExpect(view().name("donations/createDonationForm"));
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testProcessNewDonationFormFailOwner() throws Exception {
		given(this.causeService.findCauseById(TEST_CAUSE_ID)).willReturn(null);
		given(this.ownerService.findCurrentOwner()).willReturn(null);
		
		mockMvc.perform(post("/causes/{causeId}/donations/new", TEST_CAUSE_ID)
				.with(csrf())
				.param("description", "Test Desc")
				.param("amount", "10.02"))  
				.andExpect(status().isOk());
	}
}
