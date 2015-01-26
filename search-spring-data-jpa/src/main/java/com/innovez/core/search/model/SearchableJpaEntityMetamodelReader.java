package com.innovez.core.search.model;

import java.io.Serializable;
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
import org.springframework.core.Ordered;
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
public class SearchableJpaEntityMetamodelReader implements SearchableMetamodelReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchableJpaEntityMetamodelReader.class);
	
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
		Assert.notNull(target, "Search target type parameter should not be null");
		Assert.isTrue(support(target), "Search target type parameter not supported by this search-metamodel-reader.");
		
		Searchable searchable = AnnotationUtils.findAnnotation(target, Searchable.class);
		String defaulFieldName = searchable.defaultField();
		
		Map<String, SearchableFieldMetamodel> searchableFieldMetamodels = readFields(target);
		
		SearchableJpaEntityMetamodel searchableMetamodel = new SearchableJpaEntityMetamodel(target, searchableFieldMetamodels);
		if(StringUtils.hasText(defaulFieldName) && searchableMetamodel.hasSearchableField(defaulFieldName)) {
			SearchableFieldMetamodel defaultField = searchableMetamodel.getSearchableField(defaulFieldName);
			searchableMetamodel.setDefaultField(defaultField);
		}
		return searchableMetamodel;
	}

	/**
	 * Read metadatas of all searchable-field on {@link Searchable} target type.
	 * 
	 * @param target
	 * @return
	 * @throws Exception
	 */
	private <T> Map<String, SearchableFieldMetamodel> readFields(Class<T> target) throws Exception {		
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

	/**
	 * Read metadata of all (selected) given includeField parameter on any searchable target.
	 * 
	 * @param target
	 * @param includeFields
	 * @return
	 * @throws Exception
	 */
	private <T> Map<String, SearchableFieldMetamodel> readFields(Class<T> target, Map<String, SearchableFieldInfo> includeFields) throws Exception {
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
						LOGGER.debug("***************** Trying to read relation metadatas with type {}", fieldType.getName());
						if(support(fieldType)) {
							LOGGER.debug("***************** Can read relation metadatas with type : {}", fieldType.getName());
							FieldOverrides referenceFieldOverrides = AnnotationUtils.getAnnotation(field, FieldOverrides.class);
							if (referenceFieldOverrides != null) {
								Map<String, SearchableFieldInfo> includeReferenceFields = new HashMap<String, SearchableFieldInfo>();
								for (FieldOverride referenceFieldOverride : referenceFieldOverrides.value()) {
									LOGGER.debug("***************** Relation with name : {} : include field {}", fieldName, referenceFieldOverride.name());
									SearchableFieldInfo searchableReferenceFieldInfo = new SearchableFieldInfo(
											fieldType,
											referenceFieldOverride.name(), 
											referenceFieldOverride.field().label(), 
											referenceFieldOverride.field().order());
									includeReferenceFields.put(referenceFieldOverride.name(), searchableReferenceFieldInfo);
								}
								LOGGER.debug("***************** Include references : {}", includeReferenceFields.toString());
								Map<String, SearchableFieldMetamodel> searchableReferenceFieldMetamodels = readFields(fieldType, includeReferenceFields);
								LOGGER.debug("***************** {}", searchableReferenceFieldMetamodels);
								CompositeSearchableFieldMetamodel compositeReferenceFieldMetamodel = new CompositeSearchableJpaEntityFieldMetamodel(target, fieldName, fieldType, fieldLabel, fieldOrder, searchableReferenceFieldMetamodels);
								LOGGER.debug("***************** {}", compositeReferenceFieldMetamodel.toString());
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
	
	/**
	 * Just a convenience data transfer object for {@link SearchableField} annotation attributes.
	 * 
	 * @author zakyalvan
	 */
	@SuppressWarnings("serial")
	public static class SearchableFieldInfo implements SearchableTypeContainer, Serializable {
		private final Class<?> searchableType;
		private final String searchableTypeName;
		private final String name;
		private final String label;
		private final Integer order;
			
		public SearchableFieldInfo(Class<?> searchableType, String name, String label, Integer order) {
			Assert.notNull(searchableType);
			Assert.hasLength(name);
			Assert.hasLength(label);
			
			this.searchableType = searchableType;
			this.searchableTypeName = searchableType.getName();
			this.name = name;
			this.label = label;
			this.order = order != null ? order : Ordered.HIGHEST_PRECEDENCE;
		}

		@Override
		public Class<?> getSearchableType() {
			return searchableType;
		}

		public String getName() {
			return name;
		}

		public String getLabel() {
			return label;
		}

		public Integer getOrder() {
			return order;
		}

		@Override
		public String toString() {
			return "SearchableFieldInfo [searchableTypeName=" + searchableTypeName
					+ ", name=" + name + ", label=" + label + ", order=" + order
					+ "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime
					* result
					+ ((searchableTypeName == null) ? 0 : searchableTypeName
							.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SearchableFieldInfo other = (SearchableFieldInfo) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (searchableTypeName == null) {
				if (other.searchableTypeName != null)
					return false;
			} else if (!searchableTypeName.equals(other.searchableTypeName))
				return false;
			return true;
		}
	}
}
