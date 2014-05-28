package com.innovez.core.entity.support.search.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

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
	private final Map<String, SearchableFieldMetamodel> searchFields;
	
	public SearchableJpaEntityMetamodel(Class<?> searchableType, Map<String, SearchableFieldMetamodel> searchFields) {
		Assert.notNull(searchableType);
		Assert.notNull(searchFields);
		
		this.searchableType = searchableType;
		this.searchableTypeName = searchableType.getName();
		this.searchFields = searchFields;
	}

	@Override
	public Class<?> getSearchableType() {
		return searchableType;
	}

	@Override
	public Collection<SearchableFieldMetamodel> getSearchFields() {
		return searchFields.values();
	}

	@Override
	public boolean hasSearchField(String fieldName) {
		return searchFields.containsKey(fieldName);
	}

	@Override
	public SearchableFieldMetamodel getSearchField(String fieldName) {
		Assert.isTrue(hasSearchField(fieldName), "Given fieldName parameter is not found as searchable field.");
		return searchFields.get(fieldName);
	}

	@Override
	public String toString() {
		return "SearchableEntityMetamodel [searchableType=" + searchableType
				+ ", searchFields=" + searchFields + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((searchFields == null) ? 0 : searchFields.hashCode());
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
		if (searchFields == null) {
			if (other.searchFields != null)
				return false;
		} else if (!searchFields.equals(other.searchFields))
			return false;
		if (searchableTypeName == null) {
			if (other.searchableTypeName != null)
				return false;
		} else if (!searchableTypeName.equals(other.searchableTypeName))
			return false;
		return true;
	}
}
