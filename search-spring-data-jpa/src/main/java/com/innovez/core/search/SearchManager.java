package com.innovez.core.search;

import java.util.Map;

import com.innovez.core.search.model.SearchableMetamodel;

/**
 * Base contract for search manager.
 * 
 * @author zakyalvan
 */
public interface SearchManager {
	/**
	 * Retrieve search metamodel.
	 * 
	 * @param target
	 * @return
	 */
	SearchableMetamodel getSearchMetamodel(Class<?> target);
	
	/**
	 * Ask whether given target type has searchable metamodel.
	 * 
	 * @param target
	 */
	boolean hasSearchMetamodel(Class<?> target);
	
	/**
	 * Ask whether given search parameter is valid search parameters for given target type.
	 * 
	 * @param target
	 * @param searchParameters
	 * @return
	 */
	boolean isValidSearchParameters(Class<?> target, Map<String, Object> searchParameters);
}
