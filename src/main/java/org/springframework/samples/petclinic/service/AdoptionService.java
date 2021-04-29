package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.samples.petclinic.repository.HotelRoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionService {
	
	
	private AdoptionApplicationRepository adoptionRepository;
	
	@Autowired
	public AdoptionService(AdoptionApplicationRepository adoptionRepository) {
		this.adoptionRepository = adoptionRepository;
	}
	
	
	@Transactional
	public Collection<AdoptionApplication> findAllAdoptionsApplications() {
		return adoptionRepository.findAll();
	}

	@Transactional
	public List<AdoptionApplication> findAdoptionsByPet(int petId) {
		return adoptionRepository.findByPet(petId);
	}
	
	@Transactional
	public AdoptionApplication findAdoptionsById(int adopId) {
		
		return adoptionRepository.findById(adopId).orElse(null);
	}
	
	@Transactional
	public void saveAdoptionsApplications(AdoptionApplication adopApply) {
		adoptionRepository.save(adopApply);
	}
	
	
	@Transactional
	public void deleteAllAdoptionsApplications(Set<AdoptionApplication> adopApply) {
		adoptionRepository.deleteAll(adopApply);
	}
	
	
	
	
	
	
	
}
