package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.stereotype.Service;

@Service
public class CauseService {
	
	private CauseRepository causeRepository;
	
    @Autowired
    public CauseService(CauseRepository causeRepository) {
    	this.causeRepository = causeRepository;
    }
    
    @Transactional
    public void saveCause(Cause cause) throws DataAccessException {
    	this.causeRepository.save(cause);
    }
    
    public Collection<Cause> findcause() throws DataAccessException {
    	return this.causeRepository.findAll();
    }
    
    public Cause findCauseById(int causeId) {
    	return this.causeRepository.findById(causeId);
    }
}
