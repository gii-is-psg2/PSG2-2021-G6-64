package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/causes/{causeId}/donations")
public class DonationController {

	@Autowired
	private DonationService donationService;

	@Autowired
	private CauseService causeService;

	@Autowired
	private OwnerService ownerService;

	@GetMapping()
	public String showDonationsList(Map<String, Object> model, @PathVariable("causeId") Integer causeId) {
		model.put("donations", donationService.getDonationsCauseId(causeId));
		model.put("cause", causeService.findCauseById(causeId));
		return "donations/donationsList";
	}

	@GetMapping(value = "/new")
	public String initDonationForm(Map<String, Object> model, @PathVariable("causeId") Integer causeId) {
		if (causeService.findCauseById(causeId).getClosed()) {
			return "redirect: /causes/" + causeId;
		}
		Donation donation = new Donation();
		donation.setAmount(0.1);
		model.put("donation", donation);

		return "donations/createDonationForm";
	}

	@PostMapping(value = "/new")
	public String processDonationForm(@Valid Donation donation, final BindingResult result,
			@PathVariable("causeId") Integer causeId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("donation", donation);
			return "donations/createDonationForm";
		} else if (this.causeService.findCauseById(causeId).getClosed()) {
			return "redirect:/causes/" + causeId;
		} else {
			Cause cause = this.causeService.findCauseById(causeId);
			Owner owner = this.ownerService.findCurrentOwner();
			if (cause != null && owner != null) {
				cause.addDonation(donation);
				donation.setOwner(owner);
				this.donationService.save(donation);
				causeService.saveCause(cause);
				return "redirect:/causes/" + causeId;
			} else {
				throw new RuntimeException();
			}

		}
	}

}
