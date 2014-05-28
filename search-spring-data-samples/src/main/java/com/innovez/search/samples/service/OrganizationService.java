package com.innovez.search.samples.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.innovez.search.samples.entity.Organization;

public interface OrganizationService {
	Organization createOrganization(String name, String emailAddress, String contactPerson);
	Page<Organization> getPagedOrganizationsList(Integer page, Integer size);
	Page<Organization> getPagedOrganizationsList(Integer page, Integer size, Map<String, Object> parameters);
}