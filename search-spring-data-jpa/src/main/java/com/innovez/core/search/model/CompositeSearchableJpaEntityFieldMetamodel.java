package com.innovez.core.search.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.Assert;

@SuppressWarnings("serial")
public class CompositeSearchableJpaEntityFieldMetamodel implements CompositeSearchableFieldMetamodel, Serializable {
	private final Class<?> searchableType;
	private final String searchableTypeName;
	private final String name;
	private final Class<?> fieldType;
	private final String fieldTypeName;
	private final String label;
	private final Integer order;

	private final Map<String, SearchableFieldMetamodel> searchableFields;

	public CompositeSearchableJpaEntityFieldMetamodel(Class<?> searchableType, String name, Class<?> fieldType, String label, Integer order, Map<String, SearchableFieldMetamodel> searchableFields) {
		Assert.notNull(searchableType, "Searchable type parameter should not be null");
		Assert.hasText(name, "Name parameter should not be empty text");
		Assert.notNull(fieldType, "Field type parameter should not be null");
		Assert.hasText(label, "Label parameter should not be empty text");
		Assert.notNull(order, "Order parameter should not be null");
		Assert.notNull(searchableFields);
		
		this.searchableType = searchableType;
		this.searchableTypeName = searchableType.getName();
		this.name = name;
		this.fieldType = fieldType;
		this.fieldTypeName = fieldType.getName();
		this.label = label;
		this.order = order;
		this.searchableFields = searchableFields;
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
	public Collection<SearchableFieldMetamodel> getSearchableFields() {
		List<SearchableFieldMetamodel> searchableFieldsList = new ArrayList<SearchableFieldMetamodel>(searchableFields.values());
		Collections.sort(searchableFieldsList, AnnotationAwareOrderComparator.INSTANCE);
		return searchableFieldsList;
	}
	@Override
	public SearchableFieldMetamodel getDefaultField() {
		// Always return null value.
		return null;
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
		Assert.isTrue(hasSearchableField(fieldName), String.format("Searchable field with name %s not found", fieldName));
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
		return "CompositeSearchableJpaEntityFieldMetamodel [searchableTypeName="
				+ searchableTypeName
				+ ", name="
				+ name
				+ ", fieldTypeName="
				+ fieldTypeName
				+ ", label="
				+ label
				+ ", order="
				+ order
				+ ", searchableFields=" + searchableFields + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fieldTypeName == null) ? 0 : fieldTypeName.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
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
		CompositeSearchableJpaEntityFieldMetamodel other = (CompositeSearchableJpaEntityFieldMetamodel) obj;
		if (fieldTypeName == null) {
			if (other.fieldTypeName != null)
				return false;
		} else if (!fieldTypeName.equals(other.fieldTypeName))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (searchableTypeName == null) {
			if (other.searchableTypeName != null)
				return false;
		} else if (!searchableTypeName.equals(other.searchableTypeName))
			return false;
		return true;
	}
}
