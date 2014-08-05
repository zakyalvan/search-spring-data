package com.innovez.core.search.param;

import java.util.Map;

import com.innovez.core.search.model.SearchableTypeContainer;

/**
 * Mapped search parameter.
 * 
 * @author zakyalvan
 */
public interface MappedSearchParameter extends SearchableTypeContainer {
	Map<String, Object> getParameters();
}
