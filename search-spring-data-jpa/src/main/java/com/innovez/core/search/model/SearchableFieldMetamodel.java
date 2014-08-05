package com.innovez.core.search.model;

import org.springframework.core.Ordered;

/**
 * Metamodel for search field.
 * 
 * @author zakyalvan
 */
public interface SearchableFieldMetamodel extends SearchableTypeContainer, Ordered {
	/**
	 * Retrieve name of searchable field.
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * Retrieve type of searchable field.
	 * 
	 * @return
	 */
	Class<?> getFieldType();
	
	/**
	 * Retrieve code of label or label for searchable field. 
	 * 
	 * @return
	 */
	String getLabel();
}