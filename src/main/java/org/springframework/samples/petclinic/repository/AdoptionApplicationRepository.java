package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.HotelRoom;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionApplicationRepository extends CrudRepository<AdoptionApplication, Integer> {

	List<AdoptionApplication> findAll();

	
	
	
	@Query("SELECT ALL h from AdoptionApplication h where h.pet.id = :petId")
	List<AdoptionApplication> findByPet(@Param("petId") int petId);
	
	
	Optional<AdoptionApplication> findById(Integer id);
	
	
	
	
	
	
	
	
	
	
//	@Query("SELECT ALL h from HotelRoom h where h.pet.id = :petId")
//	List<HotelRoom> findByPetId(Integer petId);
//		
//	@Query("SELECT ALL h from HotelRoom h where h.name = :name")
//	Collection<HotelRoom> findAllByHotelName(@Param("name") String name) throws DataAccessException;
//	
}
