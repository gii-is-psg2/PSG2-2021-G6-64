package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CauseController {
	
	private static final String VIEWS_CAUSE_CREATE_OR_UPDATE_FORM = "causes/createOrUpdateCauseForm";
	private final CauseService causeService;
	
	@Autowired
	public CauseController(CauseService causeService) {
		this.causeService = causeService;
	}
	
	@GetMapping(value = "/causes/new")
	public String initCreationForm(final ModelMap model) {
		Cause cause = new Cause();
		model.put("cause", cause);
		return VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/causes/new")
	public String processCreationForm(@Valid Cause cause, final BindingResult result, final ModelMap model) {
		if(result.hasErrors()) {
			model.put("cause", cause);
			return VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
		} else {
			this.causeService.saveCause(cause);
			return "redirect:/causes";
		}
	}
	
	@GetMapping("/causes/{causeId}")
	public ModelAndView showCause(@PathVariable("causeId") int causeId) {
		ModelAndView modelAndView = new ModelAndView("causes/causeDetails");
		modelAndView.addObject(this.causeService.findCauseById(causeId));
		return modelAndView;
	}
	
	@GetMapping(value = {"/causes"})
	public String showCausesList(Map<String, Object> model) {
		Collection<Cause> causes = new ArrayList<Cause>();
		causes.addAll(this.causeService.findcause());
		model.put("causes", causes);
		return "causes/causesList";
	}
}
