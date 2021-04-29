package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {
	
	private DonationRepository donationRepo;
	
	@Autowired
	public DonationService(DonationRepository donationRepo) {
		this.donationRepo = donationRepo;
	}
	
	@Transactional(readOnly = true)
	public List<Donation> getDonationsCauseId(Integer id) {
		return donationRepo.findByCauseId(id);
	}
	
	@Transactional
	public Donation save(Donation donation) {
		return donationRepo.save(donation);
	}

}
