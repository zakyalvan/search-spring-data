package com.innovez.search.samples.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.innovez.core.search.SearchSpecificationHolder;
import com.innovez.core.search.annotation.SearchParams;
import com.innovez.search.samples.entity.Currency;
import com.innovez.search.samples.entity.Organization;
import com.innovez.search.samples.entity.Person;
import com.innovez.search.samples.repository.OrganizationRepository;

@Service
@Transactional(readOnly=true)
public class JpaRepoBackedOrganizationService implements OrganizationService {
	private Logger logger = Logger.getLogger(JpaRepoBackedOrganizationService.class);
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Organization createOrganization(String name, String emailAddress, Person manager, Person contact, Currency usedCurrency) {
		Assert.notNull(name, "Name parameter sould not be null.");
		Assert.notNull(emailAddress, "Email address sould not be null.");
		Assert.notNull(manager, "Contact person parameter sould not be null.");
		Assert.notNull(contact, "Manager parameter sould not be null.");
		
		logger.debug("Create organization with name : " + name);
		Organization organization = new Organization(name, emailAddress);
		organization.setManager(manager);
		organization.setContact(contact);
		organization.setUsedCurrency(usedCurrency);
		return organizationRepository.saveAndFlush(organization);
	}

	@Override
	public Page<Organization> getPagedOrganizationsList(Integer page, Integer size) {
		Assert.notNull(page, "Page parameter should not be null.");
		Assert.notNull(size, "Size parameter should not be null.");
		
		logger.debug("Retrieve paged list of organization.");
		Sort sort = new Sort(Direction.ASC, "name");
		PageRequest pageRequest = new PageRequest(page, size, sort);
		return organizationRepository.findAll(pageRequest);
	}

	@Override
	public Page<Organization> getPagedOrganizationsList(Integer page, Integer size, @SearchParams(target=Organization.class, validate=true) Map<String, Object> parameters) {
		Assert.notNull(page, "Page parameter should not be null.");
		Assert.notNull(size, "Size parameter should not be null.");
		
		logger.debug("Retrieve paged list of organization with search parameters : " + parameters);
		
		Sort sort = new Sort(Direction.ASC, "name");
		PageRequest pageRequest = new PageRequest(page, size, sort);
		
		@SuppressWarnings("unchecked")
		Specification<Organization> specification = (Specification<Organization>) parameters.get(SearchSpecificationHolder.FINAL_SPECIFICATION);
		
		return organizationRepository.findAll(specification, pageRequest);
	}
}
