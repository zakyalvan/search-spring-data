package com.innovez.search.samples.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.innovez.search.samples.entity.Currency;
import com.innovez.search.samples.entity.Organization;
import com.innovez.search.samples.entity.Person;

public interface OrganizationService {
	Organization createOrganization(String name, String emailAddress, Integer peopleNumber, Person manager, Person contact, Currency usedCurrency);
	Page<Organization> getPagedOrganizationsList(Integer page, Integer size);
	Page<Organization> getPagedOrganizationsList(Integer page, Integer size, Map<String, Object> parameters);
}