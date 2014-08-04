package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.MessageSource;

/**
 * Marking search-able field of an entity.
 * 
 * @author zakyalvan
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SearchableField {
	/**
	 * Name of search-able field. If this annotation applied to basic fields, this attribute will be ignored.
	 * 
	 * @return
	 */
	String name() default "";
	
	/**
	 * Label code of search-able field. On runtime, this label code should be resolvable against {@link MessageSource}.
	 * 
	 * @return
	 */
	String label() default "";
	
	/**
	 * Order of search field, could be used for arranging the fields properly.
	 * 
	 * @return
	 */
	int order() default Integer.MAX_VALUE;
}