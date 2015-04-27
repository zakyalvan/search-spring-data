package com.innovez.web.support.search;

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
	 * Search target type.
	 */
	private Class<?> searchTarget;
	
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
	
	@Override
	public Class<?> getSearchTarget() {
		return searchTarget;
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	public Object getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(Object parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	@Override
	public String toString() {
		return "SimpleSearchForm [enabled=" + enabled + ", parameterName="
				+ parameterName + ", parameterValue=" + parameterValue
				+ ", searchTarget=" + searchTarget + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((searchTarget.getName() == null) ? 0 : searchTarget.getName().hashCode());
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
		SimpleSearchForm other = (SimpleSearchForm) obj;
		if (searchTarget.getName() == null) {
			if (other.searchTarget.getName() != null)
				return false;
		} else if (!searchTarget.getName().equals(other.searchTarget.getName()))
			return false;
		return true;
	}
}