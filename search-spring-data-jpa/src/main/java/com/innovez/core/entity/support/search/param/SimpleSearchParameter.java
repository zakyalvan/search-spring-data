package com.innovez.core.entity.support.search.param;

import com.innovez.core.entity.support.search.model.SearchableTypeContainer;

/**
 * Simple search form contract.
 * 
 * @author zakyalvan
 */
public interface SimpleSearchParameter extends SearchableTypeContainer {
	/**
	 * Retrieve parameter name.
	 * 
	 * @return
	 */
	String getName();
	
	/**
	 * Retrieve parameter value.
	 * 
	 * @return
	 */
	Object getValue();
}
