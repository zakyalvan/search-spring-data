package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.innovez.core.entity.support.search.model.SearchableMetamodel;

/**
 * Marking for search-able entity. By marking an entity using this annotation, automatically
 * a {@link SearchableMetamodel} would be created and cached for subsequent usage.
 * 
 * @author zakyalvan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Searchable {
	/**
	 * Searchable fields declaration.
	 * 
	 * @return
	 */
	SearchField[] fields() default {};
}