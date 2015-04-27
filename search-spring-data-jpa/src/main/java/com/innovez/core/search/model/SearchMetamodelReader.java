package com.innovez.core.search.model;

import java.util.Map;

import com.innovez.core.search.annotation.Searchable;

/**
 * Contract for {@link SearchableMetamodel} reader.
 * 
 * @author zakyalvan
 */
public interface SearchMetamodelReader {
	/**
	 * Ask whether reader can read metamodel for given target.
	 * 
	 * @param target
	 * @return
	 */
	<T> boolean support(Class<T> target);
	
	/**
	 * Read metamodel of searchable target
	 * 
	 * @param target
	 * @return
	 * @throws Exception
	 */
	<T> SearchableMetamodel read(Class<T> target) throws Exception;
	
	/**
	 * Read metamodel of searchable target with including fields.
	 * 
	 * @param target
	 * @param includeFields
	 * @return
	 * @throws Exception
	 */
	<T> SearchableMetamodel read(Class<T> target, String... includeFields) throws Exception;
	
	/**
	 * Read searchable fields metamodel for given {@link Searchable} target.
	 * 
	 * @param target
	 * @return
	 * @throws Exception
	 */
	<T> Map<String, SearchableFieldMetamodel> readFields(Class<T> target) throws Exception;
	
	/**
	 * Read searchable fields metamodel for given {@link Searchable} target with including fields.
	 * 
	 * @param target
	 * @param overrideFields
	 * @return
	 * @throws Exception
	 */
	<T> Map<String, SearchableFieldMetamodel> readFields(Class<T> target, Map<String, SearchableFieldInfo> includeFields) throws Exception;
}
