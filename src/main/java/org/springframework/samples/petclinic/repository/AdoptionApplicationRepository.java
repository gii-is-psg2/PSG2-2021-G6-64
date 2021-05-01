package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionApplicationRepository extends CrudRepository<AdoptionApplication, Integer> {

	List<AdoptionApplication> findAll();

	
	
	
	@Query("SELECT ALL h from AdoptionApplication h where h.pet.id = :petId")
	List<AdoptionApplication> findByPet(@Param("petId") int petId);
	
	
	Optional<AdoptionApplication> findById(Integer id);
	
}
