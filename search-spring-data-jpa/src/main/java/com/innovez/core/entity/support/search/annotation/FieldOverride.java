package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FieldOverride {
	/**
	 * Search-able field name to override.
	 * 
	 * @return
	 */
	String name();
	
	/**
	 * Search-able field override definition.
	 * 
	 * @return
	 */
	SearchableField field();
}
