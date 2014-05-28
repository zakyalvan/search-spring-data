package com.innovez.data.search.support.webmvc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.innovez.core.entity.support.search.SearchManager;
import com.innovez.core.entity.support.search.SearchParameterHolder;
import com.innovez.core.entity.support.search.annotation.SearchTarget;
import com.innovez.data.search.support.dto.SimpleSearchForm;

/**
 * 
 * 
 * @author zakyalvan
 */
public class SearchParameterHolderArgumentResolver implements WebArgumentResolver {
	public static final String DEFAULT_TARGET_TYPE_REQUEST_PARAMETER_NAME = "_simpleSearchForm_target";
	public static final String DEFAULT_SEARCH_BY_REQUEST_PARAMETER_NAME = "_simpleSearchForm_searchBy";
	public static final String DEFAULT_SEARCH_PARAM_REQUEST_PARAMETER_NAME = "_simpleSearchForm_searchParameter";
	
	private Logger logger = Logger.getLogger(SearchParameterHolderArgumentResolver.class);
	
	@Autowired
	private SearchManager searchManager;
	
	private ExpressionParser expressionParser = new SpelExpressionParser();
	
	private String targetTypeRequestParam = DEFAULT_TARGET_TYPE_REQUEST_PARAMETER_NAME;
	private String parameterNameRequestParam = DEFAULT_SEARCH_BY_REQUEST_PARAMETER_NAME;
	private String parameterValueRequestParam = DEFAULT_SEARCH_PARAM_REQUEST_PARAMETER_NAME;
	
	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		Class<?> parameterType = methodParameter.getParameterType();
		logger.debug("Resolve parameter for controller method argument with type : " + parameterType.getName());
		
		if(!SearchParameterHolder.class.isAssignableFrom(parameterType)) {
			logger.error("Can't resolve, given parameter type " + parameterType.getName() + " is not assignable to " + SearchParameterHolder.class.getName());
			return WebArgumentResolver.UNRESOLVED;
		}
		
		logger.debug("Try to resolve search target type, first from @SearchTarget argument annotation, if not found, try resolve from submitted search target type.");
		Class<?> searchTargetType = null;
		SearchTarget searchTargetAnnotation = methodParameter.getParameterAnnotation(SearchTarget.class);
		if(searchTargetAnnotation != null) {
			logger.debug("Search form handler parameter hinted with @SearchTarget annotation.");
			searchTargetType = searchTargetAnnotation.value();
		}
		
		if(searchTargetType == null) {
			logger.debug("Search form handler parameter not hinted with @SearchTarget, try to resolve target type from request parameter.");
			String submittedSearchTargetTypeRequestParam = webRequest.getParameter(targetTypeRequestParam);
			
			try {
				Expression searchTargetTypeExpression = expressionParser.parseExpression(submittedSearchTargetTypeRequestParam);
				searchTargetType = searchTargetTypeExpression.getValue(Class.class);
			}
			catch(EvaluationException evaluationException) {
				logger.error("Can't parse search target type submitted", evaluationException);
				throw new IllegalStateException("Can't parse search target type submitted", evaluationException);
			}
		}
		
		String parameterName = webRequest.getParameter(parameterNameRequestParam);
		String parameterValue = webRequest.getParameter(parameterValueRequestParam);
		
		boolean enabled = StringUtils.hasText(parameterName) && StringUtils.hasText(parameterValue);
		
		if(enabled) {
			return new SimpleSearchForm(searchTargetType, enabled, parameterName, parameterValue);
		}
		else {
			return new SimpleSearchForm(searchTargetType);
		}
	}

	public String getTargetTypeRequestParam() {
		return targetTypeRequestParam;
	}
	public void setTargetTypeRequestParam(String targetTypeRequestParam) {
		this.targetTypeRequestParam = targetTypeRequestParam;
	}

	public String getParameterNameRequestParam() {
		return parameterNameRequestParam;
	}
	public void setParameterNameRequestParam(String parameterNameRequestParam) {
		this.parameterNameRequestParam = parameterNameRequestParam;
	}

	public String getParameterValueRequestParam() {
		return parameterValueRequestParam;
	}
	public void setParameterValueRequestParam(String parameterValueRequestParam) {
		this.parameterValueRequestParam = parameterValueRequestParam;
	}
}
