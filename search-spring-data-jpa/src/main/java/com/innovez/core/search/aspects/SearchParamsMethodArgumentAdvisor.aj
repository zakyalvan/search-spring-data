package com.innovez.core.search.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import com.innovez.core.search.SearchManager;
import com.innovez.core.search.SearchSpecificationHolder;
import com.innovez.core.search.annotation.SearchParams;
import com.innovez.core.search.model.SearchableMetamodel;

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
		
		Map<String, Object> overridingParameters = new HashMap<String, Object>();
		for(String searchField : parameters.keySet()) {
			overridingParameters.put(searchField, parameters.get(searchField));
		}
		
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
					
					/**
					 * Add final search specification.
					 */
					overridingParameters.put(SearchSpecificationHolder.FINAL_SPECIFICATION, buildFinalSpecification(searchParamsAnnotation.target(), searchParameters));
					
					logger.debug("Search param found, break!");
					break;
				}
			}
		}
		
		logger.debug("Proceed call with parameters : " + overridingParameters);
		return proceed(targetObject, overridingParameters);
	}
	
	/**
	 * Build final specification.
	 * 
	 * @param target
	 * @param parameters
	 * @return
	 */
	private <T> Specification<T> buildFinalSpecification(Class<T> target, final Map<String, Object> parameters) {
		logger.debug("Build final search specification.");
		
		if(parameters.size() == 0) {
			return null;
		}
		
		SearchableMetamodel metamodel = searchManager.getSearchMetamodel(target);
		final List<Specification<T>> searchSpecifications = new ArrayList<Specification<T>>();
		for(final String fieldName : parameters.keySet()) {
			Assert.isTrue(metamodel.hasSearchableField(fieldName), String.format("Field name '%s' given on parameter map object is not valid searcable field", fieldName));
			
			Class<?> declaredFieldType = metamodel.getSearchableField(fieldName).getFieldType();
			// Remember, if string type use 'like' clause, and use criteriaBuilder.lower method to built spec.
			if(String.class.isAssignableFrom(declaredFieldType)) {
				Specification<T> parameterSpecification = new Specification<T>() {
					@Override
					public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						// Iterate, to anticipate if given parameter in aaa.bbb.ccc nested field.
						String iteratingFieldName = fieldName;
						Path<?> iteratingPath = root;
						while(iteratingFieldName.split("\\.", 2).length == 2)  {
							String[] splitedIteratingFieldName = iteratingFieldName.split("\\.", 2);
							iteratingPath = iteratingPath.get(splitedIteratingFieldName[0]);
							iteratingFieldName = splitedIteratingFieldName[1];
						}
						Path<String> lastPath = iteratingPath.<String>get(iteratingFieldName);
						return cb.like(
								cb.lower(lastPath), 
								((String) parameters.get(fieldName)).toLowerCase() + "%");
					}
				};
				searchSpecifications.add(parameterSpecification);
			}
			// If any number type simply using 'equal' clause
			else if(Number.class.isAssignableFrom(declaredFieldType)) {
				try {
					NumberUtils.parseNumber((String) parameters.get(fieldName), Number.class);
					Specification<T> parameterSpecification = new Specification<T>() {
						@Override
						public Predicate toPredicate(Root<T> root,
								CriteriaQuery<?> query, CriteriaBuilder cb) {
							String iteratingFieldName = fieldName;
							Path<?> iteratingPath = root;
							while (iteratingFieldName.split("\\.", 2).length == 2) {
								String[] splitedIteratingFieldName = iteratingFieldName
										.split("\\.", 2);
								iteratingPath = iteratingPath
										.get(splitedIteratingFieldName[0]);
								iteratingFieldName = splitedIteratingFieldName[1];
							}
							Path<Number> lastPath = iteratingPath
									.<Number> get(iteratingFieldName);
							return cb
									.equal(lastPath, parameters.get(fieldName));
						}
					};
					searchSpecifications.add(parameterSpecification);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(searchSpecifications.size() == 0) {
			// Something wrong on parameter evaluation or specification building (e.g wrong number format.).
			return null;
		}
		else if(searchSpecifications.size() == 1) {
			logger.debug("Only one search specification built.");
			return searchSpecifications.get(0);
		}
		else {
			Specification<T> finalSpecification = new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> singlePredicates = new ArrayList<Predicate>();
					for(Specification<T> singleSpecification : searchSpecifications) {
						singlePredicates.add(singleSpecification.toPredicate(root, query, cb));
					}
					return cb.and((Predicate[]) singlePredicates.toArray());
				}
			};
			return finalSpecification;
		}
	}
}
