package com.innovez.core.entity.support.search.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import com.innovez.core.entity.support.search.SearchManager;
import com.innovez.core.entity.support.search.SearchSpecificationHolder;
import com.innovez.core.entity.support.search.annotation.SearchParams;
import com.innovez.core.entity.support.search.model.SearchableFieldMetamodel;
import com.innovez.core.entity.support.search.model.SearchableMetamodel;

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
		
		final List<Specification<T>> singleSpecifications = new ArrayList<Specification<T>>();
		for(final SearchableFieldMetamodel fieldMetamodel : metamodel.getSearchableFields()) {
			if(!parameters.containsKey(fieldMetamodel.getName())) {
				continue;
			}
			
			if(parameters.get(fieldMetamodel.getName()) == null) {
				continue;
			}
			
			logger.debug("Build specification for field : " + fieldMetamodel.getName() + " with type : " + fieldMetamodel.getFieldType());
			
			// If string, using 'like' clause.
			if(String.class.isAssignableFrom(fieldMetamodel.getFieldType())) {
				Specification<T> singleSpecification = new Specification<T>() {
					@Override
					public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						// Don't forget to use cb.lower.
						return cb.like(
								cb.lower(root.<String>get(fieldMetamodel.getName())), 
								"%" + ((String) parameters.get(fieldMetamodel.getName())).toLowerCase() + "%");
					}
				};
				singleSpecifications.add(singleSpecification);
			}
			// If number, using equal.
			else if(Number.class.isAssignableFrom(fieldMetamodel.getFieldType())) {
				Specification<T> singleSpecification = new Specification<T>() {
					@Override
					public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						return cb.equal(
								root.get(fieldMetamodel.getName()), 
								parameters.get(fieldMetamodel.getName()));
					}
				};
				singleSpecifications.add(singleSpecification);
			}
		}
		
		if(singleSpecifications.size() == 1) {
			logger.debug("Only one search specification built.");
			return singleSpecifications.get(0);
		}
		else {
			Specification<T> finalSpecification = new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> singlePredicates = new ArrayList<Predicate>();
					for(Specification<T> singleSpecification : singleSpecifications) {
						singlePredicates.add(singleSpecification.toPredicate(root, query, cb));
					}
					return cb.and((Predicate[]) singlePredicates.toArray());
				}
			};
			return finalSpecification;
		}
	}
}
