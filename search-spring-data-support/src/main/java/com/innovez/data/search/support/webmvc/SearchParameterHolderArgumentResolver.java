package com.innovez.data.search.support.webmvc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.innovez.core.entity.support.search.SearchManager;
import com.innovez.core.entity.support.search.SearchParameterHolder;
import com.innovez.data.search.support.dto.SimpleSearchForm;

/**
 * 
 * 
 * @author zakyalvan
 */
public class SearchParameterHolderArgumentResolver implements WebArgumentResolver {
	public static final String DEFAULT_TARGET_REQUEST_PARAMETER_NAME = "_simpleSearchForm_target";
	public static final String DEFAULT_SEARCH_BY_REQUEST_PARAMETER_NAME = "_simpleSearchForm_searchBy";
	public static final String DEFAULT_SEARCH_PARAM_REQUEST_PARAMETER_NAME = "_simpleSearchForm_searchParameter";
	
	private Logger logger = Logger.getLogger(SearchParameterHolderArgumentResolver.class);
	
	@Autowired
	private SearchManager searchManager;
	
	private ExpressionParser expressionParser = new SpelExpressionParser();
	
	private String targetRequestParameter = DEFAULT_TARGET_REQUEST_PARAMETER_NAME;
	private String searchByRequestParameter = DEFAULT_SEARCH_BY_REQUEST_PARAMETER_NAME;
	private String searchParamRequestParameter = DEFAULT_SEARCH_PARAM_REQUEST_PARAMETER_NAME;
	
	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		Class<?> parameterType = methodParameter.getParameterType();
		logger.debug("Resolve parameter for controller method argument with type : " + parameterType.getName());
		
		if(!SearchParameterHolder.class.isAssignableFrom(parameterType)) {
			logger.error("Can't resolve, given parameter type " + parameterType.getName() + " is not assignable to " + SearchParameterHolder.class.getName());
			return WebArgumentResolver.UNRESOLVED;
		}
		
		String target = webRequest.getParameter(targetRequestParameter);
		String searchBy = webRequest.getParameter(searchByRequestParameter);
		String searchParameter = webRequest.getParameter(searchParamRequestParameter);
		
		try {
			Assert.isTrue(StringUtils.hasText((String) target), "No request parameter with name " + targetRequestParameter + " for determining search target type.");
			Assert.isTrue(StringUtils.hasText((String) searchBy), "No request parameter with name " + searchByRequestParameter + " for determining search field used.");
			Assert.isTrue(StringUtils.hasText((String) searchParameter), "No request parameter with name " + searchParamRequestParameter + " for determining search parameter value.");
		}
		catch(IllegalArgumentException iae) {
			logger.error("One or more required request parameter for search not found in request.");
			return WebArgumentResolver.UNRESOLVED;
		}
		
		SimpleSearchForm simpleSearchForm = null;
		
		/**
		 * TODO Fix me! This block not usable!
		 */
		ModelAttribute modelAttribute = methodParameter.getParameterAnnotation(ModelAttribute.class);
		boolean modelAttributeAnnotatedParam = false;
		if(modelAttribute != null) {
			logger.debug("Method parameter annotated with ModelAttribute, try lookup to session for object SimpleSearchForm.");
			modelAttributeAnnotatedParam = true;
			String searchFormName = modelAttribute.value();
			simpleSearchForm = (SimpleSearchForm) webRequest.getAttribute(searchFormName, WebRequest.SCOPE_SESSION);
			logger.error("No search form object found at session with name : " + searchFormName);
		}
		
		if(simpleSearchForm == null) {
			if(modelAttributeAnnotatedParam) {
				logger.debug("Method parameter annotated with @ModelAttribute, no serach form object found for given attribute name.");
			}
			logger.debug("Create fresh search form object.");
			simpleSearchForm = new SimpleSearchForm();
		}
		
		Expression targetTypeExpression = expressionParser.parseExpression(target);
		Class<?> targetType = targetTypeExpression.getValue(Class.class);
		
		simpleSearchForm.setTarget(targetType);
		simpleSearchForm.getParameters().put(searchBy, searchParameter);
		
		return simpleSearchForm;
	}

	public String getTargetRequestParameter() {
		return targetRequestParameter;
	}
	public void setTargetRequestParameter(String targetRequestParameter) {
		this.targetRequestParameter = targetRequestParameter;
	}

	public String getSearchByRequestParameter() {
		return searchByRequestParameter;
	}
	public void setSearchByRequestParameter(String searchByRequestParameter) {
		this.searchByRequestParameter = searchByRequestParameter;
	}

	public String getSearchParamRequestParameter() {
		return searchParamRequestParameter;
	}
	public void setSearchParamRequestParameter(String searchParamRequestParameter) {
		this.searchParamRequestParameter = searchParamRequestParameter;
	}
}
