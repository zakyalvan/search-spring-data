package com.innovez.core.entity.support.search.model;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * Default implementation of {@link SearchableFieldMetamodel}.
 * 
 * @author zakyalvan
 */
@SuppressWarnings("serial")
public class BasicSearchableJpaEntityFieldMetamodel implements BasicSearchableFieldMetamodel, Serializable {
	private final Class<?> searchableType;
	private final String searchableTypeName;
	private final String name;
	private final Class<?> fieldType;
	private final String fieldTypeName;
	private final String label;
	private final int order;
	
	public BasicSearchableJpaEntityFieldMetamodel(Class<?> searchableType, String name, Class<?> fieldType, String label, int order) {
		Assert.notNull(searchableType);
		Assert.notNull(name);
		Assert.notNull(fieldType);
		Assert.notNull(label);
		
		this.searchableType = searchableType;
		this.searchableTypeName = searchableType.getName();
		this.name = name;
		this.fieldType = fieldType;
		this.fieldTypeName = fieldType.getName();
		this.label = label;
		this.order = order;
	}

	@Override
	public Class<?> getSearchableType() {
		return searchableType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?> getFieldType() {
		return fieldType;
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public int getOrder() {
		return order;
	}

	

	@Override
	public String toString() {
		return "BasicSearchableJpaEntityFieldMetamodel [searchableTypeName="
				+ searchableTypeName + ", name=" + name + ", fieldTypeName="
				+ fieldTypeName + ", label=" + label + ", order=" + order + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldTypeName == null) ? 0 : fieldTypeName.hashCode());
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
		BasicSearchableJpaEntityFieldMetamodel other = (BasicSearchableJpaEntityFieldMetamodel) obj;
		if (fieldTypeName == null) {
			if (other.fieldTypeName != null)
				return false;
		} else if (!fieldTypeName.equals(other.fieldTypeName))
			return false;
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
