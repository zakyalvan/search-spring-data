package com.innovez.core.search.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.innovez.core.search.SpringDataJpaSearchManager;
import com.innovez.core.search.aspects.SearchParamsMethodArgumentAdvisor;
import com.innovez.core.search.model.SearchableJpaEntityMetamodelReader;

/**
 * Search feature related import bean registrar. Used for manually registering bean required for search feature.
 * This type implicitly imported when you annotate you configuration class with {@link EnableSpringDataJpaSearch}
 * 
 * @author zakyalvan
 */
public class SearchRelatedBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchRelatedBeanDefinitionRegistrar.class);
	
	/**
	 * Register beans for search related feature. We use this because on future we need to extends this project to support 
	 * other storage mechanism supported by spring data project.
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableSpringDataJpaSearch.class.getName());
		PersistenceMechanism persistenceMechanism =  (PersistenceMechanism) annotationAttributes.get("mechanism");
		
		if(persistenceMechanism == PersistenceMechanism.JPA) {
			LOGGER.debug("Using jpa persistence mechanism, then registering jpa related search bean.");
			
			/**
			 * Register search manager.
			 */
			BeanDefinition searchManager = BeanDefinitionBuilder.rootBeanDefinition(SpringDataJpaSearchManager.class)
					.getBeanDefinition();
			registry.registerBeanDefinition("jpaSearchManager", searchManager);
			
			/**
			 * Register search meta-model reader.
			 */
			BeanDefinition searchMetamodelReader = BeanDefinitionBuilder.rootBeanDefinition(SearchableJpaEntityMetamodelReader.class)
					.getBeanDefinition();
			registry.registerBeanDefinition("defaultSearchMetamodelReader", searchMetamodelReader);
			
			/**
			 * Register search-param annotated argument method advisor.
			 */
			BeanDefinition searchParamsMethodArgumentAdvisor = BeanDefinitionBuilder.rootBeanDefinition(SearchParamsMethodArgumentAdvisor.class)
					.setFactoryMethod("aspectOf")
					.getBeanDefinition();
			registry.registerBeanDefinition("searchParamsMethodArgumentAdvisor", searchParamsMethodArgumentAdvisor);
			
			/**
			 * Register cache manager abstraction, if caching enabled.
			 */
		}
	}
}