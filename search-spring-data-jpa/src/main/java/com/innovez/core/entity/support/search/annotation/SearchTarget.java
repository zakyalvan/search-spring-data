package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Give a hint for searchable type.
 * 
 * @author zakyalvan
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SearchTarget {
	/**
	 * Type of search target.
	 * 
	 * @return
	 */
	Class<?> value();
}
