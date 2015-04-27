package com.innovez.core.search.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.innovez.core.search.annotation.FieldOverride;
import com.innovez.core.search.annotation.FieldOverrides;
import com.innovez.core.search.annotation.Searchable;
import com.innovez.core.search.annotation.SearchableField;

/**
 * Metamodel reader for {@link Searchable} annotated entity type.
 * 
 * @author zakyalvan
 */
public class SearchableJpaEntityMetamodelReader implements SearchMetamodelReader {
	private Logger logger = LoggerFactory.getLogger(SearchableJpaEntityMetamodelReader.class);
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public <T> boolean support(Class<T> target) {
		Assert.notNull(target, "Search target type parameter should not be empty");
		Searchable searchable = AnnotationUtils.findAnnotation(target, Searchable.class);
		Entity entity = AnnotationUtils.findAnnotation(target, Entity.class);
		return searchable != null && entity != null;
	}

	@Override
	public <T> SearchableMetamodel read(Class<T> target) throws Exception {
		Assert.isTrue(support(target), "Search target type parameter not supported by this search-metamodel-reader.");
		Map<String, SearchableFieldMetamodel> searchableFieldMetamodels = readFields(target);
		logger.debug(searchableFieldMetamodels.toString());
		return new SearchableJpaEntityMetamodel(target, searchableFieldMetamodels);
	}

	@Override
	public <T> SearchableMetamodel read(Class<T> target, String... excludeFields) throws Exception {
		return null;
	}

	@Override
	public <T> Map<String, SearchableFieldMetamodel> readFields(Class<T> target) throws Exception {
		Assert.isTrue(support(target), "Target type parameter not supported by this search-metamodel-reader.");
		
		EntityType<T> entityType = entityManager.getMetamodel().entity(target);
		Map<String, SearchableFieldInfo> includeFields = new HashMap<String, SearchableFieldInfo>();
		for (Attribute<? super T, ?> attribute : entityType.getAttributes()) {
			Field field = target.getDeclaredField(attribute.getName());

			if(field != null) {
				SearchableField searchableField = AnnotationUtils.getAnnotation(field, SearchableField.class);
				if (searchableField != null) {
					String fieldName = attribute.getName();
					String fieldLabel = StringUtils.hasText(searchableField.label()) ? searchableField.label() : target.getSimpleName() + "." + fieldName + "." + "label";
					int fieldOrder = searchableField.order();
					
					includeFields.put(fieldName, new SearchableFieldInfo(target, fieldName, fieldLabel, fieldOrder));
				}
			}
		}
		return readFields(target, includeFields);
	}

	@Override
	public <T> Map<String, SearchableFieldMetamodel> readFields(Class<T> target, Map<String, SearchableFieldInfo> includeFields) throws Exception {
		Assert.isTrue(support(target), "Target type parameter not supported by this search-metamodel-reader.");
		
		Map<String, SearchableFieldMetamodel> searchableFieldMetamodels = new HashMap<String, SearchableFieldMetamodel>();
		EntityType<T> entityType = entityManager.getMetamodel().entity(target);
		
		for(Attribute<? super T, ?> attribute : entityType.getAttributes()) {
			if(includeFields.containsKey(attribute.getName())) {
				Field field = target.getDeclaredField(attribute.getName());
	
				if(field != null) {
					SearchableFieldInfo searchableField = includeFields.get(attribute.getName());
					PersistentAttributeType persistentAttributeType = attribute.getPersistentAttributeType();

					String fieldName = attribute.getName();
					Class<?> fieldType = attribute.getJavaType();
					String fieldLabel = StringUtils.hasText(searchableField.getLabel()) ? searchableField.getLabel() : target.getSimpleName() + "." + fieldName + "." + "label";
					int fieldOrder = searchableField.getOrder();

					if (persistentAttributeType == PersistentAttributeType.BASIC) {
						BasicSearchableFieldMetamodel basicSearchableFieldMetamodel = new BasicSearchableJpaEntityFieldMetamodel(target, fieldName, fieldType, fieldLabel, fieldOrder);
						searchableFieldMetamodels.put(basicSearchableFieldMetamodel.getName(), basicSearchableFieldMetamodel);
					}
					else if (persistentAttributeType == PersistentAttributeType.MANY_TO_ONE || persistentAttributeType == PersistentAttributeType.ONE_TO_MANY || persistentAttributeType == PersistentAttributeType.ONE_TO_ONE) {
						if(support(fieldType)) {
							FieldOverrides referenceFieldOverrides = AnnotationUtils.getAnnotation(field, FieldOverrides.class);
							if (referenceFieldOverrides != null) {
								Map<String, SearchableFieldInfo> includeReferenceFields = new HashMap<String, SearchableFieldInfo>();
								for (FieldOverride referenceFieldOverride : referenceFieldOverrides.value()) {
									SearchableFieldInfo searchableReferenceFieldInfo = new SearchableFieldInfo(
											fieldType,
											referenceFieldOverride.name(), 
											referenceFieldOverride.field().label(), 
											referenceFieldOverride.field().order());
									includeReferenceFields.put(referenceFieldOverride.name(), searchableReferenceFieldInfo);
								}
								Map<String, SearchableFieldMetamodel> searchableReferenceFieldMetamodels = readFields(fieldType, includeReferenceFields);
								CompositeSearchableFieldMetamodel compositeReferenceFieldMetamodel = new CompositeSearchableJpaEntityFieldMetamodel(target, fieldName, fieldType, fieldLabel, fieldOrder, searchableReferenceFieldMetamodels);
								searchableFieldMetamodels.put(compositeReferenceFieldMetamodel.getName(), compositeReferenceFieldMetamodel);
							}
							else {
								throw new IllegalStateException("Currently, every  not-basic searchable fields type (many-to-one, one-to-many or one-to-one relation) should be annotated with @FieldOverrides. Also please manually make sure no cyclic metadata read occured.");
							}
						}
					}
					else {
						throw new IllegalStateException(String.format("Attribute with persistent attribut type %s not supported yet", persistentAttributeType.toString()));
					}
				}
			}
		}
		return searchableFieldMetamodels;
	}
}
