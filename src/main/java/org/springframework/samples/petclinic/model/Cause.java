package org.springframework.samples.petclinic.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "causes")
public class Cause extends NamedEntity {

	@Column(name = "description")
	@NotEmpty
	private String description;

	@Column(name = "budget_target")
	@NotNull
	@Digits(integer = 10, fraction = 2)
	@Positive
	private Double budgetTarget;
	

	@Column(name = "organization")
	@NotEmpty
	private String organization;

	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cause")
	private List<Donation> donations;
	
	public List<Donation> getDonation() {
		return donations;
	}
	
	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(final String organization) {
		this.organization = organization;
	}

	public boolean getClosed() {
		return getBudgetAchieved() >= budgetTarget;
	}


	public Double getBudgetAchieved() {
		
		double result = 0.0;
		for (Donation donation : donations) {
			result += donation.getAmount();
		}
		
		result = (double) Math.round(result * 100) / 100;
		return result;
	}

	
	public Double getBudgetTarget() {
		return budgetTarget;
	}

	public void setBudgetTarget(Double budgetTarget) {
		this.budgetTarget = budgetTarget;
	}
	
	public void addDonation(Donation donation) {
		this.donations.add(donation);
		donation.setCause(this);
	}
	
}
