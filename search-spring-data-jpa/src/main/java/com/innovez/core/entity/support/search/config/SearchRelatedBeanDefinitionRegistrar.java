package com.innovez.core.entity.support.search.config;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.innovez.core.entity.support.search.SpringDataJpaSearchManager;
import com.innovez.core.entity.support.search.aspects.SearchParamsMethodArgumentAdvisor;
import com.innovez.core.entity.support.search.model.SearchableAnnotatedJpaEntityMetamodelReader;

/**
 * Search feature related import bean registrar. Used for manually registering bean required for search feature.
 * This type implicitly imported when you annotate you configuration class with {@link EnableSpringDataJpaSearch}
 * 
 * @author zakyalvan
 */
public class SearchRelatedBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	private Logger logger = Logger.getLogger(SearchRelatedBeanDefinitionRegistrar.class);
	
	/**
	 * Register beans for search related feature. We use this because on future we need to extends this project to support 
	 * other storage mechanism supported by spring data project.
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableSpringDataJpaSearch.class.getName());
		PersistenceMechanism persistenceMechanism =  (PersistenceMechanism) annotationAttributes.get("mechanism");
		
		if(persistenceMechanism == PersistenceMechanism.JPA) {
			logger.debug("Using jpa persistence mechanism, then registering jpa related search bean.");
			
			/**
			 * Register search manager.
			 */
			BeanDefinition searchManager = BeanDefinitionBuilder.rootBeanDefinition(SpringDataJpaSearchManager.class)
					.getBeanDefinition();
			registry.registerBeanDefinition("jpaSearchManager", searchManager);
			
			/**
			 * Register search meta-model reader.
			 */
			BeanDefinition searchMetamodelReader = BeanDefinitionBuilder.rootBeanDefinition(SearchableAnnotatedJpaEntityMetamodelReader.class)
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
			 * Register cache manager abstraction.
			 */
		}
	}
}