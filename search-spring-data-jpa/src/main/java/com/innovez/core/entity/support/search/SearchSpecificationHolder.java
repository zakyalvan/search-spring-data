package com.innovez.core.entity.support.search;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

/**
 * Specification holder, this interface tightly bound or specific to spring-data-jpa project.
 * 
 * @author zakyalvan
 *
 * @param <T>
 */
public interface SearchSpecificationHolder<T> extends SearchParameterHolder {
	/**
	 * Single final specification.
	 */
	public static final String FINAL_SPECIFICATION = "single-final-specification";
	
	/**
	 * Retrieve all specifications, keyed by field name.
	 * 
	 * @return
	 */
	Map<String, Specification<T>> getSpecifications();
}