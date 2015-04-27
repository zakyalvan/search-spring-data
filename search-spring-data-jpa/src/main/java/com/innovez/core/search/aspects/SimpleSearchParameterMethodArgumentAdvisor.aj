package com.innovez.core.search.aspects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.innovez.core.search.SearchManager;
import com.innovez.core.search.SearchOperationException;
import com.innovez.core.search.model.SearchableMetamodel;
import com.innovez.core.search.param.SimpleSearchParameter;

/**
 * Advising methods containing {@link SimpleSearchParameter} type argument.
 * 
 * @author zakyalvan
 */
public aspect SimpleSearchParameterMethodArgumentAdvisor {
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSearchParameterMethodArgumentAdvisor.class);
	
	@Autowired
	private SearchManager searchManager;
	
	/**
	 * Pointcut on execution of method with {@link SimpleSearchParameter} argument.
	 * 
	 * @param searchParameter
	 */
	pointcut executionOfMethodsContainingSimpleSearchParameterArgument(SimpleSearchParameter searchParameter) : execution(* *.*(.., SimpleSearchParameter+, ..)) && args(searchParameter);
	
	/**
	 * Arround advice on {@link #executionOfMethodsContainingSimpleSearchParameterArgument(SimpleSearchParameter)}.
	 * 
	 * @param searchParameter
	 * @return
	 */
	Object around(SimpleSearchParameter searchParameter) : executionOfMethodsContainingSimpleSearchParameterArgument(searchParameter) {
		if(searchParameter == null) {
			LOGGER.error("Given search parameter is null, proceed normal execution.");
			return proceed(searchParameter);
		}
		
		LOGGER.debug("Retrieve metamodel of searchable type.");
		SearchableMetamodel metamodel = searchManager.getSearchMetamodel(searchParameter.getSearchableType());
		
		LOGGER.debug("Validate search parameter name, whether the field is searchable field of type : " + searchParameter.getSearchableType().getName());
		if(!metamodel.hasSearchableField(searchParameter.getName())) {
			LOGGER.error("Given search parameter name " + searchParameter.getName() + " is not valid searchable field of type : " + searchParameter.getSearchableType().getName());
			throw new SearchOperationException("Given search parameter name " + searchParameter.getName() + " is not valid searchable field of type : " + searchParameter.getSearchableType().getName());
		}
		
		
		
		return proceed(searchParameter);
	}
}
