package com.innovez.web.support.search;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.innovez.core.search.SearchManager;
import com.innovez.core.search.SearchParameterHolder;
import com.innovez.core.search.annotation.SearchTarget;

/**
 * Resolving {@link SearchParameterHolder} on handler methods arguments.
 * 
 * @author zakyalvan
 */
public class SearchParameterMethodArgumentResolver implements HandlerMethodArgumentResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchParameterMethodArgumentResolver.class);
	
	public static final String DEFAULT_TARGET_TYPE_REQUEST_PARAMETER_NAME = "_simpleSearchForm_target";
	public static final String DEFAULT_SEARCH_BY_REQUEST_PARAMETER_NAME = "_simpleSearchForm_searchBy";
	public static final String DEFAULT_SEARCH_PARAM_REQUEST_PARAMETER_NAME = "_simpleSearchForm_searchParameter";
	
	@Autowired
	private SearchManager searchManager;
	
	private ExpressionParser expressionParser = new SpelExpressionParser();
	
	private String targetTypeRequestParam = DEFAULT_TARGET_TYPE_REQUEST_PARAMETER_NAME;
	private String parameterNameRequestParam = DEFAULT_SEARCH_BY_REQUEST_PARAMETER_NAME;
	private String parameterValueRequestParam = DEFAULT_SEARCH_PARAM_REQUEST_PARAMETER_NAME;
	
	/**
	 * Simple internal cache for search form, so no need to create new object on each resolve process.
	 */
	private Map<Class<?>, SimpleSearchForm> searchFormCache = new HashMap<Class<?>, SimpleSearchForm>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		LOGGER.debug("Check whether this type can resolve handler method argument with type : {}", parameter.getParameterType().getName());
		Class<?> parameterType = parameter.getParameterType();
		
		if(!SearchParameterHolder.class.isAssignableFrom(parameterType)) {
			LOGGER.error("Can't resolve, given parameter type {} is not assignable to {} ", parameterType.getName(), SearchParameterHolder.class.getName());
			return false;
		}
		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		LOGGER.debug("Try to resolve parameter for controller method argument with type {}", parameter.getParameterType().getName());
		
		Class<?> searchTargetType = null;
		SearchTarget searchTargetAnnotation = parameter.getParameterAnnotation(SearchTarget.class);
		if(searchTargetAnnotation != null) {
			LOGGER.debug("Search form handler parameter hinted with @SearchTarget annotation.");
			searchTargetType = searchTargetAnnotation.value();
		}
		
		if(searchTargetType == null) {
			LOGGER.debug("Search form handler parameter not hinted with @SearchTarget, try to resolve target type from request parameter.");
			String submittedSearchTargetTypeRequestParam = webRequest.getParameter(targetTypeRequestParam);
			
			try {
				Expression searchTargetTypeExpression = expressionParser.parseExpression(submittedSearchTargetTypeRequestParam);
				searchTargetType = searchTargetTypeExpression.getValue(Class.class);
			}
			catch(EvaluationException evaluationException) {
				LOGGER.error("Can't parse search target type submitted", evaluationException);
				throw new IllegalStateException("Can't parse search target type submitted", evaluationException);
			}
		}
		
		String parameterName = webRequest.getParameter(parameterNameRequestParam);
		String parameterValue = webRequest.getParameter(parameterValueRequestParam);
		
		boolean searchEnabled = StringUtils.hasText(parameterName) && StringUtils.hasText(parameterValue);
		
		// Create new search form if not already in search form cache.
		if(!searchFormCache.containsKey(searchTargetType)) {
			searchFormCache.put(searchTargetType, new SimpleSearchForm(searchTargetType));
		}
		
		SimpleSearchForm searchForm = searchFormCache.get(searchTargetType);
		if(searchEnabled) {
			searchForm.setEnabled(true);
			searchForm.setParameterName(parameterName);
			searchForm.setParameterValue(parameterValue);
			searchForm.getParameters().clear();
			searchForm.getParameters().put(parameterName, parameterValue);
		}
		else {
			searchForm.setEnabled(false);
			searchForm.setParameterName(null);
			searchForm.setParameterValue(null);
			searchForm.getParameters().clear();
		}
		return searchForm;
	}
}
