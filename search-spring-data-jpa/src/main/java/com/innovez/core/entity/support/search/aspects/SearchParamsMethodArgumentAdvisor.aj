package com.innovez.core.entity.support.search.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.innovez.core.entity.support.search.SearchManager;
import com.innovez.core.entity.support.search.annotation.SearchParams;

/**
 * Aspect which responsible for advice method with {@link SearchParams} annotated argument.
 * 
 * @author zakyalvan
 */
public aspect SearchParamsMethodArgumentAdvisor {
	private Logger logger = Logger.getLogger(SearchParamsMethodArgumentAdvisor.class);
	
	@Autowired
	private SearchManager searchManager;
	
	/**
	 * Pointcut on method executions with {@link SearchParams} annotated parameter.
	 * 
	 * @param searchParams
	 */
	pointcut withSearchParamsAnnotatedArgument(Object targetObject, Map<String, Object> parameters) : execution(* *.*(.., @SearchParams (Map<String,Object>))) && args(.., parameters) && target(targetObject);
	Object around(Object targetObject, Map<String, Object> parameters) :  withSearchParamsAnnotatedArgument(targetObject, parameters) {
		logger.debug("Before execution of method with @SearchParams annotated argument, check whether validate search parameters.");
		
		Object[] methodArguments = thisJoinPoint.getArgs();
		
		MethodSignature methodSignature = (MethodSignature) thisJoinPoint.getSignature();
		Method method = methodSignature.getMethod();
		Annotation[][] methodArgumentsAnnotations = method.getParameterAnnotations();
		
		/**
		 * Loop for every method arguments, looking for SearchParams annotated arguments.
		 * Currently we only support one SearchParams per-method arguments.
		 */
		for(int i = 0; i < methodArgumentsAnnotations.length; i++) {
			Annotation[] argumentAnnotations = methodArgumentsAnnotations[i];
			for(Annotation methodArgumentAnnotation : argumentAnnotations) {
				if(methodArgumentAnnotation instanceof SearchParams) {
					Assert.isTrue(methodArguments[i] instanceof Map, "SearchParams annotated method argument (on " + targetObject.getClass().getName() + "." + method.getName() + ") should in type of Map with key String and general Object value.");
					
					@SuppressWarnings("unchecked")
					Map<String, Object> searchParameters = (Map<String, Object>) methodArguments[i];
					SearchParams searchParamsAnnotation = (SearchParams) methodArgumentAnnotation;
					if(searchParamsAnnotation.validate()) {
						logger.debug("Validation on SearchParams annotated method argument of method " + targetObject.getClass().getName() + "." + method.getName() + " is enabled. Validate search parameters.");
						Assert.isTrue(searchManager.isValidSearchParameters(searchParamsAnnotation.target(), searchParameters), "Given search parameters " + searchParameters + " is not valid for target type " + searchParamsAnnotation.target());				
					}
					
					// Found SearchParams annotated argument, break process.
					break;
				}
			}
		}
		
		logger.debug("Proceed call with parameters : " + parameters);
		return proceed(targetObject, parameters);
	}
}
