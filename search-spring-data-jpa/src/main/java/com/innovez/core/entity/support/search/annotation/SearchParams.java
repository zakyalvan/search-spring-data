package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Annotate any method argument with {@link Map} type to be evaluated as search
 * parameter for given target type.
 * 
 * @author zakyalvan
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
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
