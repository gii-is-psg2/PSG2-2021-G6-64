package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Authorities;



public interface AuthoritiesRepository extends CrudRepository<Authorities, String>{
	
	@Query("SELECT ALL a from Authorities a where a.authority=:rol")
	Authorities findByRol(String rol);
	
}
