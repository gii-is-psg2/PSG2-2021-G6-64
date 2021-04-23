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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class AdoptionController {

	
	private final OwnerService ownerService;
	private final PetService petService;
	private final AdoptionService adoptService;
	
	@Autowired
	public AdoptionController(OwnerService ownerService,PetService petService,AdoptionService adoptService) {
		this.ownerService=ownerService;
		this.petService=petService;
		this.adoptService= adoptService;
	}
	
	
	@GetMapping(value = { "/adoptions" })
	public String showAdoptList(Map<String, Object> model) {
		List<Pet> listaPets = petService.findPetsInAdoption();
		Owner owner = this.ownerService.findCurrentOwner();

		int currentOwnerId = 0;
		Set<Pet> idPetsApply = new HashSet<>();
		if(owner != null) {
			currentOwnerId = owner.getId();
			idPetsApply = owner.getApplication().stream().map(x -> x.getPet()).collect(Collectors.toSet());
		}

		model.put("currentApplications",idPetsApply);
		model.put("currentOwnerId",currentOwnerId);
		model.put("listaPets", listaPets);
		
		return "adoptions/showAdoptionPets";
	}

	
	@GetMapping(value = {"/adoptions/{petId}/apply" })
	public String InitApplyAdoptForm(Map<String, Object> model, @PathVariable("petId") int petId) {
		AdoptionApplication adopApp= new AdoptionApplication();
		Pet pet = this.petService.findPetById(petId);
		
		model.put("pet", pet);
		model.put("adoptionApply", adopApp);
		
		return "adoptions/adoptionPetForm";
	}
	
	

	@PostMapping(value =  "/adoptions/{petId}/apply" )
	public String processApplyAdoptForm(@Valid AdoptionApplication adopApp, BindingResult result, ModelMap model,@PathVariable("petId") int petId) {		
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
	
}
