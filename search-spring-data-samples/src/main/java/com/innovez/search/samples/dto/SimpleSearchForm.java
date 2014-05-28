package com.innovez.search.samples.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.innovez.core.entity.support.search.SearchParameterHolder;

/**
 * Search parameter holder type.
 * 
 * @author zakyalvan
 */
@SuppressWarnings("serial")
public class SimpleSearchForm implements SearchParameterHolder, Serializable {
	private boolean enabled = false;
	private Class<?> target;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public Class<?> getTarget() {
		return target;
	}
	public void setTarger(Class<?> target) {
		this.target = target;
	}
	
	@Override
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}