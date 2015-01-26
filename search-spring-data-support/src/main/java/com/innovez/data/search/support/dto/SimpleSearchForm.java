package com.innovez.data.search.support.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.Assert;

import com.innovez.core.search.SearchParameterHolder;

/**
 * Search parameter holder type.
 * 
 * @author zakyalvan
 */
@SuppressWarnings("serial")
public class SimpleSearchForm implements SearchParameterHolder, Serializable {
	/**
	 * Determine whether search enabled.
	 */
	private boolean enabled = false;
	
	/**
	 * Search by field, used for simple search (With one parameter).
	 */
	@NotBlank
	private String parameterName;
	
	/**
	 * Search parameter value.
	 */
	@NotNull
	private Object parameterValue;
	
	/**
	 * Search target type.
	 */
	private Class<?> searchTarget;
	
	/**
	 * Parameter map.
	 */
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	SimpleSearchForm() {}
	public SimpleSearchForm(Class<?> searchTarget) {
		Assert.notNull(searchTarget, "Target type argument should not be null");
		this.searchTarget = searchTarget;
	}
	public SimpleSearchForm(Class<?> searchTarget, boolean enabled, String parameterName, String parameterValue) {
		Assert.notNull(searchTarget, "Target type argument should not be null");
		Assert.notNull(parameterName, "Parameter name argument should not be null.");
		Assert.notNull(parameterValue, "Parameter name argument should not be null.");
		
		this.searchTarget = searchTarget;
		this.enabled = enabled;
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	
	public Object getParameterValue() {
		return parameterValue;
	}
	
	@Override
	public Class<?> getSearchTarget() {
		return searchTarget;
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
}