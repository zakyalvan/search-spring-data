package com.innovez.core.entity.support.search.model;

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
	 * Read metamodel of searchable target with excluding fields.
	 * 
	 * @param target
	 * @param excludeField
	 * @return
	 * @throws Exception
	 */
	<T> SearchableMetamodel read(Class<T> target, String... excludeFields) throws Exception;
}
