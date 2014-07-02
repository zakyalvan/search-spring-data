package com.innovez.core.entity.support.search;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;

import com.innovez.core.entity.support.search.model.SearchMetamodelReader;
import com.innovez.core.entity.support.search.model.SearchableMetamodel;

/**
 * Default implementation of {@link SearchManager}.
 * 
 * @author zakyalvan
 */
@Qualifier("default")
public class SpringDataJpaSearchManager implements SearchManager {
	private Logger logger = Logger.getLogger(SpringDataJpaSearchManager.class);
	
	@Autowired
	private List<SearchMetamodelReader> metamodelReaders;
	
	@Override
	@Cacheable(value="searchMetamodels", key="#target.name")
	public SearchableMetamodel getSearchMetamodel(Class<?> target) {
		Assert.notNull(target, "Target type parameter should not be null");
		Assert.isTrue(hasSearchMetamodel(target), "Given target type parameter " + target.getName() + " is not searchable type.");
		
		logger.debug("Looking for appropriate delegatable search-metamodel-reader for type : " + target.getName());
		SearchMetamodelReader searchMetamodelReader = getSearchMetamodelReader(target);
		if(searchMetamodelReader == null) {
			logger.error("No search-metamodel-reader found for type : " + target.getName());
			throw new IllegalStateException("No search-metamodel-reader found for type : " + target.getName());
		}
		
		try {
			SearchableMetamodel searchableMetamodel = searchMetamodelReader.read(target);
			logger.debug("Search metamodel for target type : " + target.getName() + " is : " + searchableMetamodel);
			return searchableMetamodel;
		}
		catch(Exception exception) {
			exception.printStackTrace();
			throw new SearchOperationException("Can't read metadata on target type : " + target.getName() + ". See cause stack traces.", exception);
		}
	}

	@Override
	public boolean hasSearchMetamodel(Class<?> target) {
		logger.debug("Determine whether given target type ( " + target.getName() + " ) is searchable type.");
		return getSearchMetamodelReader(target) != null;
	}

	@Override
	public boolean isValidSearchParameters(Class<?> target, Map<String, Object> searchParameters) {
		Assert.notNull(target);
		Assert.notNull(searchParameters);
		
		logger.debug("Validate search parameters for target type : " + target.getName());
		SearchableMetamodel searchableMetamodel = getSearchMetamodel(target);
		
		boolean valid = true;
		for(String keyField : searchParameters.keySet()) {
			if(searchableMetamodel.hasSearchableField(keyField)) {
				logger.debug("Search field '" + keyField + "' is valid search field for target type : " + target.getName());
				continue;
			}
			logger.error("Search field '" + keyField + "' is invalid search field for target type : " + target.getName());
			valid = false;
		}
		logger.debug("Over all, given search parameters : " + searchParameters + " contains " + (valid ? "valid" : "invalid") + " search fields for target type : " + target.getName());
		return valid;
	}
	
	@Cacheable(value="searchMetamodelReader", key="#target.name", condition="#result != null")
	private SearchMetamodelReader getSearchMetamodelReader(Class<?> target) {
		logger.debug("Retrieve search-metamodel-reader for target type : " + target.getName());
		SearchMetamodelReader searchMetamodelReader = null;
		for(SearchMetamodelReader metamodelReader : metamodelReaders) {
			if(metamodelReader.support(target)) {
				if(searchMetamodelReader != null) {
					logger.error("Found multiple search-metamodel-reader for type : " + target.getName());
					throw new IllegalStateException("Found multiple search-metamodel-reader for type : " + target.getName());
				}
				searchMetamodelReader = metamodelReader;
			}
		}
		return searchMetamodelReader;
	}
}
