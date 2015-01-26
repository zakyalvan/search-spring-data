package com.innovez.core.search.model;


/**
 * Contract for {@link SearchableMetamodel} reader.
 * 
 * @author zakyalvan
 */
public interface SearchableMetamodelReader {
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
}
