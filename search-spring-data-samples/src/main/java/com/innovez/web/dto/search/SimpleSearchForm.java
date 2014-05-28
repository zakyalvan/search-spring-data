package com.innovez.web.dto.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

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
	
	public SimpleSearchForm() {}
	public SimpleSearchForm(Class<?> target) {
		Assert.notNull(target, "Target type parameter should not be null");
		this.target = target;
	}
	
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
	public void setTarget(Class<?> target) {
		Assert.notNull(target, "Target type parameter should not be null");
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