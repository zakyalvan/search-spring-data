package com.innovez.search.samples.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.innovez.search.samples.entity.Organization;
import com.innovez.search.samples.service.OrganizationService;

@Controller
@RequestMapping("/organizations")
public class OrganizationController {
	private Logger logger = Logger.getLogger(OrganizationController.class);
	
	@Autowired
	private OrganizationService organizationService;
	
	@RequestMapping(value={"", "/"}, params="create", method=RequestMethod.GET)
	public String createForm(Model model) {
		logger.debug("Show creation form.");
		model.addAttribute("command", new Organization());
		return "organizations/create";
	}
	
	@RequestMapping(value={"", "/"}, method=RequestMethod.POST)
	public String create(@Valid @ModelAttribute("command") Organization organization, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			logger.error("Submitted form contains error, reject submission.");
			return "organizations/create";
		}
		logger.debug("Create organization.");
		organizationService.createOrganization(organization.getName(), organization.getEmail(), organization.getContactPerson());
		return "redirect:/organizations";
	}
	
	@RequestMapping(value={"", "/"}, method=RequestMethod.GET)
	public String list(@RequestParam(value="page", defaultValue="0") Integer page, @RequestParam(value="size", defaultValue="10")Integer size, Model model) {
		logger.debug("Retrieve list of organization");
		Page<Organization> pagedDataList = organizationService.getPagedOrganizationsList(page, size);
		model.addAttribute("pagedDataList", pagedDataList);
		return "organizations/list";
	}
	
	@RequestMapping(value={"", "/"}, params={"search"}, method=RequestMethod.POST)
	public String search() {
		logger.debug("Handle search organizations");
		return "redirect:/organizations";
	}
}