package com.innovez.core.entity.support.search;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.innovez.core.search.SearchManager;

public class SearchManagerTest {
	private Logger logger = Logger.getLogger(SearchManagerTest.class);
	
	@Autowired
	private SearchManager searchManager;
	
	public void testReadOrganizationSearchMetamodel() {
		logger.debug("Injected Search manager : " + searchManager.getClass().getName());
	}
}
