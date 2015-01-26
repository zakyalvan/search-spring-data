package com.innovez.core.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Override the selection of fields on searchable type. We could use this
 * annotation on {@link Searchable} type level or field level. Please make sure
 * you only annotating field with {@link Searchable} annotated type, not a
 * simple basic type property.
 * 
 * @author zakyalvan
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FieldOverrides {
	/**
	 * Array of {@link FieldOverride} definitions.
	 * 
	 * @return
	 */
	FieldOverride[] value();
}