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

import java.time.LocalDate;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final PetService petService;
    private final OwnerService ownerService;
    
    private static final String REDIRECT_OWNERS = "redirect:/owners/{ownerId}";

	@Autowired
	public PetController(PetService petService, OwnerService ownerService) {
		this.petService = petService;
        this.ownerService = ownerService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return this.ownerService.findOwnerById(ownerId);
	}
     
                
	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping(value = "/pets/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/pets/new")
	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {		
		
		
		if (result.hasErrors() || !comprobacionNacimiento(pet)) { // Fecha en futuro -> !(false) -> true
		
			owner.addPet(pet);
			model.put("pet", pet);
			
			if(!comprobacionNacimiento(pet)) {
				result.rejectValue("birthDate", "error.birthDate",
						"La fecha no puede ser en futuro");
			}
			
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
            try{
            	owner.addPet(pet);
            	this.petService.savePet(pet);
            	
            }catch(DuplicatedPetNameException ex){
                result.rejectValue("name", "duplicate", "already exists");
                return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
            }
            return REDIRECT_OWNERS;
		}
	}

	@GetMapping(value = "/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, @PathVariable("ownerId") int ownerId, ModelMap model) {
		Owner currentOwner = this.ownerService.findCurrentOwner();
		Pet pet = this.petService.findPetById(petId);
		
		if(!this.ownerService.ownerIsLoggedOwnerById(ownerId) || !pet.getOwner().equals(currentOwner)) {
			return REDIRECT_OWNERS;
		}
		
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

    /**
     *
     * @param pet
     * @param result
     * @param petId
     * @param model
     * @param owner
     * @param model
     * @return
     */
        @PostMapping(value = "/pets/{petId}/edit")
	public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner,@PathVariable("petId") int petId, ModelMap model) {
        	if (result.hasErrors() || !comprobacionNacimiento(pet)) { // Fecha en futuro -> !(false) -> true
        		
    			owner.addPet(pet);
    			model.put("pet", pet);
    			
    			if(!comprobacionNacimiento(pet)) {
    				result.rejectValue("birthDate", "error.birthDate",
    						"La fecha no puede ser en futuro");
    			}
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		else {
                        Pet petToUpdate=this.petService.findPetById(petId);
			BeanUtils.copyProperties(pet, petToUpdate, "id","owner","visits");                                                                                  
                    try {  
                    	petToUpdate.setNotAdoption();
                        this.petService.savePet(petToUpdate);                    
                    } catch (DuplicatedPetNameException ex) {
                        result.rejectValue("name", "duplicate", "already exists");
                        return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
                    }
			return REDIRECT_OWNERS;
		}
    }
    	
        
    @GetMapping(value = "/pets/{petId}/delete")
	public String deletePet(@PathVariable("petId") int petId,@PathVariable("ownerId") int ownerId) {
    	
    	Pet pet = this.petService.findPetById(petId);
    	Owner owner = this.ownerService.findOwnerById(ownerId);
    	
    	if(!petCanBeDeleted(pet)) {
    		return REDIRECT_OWNERS;
    	}
    	
    	owner.removePet(pet);
		this.petService.deletePet(pet);
		
		return REDIRECT_OWNERS;
	}

    
    @GetMapping(value = "/pets/{petId}/adopt")
	public String adoptPet(@PathVariable("petId") int petId,@PathVariable("ownerId") int ownerId) throws DuplicatedPetNameException {
    	
        Pet pet = this.petService.findPetById(petId);
        if (!this.petService.esReptil(pet)) {
        	pet.setAdoption();
    		this.petService.savePet(pet);
        }
		return REDIRECT_OWNERS;
	}
    

    @GetMapping(value = "/pets/{petId}/visit/{visitId}/delete")
	public String deletePetVisit(@PathVariable("visitId") int visitId, @PathVariable("petId") int petId, @PathVariable("ownerId") int ownerId) {
    	Pet mascota= this.petService.findPetById(petId);
    	Visit visita= this.petService.findVisitById(visitId);
    	
    	if(visitCanBeDeleted(visita) && this.ownerService.findCurrentOwner().equals(mascota.getOwner())) {
    		mascota.removeVisit(visita);
        	this.petService.deleteVisit(visita);
    	}
    	
		return REDIRECT_OWNERS;
	}

    private boolean petCanBeDeleted(Pet pet) {
    	boolean result = false;
    	
    	if(pet.getOwner().equals(this.ownerService.findCurrentOwner())) {
    		result = true;
    	}
    	
    	return result;
    }
    
    private boolean visitCanBeDeleted(Visit visit) {
    	boolean result = false;
    	
    	if(!visit.getDate().isBefore(LocalDate.now())) {
    		result = true;
    	}
    	
    	return result;
    }
    
    private boolean comprobacionNacimiento(Pet pet) {
    	if(pet.getBirthDate().isAfter(LocalDate.now())) {
    		   		return false;
    	}else {
    		return true;
    	}
    }
}
