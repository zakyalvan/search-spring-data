package com.innovez.core.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Search specifications descriptor, hint for {@link SearchParams}. If no
 * {@link SearchSpecification} declared for specific search parameter, default
 * specification will be used.
 * 
 * @author zakyalvan
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchSpecifications {
	/**
	 * List of search specification to be used to .
	 * 
	 * @return
	 */
	SearchSpecification[] value() default {};

	Group[] groups() default {};

	/**
	 * Grouping of search specification.
	 * 
	 * @author zakyalvan
	 */
	public static @interface Group {
		SearchSpecification[] value();

		GroupType type() default GroupType.AND;
	}

	public static enum GroupType {
		OR, AND
	}
}