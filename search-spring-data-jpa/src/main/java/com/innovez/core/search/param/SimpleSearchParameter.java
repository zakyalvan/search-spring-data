package com.innovez.core.search.param;

import com.innovez.core.search.model.SearchableTypeContainer;

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
