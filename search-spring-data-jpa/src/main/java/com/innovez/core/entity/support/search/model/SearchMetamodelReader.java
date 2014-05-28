package com.innovez.core.entity.support.search.model;

public interface SearchMetamodelReader {
	<T> boolean support(Class<T> target);
	<T> SearchableMetamodel read(Class<T> target) throws Exception;
}
