package com.innovez.core.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.innovez.core.search.model.SearchableMetamodel;

/**
 * Marking for search-able entity. By marking an entity using this annotation,
 * automatically a {@link SearchableMetamodel} would be created and cached for
 * subsequent usage.
 * 
 * @author zakyalvan
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Searchable {
	/**
	 * Define default search-able field. If blank value (as default) given, that
	 * means no default attribute. Non resolvable field name means no default field.
	 * 
	 * @return
	 */
	String defaultField() default "";
}