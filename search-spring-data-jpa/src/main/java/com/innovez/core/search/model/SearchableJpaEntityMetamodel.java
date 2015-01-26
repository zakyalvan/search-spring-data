package com.innovez.core.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

/**
 * Default implementation of {@link SearchableMetamodel} type.
 * 
 * @author zakyalvan
 */
@SuppressWarnings("serial")
public final class SearchableJpaEntityMetamodel implements SearchableMetamodel, Serializable {
	private final Class<?> searchableType;
	private final String searchableTypeName;
	private final Map<String, SearchableFieldMetamodel> searchableFields;
	
	private SearchableFieldMetamodel defaultField;
	
	public SearchableJpaEntityMetamodel(Class<?> searchableType, Map<String, SearchableFieldMetamodel> searchableFields) {
		Assert.notNull(searchableType);
		Assert.notNull(searchableFields);
		
		this.searchableType = searchableType;
		this.searchableTypeName = searchableType.getName();
		this.searchableFields = searchableFields;
	}

	@Override
	public Class<?> getSearchableType() {
		return searchableType;
	}

	@Override
	public Collection<SearchableFieldMetamodel> getSearchableFields() {
		List<SearchableFieldMetamodel> modelList = new ArrayList<SearchableFieldMetamodel>(searchableFields.values());
		Collections.sort(modelList, AnnotationAwareOrderComparator.INSTANCE);
		return modelList;
	}

	@Override
	public SearchableFieldMetamodel getDefaultField() {
		return defaultField;
	}
	public void setDefaultField(SearchableFieldMetamodel defaultField) {
		Assert.notNull(defaultField);
		this.defaultField = defaultField;
	}

	@Override
	public boolean hasSearchableField(String fieldName) {
		Assert.hasLength(fieldName, "Field name parameter should not be null or empty string");
		String[] splitedFieldNames = fieldName.split("\\.", 2);
		
		if(splitedFieldNames.length == 1) {
			return searchableFields.containsKey(splitedFieldNames[0]);
		}
		else {
			return searchableFields.containsKey(splitedFieldNames[0]) && ((CompositeSearchableFieldMetamodel) searchableFields.get(splitedFieldNames[0])).hasSearchableField(splitedFieldNames[1]);
		}
	}

	@Override
	public SearchableFieldMetamodel getSearchableField(String fieldName) {
		Assert.isTrue(hasSearchableField(fieldName), "Given fieldName parameter : '" + fieldName + "' is not found as searchable field.");
		String[] splitedFieldNames = fieldName.split("\\.", 2);
		
		if(splitedFieldNames.length == 1) {
			return searchableFields.get(splitedFieldNames[0]);
		}
		else {
			return ((CompositeSearchableFieldMetamodel) searchableFields.get(splitedFieldNames[0])).getSearchableField(splitedFieldNames[1]);
		}
	}

	@Override
	public String toString() {
		return "SearchableEntityMetamodel [searchableType=" + searchableType
				+ ", searchFields=" + searchableFields + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((searchableFields == null) ? 0 : searchableFields.hashCode());
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
		SearchableJpaEntityMetamodel other = (SearchableJpaEntityMetamodel) obj;
		if (searchableFields == null) {
			if (other.searchableFields != null)
				return false;
		} else if (!searchableFields.equals(other.searchableFields))
			return false;
		if (searchableTypeName == null) {
			if (other.searchableTypeName != null)
				return false;
		} else if (!searchableTypeName.equals(other.searchableTypeName))
			return false;
		return true;
	}
}
