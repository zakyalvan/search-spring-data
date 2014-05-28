package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark for method argument.
 * 
 * @author zakyalvan
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchParams {
	/**
	 * Target type of search parameter's target.
	 * 
	 * @return
	 */
	Class<?> target();
	
	/**
	 * Validate or not validate the search parameter given.
	 * 
	 * @return
	 */
	boolean validate() default true;
}
