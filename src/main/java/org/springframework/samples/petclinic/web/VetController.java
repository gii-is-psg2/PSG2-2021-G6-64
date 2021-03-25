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
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
public class VetController {

	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";
	
	private final VetService vetService;
	private final UserService userService;
	private final AuthoritiesService authoritiesService;
	
	@Autowired
	public VetController(VetService clinicService, UserService userService, AuthoritiesService authoritiesService) {
		this.vetService = clinicService;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
	}
	
	@ModelAttribute("specialities")
	public Collection<Specialty> populatePetTypes() {
		return this.vetService.findSpecialty();
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@PreAuthorize("hasRole('admin')")
	@GetMapping(value = { "/vets/{vetId}/delete" })
	public String deleteVet(@PathVariable("vetId") int vetId) {

		Vet vet= this.vetService.findVetById(vetId);
		this.vetService.deleteVet(vet);
		
		return "redirect:/vets";
	}
	
	@GetMapping(value = { "/vets.xml"})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}
	
	@PreAuthorize("hasRole('admin')")
	@GetMapping(value = {"/vet/new"})
	public String initCreationForm(Map<String, Object> model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PreAuthorize("hasRole('admin')")
	@PostMapping(value = {"/vet/new"})
	public String processCreationForm(@Valid Vet vet,@Valid String[] specialties, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating owner, user and authorities
			if(specialties != null) {
				int i = specialties.length;
				while (i > 0) {
					i -= 1;
					Specialty specialty = this.vetService.findSpecialtyByName(specialties[i]);
					if(specialty!= null) {
						vet.addSpecialty(specialty);
					}
				}
			}
			this.vetService.saveVet(vet);
			
			return "redirect:/vets/";
		}
	}

	@PreAuthorize("hasRole('admin')")
	@GetMapping(value = "/vet/{vetId}/edit")
	public String initUpdateOwnerForm(@PathVariable("vetId") int vetId,Map<String, Object> model) {
		Vet vet = this.vetService.findVet(vetId);
		model.put("vet",vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PreAuthorize("hasRole('admin')")
	@PostMapping(value = "/vet/{vetId}/edit")
	public String processUpdateOwnerForm(@Valid Vet vet,@Valid String[] specialties, BindingResult result,
			@PathVariable("vetId") int vetId) {
		if (result.hasErrors()) {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}
		else {
			vet.setId(vetId);
			int i = specialties.length;
			while (i > 0) {
				i -= 1;
				Specialty specialty = this.vetService.findSpecialtyByName(specialties[i]);
				if(specialty!= null) {
					vet.addSpecialty(specialty);
				}
			}
			this.vetService.saveVet(vet);
			return "redirect:/vets";
		}
	}
}
