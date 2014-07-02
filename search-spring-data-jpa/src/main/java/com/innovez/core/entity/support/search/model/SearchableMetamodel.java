package com.innovez.core.entity.support.search.model;

import java.util.Collection;

/**
 * Searchable meta-model.
 * 
 * @author zakyalvan
 */
public interface SearchableMetamodel extends SearchableTypeContainer {
	/**
	 * Retrieve all searchable fields.
	 * 
	 * @return
	 */
	Collection<SearchableFieldMetamodel> getSearchableFields();
	
	/**
	 * Ask whether searchable metamodel contains given search fields.
	 * 
	 * @param fieldName
	 * @return
	 */
	boolean hasSearchableField(String fieldName);
	
	/**
	 * Retrieve search field differentiated by field name.
	 * 
	 * @param fieldName
	 * @return
	 */
	SearchableFieldMetamodel getSearchableField(String fieldName);
}
