package com.innovez.search.samples.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.innovez.search.samples.CoreConfig;
import com.innovez.search.samples.DataConfig;
import com.innovez.search.samples.SearchConfig;
import com.innovez.search.samples.service.OrganizationService;

/**
 * Test case for organization services samples.
 * 
 * @author zakyalvan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CoreConfig.class, DataConfig.class, SearchConfig.class})
public class OrganizationServiceTest {
	@Autowired
	private OrganizationService organizationService;
	
	@Test
	public void testRetrievePagedOrganizationList() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "service prov");
		organizationService.getPagedOrganizationsList(0, 10, parameters);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRetrievePagedOrganizationListWithInvalidParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", "service prov");
		parameters.put("test", "bla bla bla");
		organizationService.getPagedOrganizationsList(0, 10, parameters);
	}
}
