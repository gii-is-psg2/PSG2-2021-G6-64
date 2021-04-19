/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class AdoptionController {

	private static final String VIEWS_ADOPTION_CREATE_OR_UPDATE_FORM = "adoptions/adoptionPetForm";
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM= "adoptions/createOrUpdateAdoptionForm";
	
	private final OwnerService ownerService;
	private final PetService petService;
	private final UserService userService;
	private final AuthoritiesService authoritiesService;
	private final VetService vetService;
	private final AdoptionService adoptService;
	
	@Autowired
	public AdoptionController(OwnerService ownerService,PetService petService, VetService vetService, UserService userService, AuthoritiesService authoritiesService,AdoptionService adoptService) {
		this.ownerService=ownerService;
		this.petService=petService;
		this.vetService = vetService;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.adoptService= adoptService;
	}
	
	
	@GetMapping(value = { "/adoptions" })
	public String showAdoptList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping

		List<Pet> listaPets = petService.findPetsInAdoption();
	
		model.put("listaPets", listaPets);
		
		return "adoptions/showAdoptionPets";
	}

	
	@GetMapping(value = {"/adoptions/{petId}/apply" })
	public String applyAdopt(Map<String, Object> model, @PathVariable("petId") int petId) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		AdoptionApplication adopApp= new AdoptionApplication();
		Pet pet= this.petService.findPetById(petId);
		
		model.put("pet", pet);
		model.put("adoptionApply", adopApp);
		
		return "adoptions/adoptionPetForm";
	}
	
	
	
	
	@PostMapping(value =  "/adoptions/{petId}/apply" )
	public String processCreationForm(@Valid AdoptionApplication adopApp, BindingResult result, ModelMap model,@PathVariable("petId") int petId) {		
		if (result.hasErrors()) {
			model.put("adoptionApply", adopApp);
			return "redirect: /adoptions/{petId}/apply";
		}
		else {
                   		adopApp.setPet(petService.findPetById(petId));
                   		adopApp.setOwner(ownerService.findCurrentOwner());
                    	this.adoptService.saveAdoptionsApplications(adopApp);
                   
                    return "redirect:/adoptions";
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@PreAuthorize("hasRole('admin')")
//	@GetMapping(value = { "/vets/{vetId}/delete" })
//	public String deleteVet(@PathVariable("vetId") int vetId) {
//
//		Vet vet= this.vetService.findVetById(vetId);
//		this.vetService.deleteVet(vet);
//		
//		return "redirect:/vets";
//	}
//	
//	@GetMapping(value = { "/vets.xml"})
//	public @ResponseBody Vets showResourcesVetList() {
//		// Here we are returning an object of type 'Vets' rather than a collection of Vet
//		// objects
//		// so it is simpler for JSon/Object mapping
//		Vets vets = new Vets();
//		vets.getVetList().addAll(this.vetService.findVets());
//		return vets;
//	}
//	
//	@PreAuthorize("hasRole('admin')")
//	@GetMapping(value = {"/vet/new"})
//	public String initCreationForm(Map<String, Object> model) {
//		Vet vet = new Vet();
//		model.put("vet", vet);
//		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
//	}
//
//	@PreAuthorize("hasRole('admin')")
//	@PostMapping(value = {"/vet/new"})
//	public String processCreationForm(
//			@ModelAttribute("vet") @Valid  Vet vet, 
//			BindingResult result,
//			Model model,
//			String[] specialties) {	
//		if (result.hasErrors()) {
//			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
//		}
//		else {
//			//creating owner, user and authorities
//			if(specialties != null) {
//				int i = specialties.length;
//				while (i > 0) {
//					i -= 1;
//					Specialty specialty = this.vetService.findSpecialtyByName(specialties[i]);
//					if(specialty!= null) {
//						vet.addSpecialty(specialty);
//					}
//				}
//			}
//			this.vetService.saveVet(vet);
//			
//			return "redirect:/vets/";
//		}
//	}
//
//	@PreAuthorize("hasRole('admin')")
//	@GetMapping(value = "/vet/{vetId}/edit")
//	public String initUpdateOwnerForm(@PathVariable("vetId") int vetId,Map<String, Object> model) {
//		Vet vet = this.vetService.findVet(vetId);
//		model.put("vet",vet);
//		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
//	}
//
//	@PreAuthorize("hasRole('admin')")
//	@PostMapping(value = "/vet/{vetId}/edit")
//	public String processUpdateOwnerForm(
//			@Valid Vet vet, 
//			BindingResult result,
//			Model model,
//			String[] specialties,
//			@PathVariable("vetId") int vetId) {
//		
//		if (result.hasErrors()) {
//			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
//		} 
//		else {
//			vet.setId(vetId);
//			if(specialties != null) {
//				int i = specialties.length;
//				while (i > 0) {
//					i -= 1;
//					Specialty specialty = this.vetService.findSpecialtyByName(specialties[i]);
//					if(specialty!= null) {
//						vet.addSpecialty(specialty);
//					}
//				}
//			}
//			this.vetService.saveVet(vet);
//			return "redirect:/vets";
//		}
//	}
}
