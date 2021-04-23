package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
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
	@Positive
	private Double budgetTarget;
	
	@Column(name = "budget_achieved")
	@NotNull
	@Min(value = 0)
	private Double budgetAchieved;

	@Column(name = "organization")
	@NotEmpty
	private String organization;

	@Column(name = "closed")
	private boolean closed;
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cause")
//	private Set<Donation> donations;
	
//	public Set<Donation> getDonation() {
//		return donations;
//	}
	
//	public void setDonations(Set<Donation> donations) {
//		this.donations = donations;
//	}

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

	public boolean isClosed() {
		return this.closed;
	}

	public void setClosed(final boolean closed) {
		this.closed = closed;
	}
	
	public Double getBudgetAchieved() {
		return budgetAchieved;
	}

	public void setBudgetAchieved(Double budgetAchieved) {
		this.budgetAchieved = budgetAchieved;
	}
	
	public Double getBudgetTarget() {
		return budgetTarget;
	}

	public void setBudgetTarget(Double budgetTarget) {
		this.budgetTarget = budgetTarget;
	}
}
