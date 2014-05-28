package com.innovez.core.entity.support.search.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Marking annotation to enable spring data jpa search features.
 * 
 * @author zakyalvan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SearchRelatedBeanDefinitionRegistrar.class)
public @interface EnableSpringDataJpaSearch {
	/**
	 * Persistence mechanism used in backgroud by spring data.
	 * 
	 * @return
	 */
	PersistenceMechanism mechanism() default PersistenceMechanism.JPA;
}
