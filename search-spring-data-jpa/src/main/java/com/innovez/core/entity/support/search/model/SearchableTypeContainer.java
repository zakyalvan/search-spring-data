package com.innovez.core.entity.support.search.model;

/**
 * Contract for class which carry type information of searchable type.
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
