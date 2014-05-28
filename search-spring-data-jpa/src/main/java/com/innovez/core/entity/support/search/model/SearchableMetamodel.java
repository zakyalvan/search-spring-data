package com.innovez.core.entity.support.search.model;

import java.util.Collection;

/**
 * Searchable meta-model.
 * 
 * @author zakyalvan
 */
public interface SearchableMetamodel {
	/**
	 * Retrieve actual type of searchable type.
	 * 
	 * @return
	 */
	Class<?> getSearchableType();
	
	/**
	 * Retrieve all search fields.
	 * 
	 * @return
	 */
	Collection<SearchFieldMetamodel> getSearchFields();
	
	/**
	 * Ask whether search metamodel contains given search fields.
	 * 
	 * @param fieldName
	 * @return
	 */
	boolean hasSearchField(String fieldName);
	
	/**
	 * Retrieve search field differentiated by field name.
	 * 
	 * @param fieldName
	 * @return
	 */
	SearchFieldMetamodel getSearchField(String fieldName);
}
