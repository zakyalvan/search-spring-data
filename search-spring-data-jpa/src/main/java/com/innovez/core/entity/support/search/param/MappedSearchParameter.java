package com.innovez.core.entity.support.search.param;

import java.util.Map;

import com.innovez.core.entity.support.search.model.SearchableTypeContainer;

/**
 * Mapped search parameter.
 * 
 * @author zakyalvan
 */
public interface MappedSearchParameter extends SearchableTypeContainer {
	Map<String, Object> getParameters();
}
