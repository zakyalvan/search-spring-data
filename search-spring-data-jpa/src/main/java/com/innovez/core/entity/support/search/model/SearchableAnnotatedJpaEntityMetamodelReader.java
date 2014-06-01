package com.innovez.core.entity.support.search.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;

import org.apache.log4j.Logger;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.innovez.core.entity.support.search.annotation.SearchableField;
import com.innovez.core.entity.support.search.annotation.Searchable;

/**
 * Metamodel reader for {@link Searchable} annotated entity type.
 * 
 * @author zakyalvan
 */
public class SearchableAnnotatedJpaEntityMetamodelReader implements SearchMetamodelReader {
	private Logger logger = Logger.getLogger(SearchableAnnotatedJpaEntityMetamodelReader.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public <T> boolean support(Class<T> target) {
		Assert.notNull(target);
		
		Searchable searchable = AnnotationUtils.findAnnotation(target, Searchable.class);
		Entity entity = AnnotationUtils.findAnnotation(target, Entity.class);
		return searchable != null && entity != null;
	}

	@Override
	public <T> SearchableMetamodel read(Class<T> target) throws Exception {
		Assert.isTrue(support(target), "Target type parameter not supported by this search-metamodel-reader.");
		
		/**
		 * Searchable fields on target entity.
		 */	
		Map<String, SearchableFieldMetamodel> searchFieldMetamodels = new HashMap<String, SearchableFieldMetamodel>();
		
		EntityType<T> entityType = entityManager.getMetamodel().entity(target);
		for(Attribute<? super T, ?> attribute : entityType.getAttributes()) {
			Field field = target.getDeclaredField(attribute.getName());

			if(field != null) {
				SearchableField searchField = AnnotationUtils.getAnnotation(field, SearchableField.class);
				if(searchField != null) {
					PersistentAttributeType persistentAttributeType = attribute.getPersistentAttributeType();
					logger.debug("Found SearchField property (" + attribute.getClass().getName() + ") on " + target.getName() + ", with name : " + attribute.getName() + " and PersistenceAttributeType : " + persistentAttributeType);
					
					/**
					 * Process for basic type.
					 */
					if(persistentAttributeType == PersistentAttributeType.BASIC) {
						String name = attribute.getName();
						String label = StringUtils.hasText(searchField.label()) ? searchField.label() : target.getSimpleName() + "." + name + "." + "label";
						Class<?> type = attribute.getJavaType();
						int order = searchField.order();
						
						SearchableJpaEntityFieldMetamodel searchFieldMetamodel = new SearchableJpaEntityFieldMetamodel(target, name, type, label, order);
						searchFieldMetamodels.put(searchFieldMetamodel.getName(), searchFieldMetamodel);
					}
					/**
					 * Process for many-to-one type.
					 */
					else if(persistentAttributeType == PersistentAttributeType.MANY_TO_ONE) {
						/**
						 * TODO How to remove cyclic read?!
						 */
						//SearchableMetamodel searchableMetamodel = read(attribute.getJavaType());
						
					}
					/**
					 * Process many-to-many relations.
					 */
					else if(persistentAttributeType == PersistentAttributeType.ONE_TO_MANY) {
						
					}
				}
			}
		}
		
		return new SearchableJpaEntityMetamodel(target, searchFieldMetamodels);
	}

	@Override
	public <T> SearchableMetamodel read(Class<T> target, String... excludeFields) throws Exception {
		return null;
	}
}
