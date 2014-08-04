package com.innovez.core.entity.support.search.model;

import java.io.Serializable;

import org.springframework.core.Ordered;
import org.springframework.util.Assert;

import com.innovez.core.entity.support.search.annotation.SearchableField;

/**
 * Just a convenience data transfer object for {@link SearchableField} annotation attributes.
 * 
 * @author zakyalvan
 */
@SuppressWarnings("serial")
public class SearchableFieldInfo implements SearchableTypeContainer, Serializable {
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
