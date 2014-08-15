package com.innovez.core.search.model;

import java.util.Collection;

public class AbstractSearchableMetamodel implements SearchableMetamodel {
	@Override
	public Class<?> getSearchableType() {
		return null;
	}

	@Override
	public Collection<SearchableFieldMetamodel> getSearchableFields() {
		return null;
	}
	
	@Override
	public SearchableFieldMetamodel getDefaultField() {
		return null;
	}

	@Override
	public boolean hasSearchableField(String fieldName) {
		return false;
	}

	@Override
	public SearchableFieldMetamodel getSearchableField(String fieldName) {
		return null;
	}
}
