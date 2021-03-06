package com.innovez.search.samples.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.innovez.core.search.annotation.SearchTarget;
import com.innovez.search.samples.entity.Organization;
import com.innovez.search.samples.repository.CurrencyRepository;
import com.innovez.search.samples.repository.PersonRespository;
import com.innovez.search.samples.service.OrganizationService;
import com.innovez.web.support.search.SimpleSearchForm;

@Controller
@RequestMapping("/organizations")
@SessionAttributes(value={OrganizationController.SEARCH_FORM_NAME})
public class OrganizationController {
	public static final String SEARCH_FORM_NAME = "searchOrgnazitionsForm";
	
	private Logger logger = Logger.getLogger(OrganizationController.class);
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private PersonRespository personRespository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@RequestMapping(value={"", "/"}, params="create", method=RequestMethod.GET)
	public String createForm(Model model) {
		logger.debug("Show creation form.");
		model.addAttribute("people", personRespository.findAll());
		model.addAttribute("currencies", currencyRepository.findAll());
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
		organizationService.createOrganization(organization.getName(), organization.getEmail(), organization.getPeopleNumber(), organization.getManager(), organization.getContact(), organization.getUsedCurrency());
		return "redirect:/organizations";
	}
	
	public String detail() {
		
		return "organizations/detail";
	}
	
	@ModelAttribute(SEARCH_FORM_NAME)
	public SimpleSearchForm prepareSimpleSearchForm() {
		logger.debug("============== Prepare simple search form.");
		return new SimpleSearchForm(Organization.class);
	}
	
	@RequestMapping(value={"", "/"}, method=RequestMethod.GET)
	public String list(@ModelAttribute(SEARCH_FORM_NAME) SimpleSearchForm searchForm, @RequestParam(value="page", defaultValue="0") Integer page, @RequestParam(value="size", defaultValue="10")Integer size, Model model) {
		logger.debug("Retrieve list of organization");
		
		if(searchForm.isEnabled()) {
			logger.debug("Search enabled, retrieve paged data list with search parameters.");
			Page<Organization> pagedDataList = organizationService.getOrganizations(page, size, searchForm.getParameters());
			model.addAttribute("pagedDataList", pagedDataList);
		}
		else {
			logger.debug("Search disabled.");
			Page<Organization> pagedDataList = organizationService.getOrganizations(page, size);
			model.addAttribute("pagedDataList", pagedDataList);
		}
		
		return "organizations/list";
	}
	
	@RequestMapping(value={"", "/"}, params={"search"}, method=RequestMethod.POST)
	public String search(@SearchTarget(Organization.class) SimpleSearchForm searchForm, Model model) {
		logger.debug("Handle search with target : " + searchForm.getSearchTarget() + " and parameters : " + searchForm.getParameters());
		model.addAttribute(SEARCH_FORM_NAME, searchForm);
		return "redirect:/organizations";
	}
}
