package com.innovez.core.entity.support.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
	 * Name of search-able field. If this annotation applied to field, this attribute will be ignored.
	 * 
	 * @return
	 */
	String name() default "";
	
	/**
	 * Label code of search-able field. On runtime, this label code should be resolvable against MessageSource.
	 * 
	 * @return
	 */
	String label() default "";
	
	/**
	 * Order of search field, could be used for properly arranging the fields.
	 * 
	 * @return
	 */
	int order() default 0;
}