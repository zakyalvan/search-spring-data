package com.innovez.core.search.model;

/**
 * Contract for class which carry type information of search-able type.
 * 
 * @author zakyalvan
 */
public interface SearchableTypeContainer {
	/**
	 * Retrieve containing searchable type.
	 * 
	 * @return
	 */
	Class<?> getSearchableType();
}
