package com.innovez.core.search;

import java.util.Map;

/**
 * Contract for type which hold search parameters.
 * 
 * @author zakyalvan
 */
public interface SearchParameterHolder {
	/**
	 * Retrieve search target type.
	 * 
	 * @return
	 */
	Class<?> getSearchTarget();
	
	/**
	 * Retrieve parameters.
	 * 
	 * @return
	 */
	Map<String, Object> getParameters();
}