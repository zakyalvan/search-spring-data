package com.innovez.core.entity.support.search.model;

import java.util.Collection;

/**
 * Searchable meta-model.
 * 
 * @author zakyalvan
 */
public interface SearchableMetamodel extends SearchableTypeContainer {
	/**
	 * Retrieve all search fields.
	 * 
	 * @return
	 */
	Collection<SearchableFieldMetamodel> getSearchFields();
	
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
	SearchableFieldMetamodel getSearchField(String fieldName);
}
