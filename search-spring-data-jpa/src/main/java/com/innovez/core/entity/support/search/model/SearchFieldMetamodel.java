package com.innovez.core.entity.support.search.model;

/**
 * Metamodel for search field.
 * 
 * @author zakyalvan
 */
public interface SearchFieldMetamodel {
	/**
	 * Retrieve appropriate containing type of this field meta-model.
	 * 
	 * @return
	 */
	Class<?> getSearchableType();
	
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