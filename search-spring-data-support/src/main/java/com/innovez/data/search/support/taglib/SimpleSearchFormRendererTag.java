package com.innovez.data.search.support.taglib;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.innovez.core.entity.support.search.SearchManager;
import com.innovez.core.entity.support.search.model.CompositeSearchableFieldMetamodel;
import com.innovez.core.entity.support.search.model.SearchableFieldMetamodel;
import com.innovez.core.entity.support.search.model.SearchableMetamodel;
import com.innovez.data.search.support.dto.SimpleSearchForm;

/**
 * Simple search tag handler.
 * 
 * @author zakyalvan
 */
public class SimpleSearchFormRendererTag extends SimpleTagSupport {
	public static final String DEFAULT_SEARCH_HINT_TEXT = "Search By";
	
	public static final String DEFAULT_SEARCH_PLACEHOLDER_CODE = "commons.searchField.placeholder.code";
	public static final String DEFAULT_SEARCH_PLACEHOLDER_TEXT = "Search Keyword";
	
	private Logger logger = Logger.getLogger(SimpleSearchFormRendererTag.class);
	
	/**
	 * Search form object.
	 */
	private SimpleSearchForm formObject;
	
	/**
	 * Action url for form.
	 */
	private String formUrl;
	
	/**
	 * Page context variable, container for html form, if not given, the html will be rendered in place of tag declaration.
	 */
	private String htmlVar;
	
	/**
	 * Page context variable, container for javascript, if not given, the script will be rendered in place of tag declaration.
	 */
	private String scriptVar;
	
	/**
	 * Search hint text, text displayed as label for search field.
	 */
	private String hintText = DEFAULT_SEARCH_HINT_TEXT;
	
	/**
	 * Search field place holder text.
	 */
	private String placeholderText = DEFAULT_SEARCH_PLACEHOLDER_TEXT;
	
	@Override
	public void doTag() throws JspException, IOException {
		logger.debug("Start handle search-param tag.");
		if(formObject == null) {
			throw new JspException("Form object parameter should not be null");
		}
		
		ApplicationContext applicationContext = getApplicationContext();
		
		Class<?> targetType = formObject.getSearchTarget();
		logger.debug("Target type of search form object : " + targetType.getName());
		
		SearchManager searchManager = applicationContext.getBean(SearchManager.class);
		SearchableMetamodel searchableModel = searchManager.getSearchMetamodel(targetType);
		
		if(formUrl == null) {
			formUrl = "#";
		}
		
		logger.debug("Build html form.");
		StringBuilder htmlFormBuilder = new StringBuilder();
		htmlFormBuilder.append("<form id='search-form' action='" + formUrl + "' class='form form-inline' role='search' method='post'>");
		htmlFormBuilder.append("<input type='hidden' name='_simpleSearchForm_target' id='search-target' value='T(" + targetType.getName() + ")'>");
		
		/**
		 * Parameter name on the form.
		 */
		String parameterName = StringUtils.hasText(formObject.getParameterName()) ? formObject.getParameterName() : "";
		htmlFormBuilder.append("<input type='hidden' value='" + parameterName + "' name='_simpleSearchForm_searchBy' id='parameter-name'>");
		
		htmlFormBuilder.append("<div class='input-group'>");
		htmlFormBuilder.append("<div class='input-group-btn search-type-selection'>");
		
		/**
		 * Class for search related buttons, we differentiated used and unused search field by different css class bur buttons.
		 */
		String searchButtonCssClass = "btn btn-default";
		
		/**
		 * Default label for search field selection.
		 */
		String selectedSearchField = hintText;
		
		String searchFieldAutofocus = "";
		
		if(formObject.isEnabled()) {
			searchButtonCssClass = "btn btn-primary";
			String searchField = formObject.getParameterName();
			if(StringUtils.hasText(searchField)) {
				selectedSearchField = searchableModel.getSearchableField(searchField).getLabel();
			}
			
			searchFieldAutofocus = "autofocus";
		}
		/**
		 * Resolve to message source, if not dclared in message source, use given text.
		 */
		selectedSearchField = applicationContext.getMessage(
				selectedSearchField, 
				new Object[] {}, 
				selectedSearchField, 
				LocaleContextHolder.getLocale());
		
		
		htmlFormBuilder.append("<button type='button' class='" + searchButtonCssClass + " dropdown-toggle' data-toggle='dropdown'><span class='search-type-selected'>" + selectedSearchField + "</span> <span class='caret'></span></button>\n");
		htmlFormBuilder.append("<ul class='dropdown-menu'>\n");
		
		/**
		 * Iterate and print searchable field label.
		 */
		List<SearchableFieldNameToLabelMap> fieldNameToLabelMaps = extractFieldNameToLabelMaps(searchableModel, null);
		for(SearchableFieldNameToLabelMap fieldNameToLabelMap : fieldNameToLabelMaps) {
			String label = applicationContext.getMessage(
					fieldNameToLabelMap.getLabel(), 
					new Object[] {}, 
					fieldNameToLabelMap.getLabel(), 
					LocaleContextHolder.getLocale());
			htmlFormBuilder.append("<li><a href='#' data-parameter-name='" + fieldNameToLabelMap.getName() + "'>" + label + "</a></li>");
		}
		
		htmlFormBuilder.append("</ul>");
		htmlFormBuilder.append("</div>");
		
		/**
		 * Parameter value on the form.
		 */
		String parameterValue = StringUtils.hasText((String) formObject.getParameterValue()) ? (String) formObject.getParameterValue() : "";
		String keywordPlaceholder = 
				applicationContext.getMessage(
						placeholderText, 
						new Object[] {}, 
						placeholderText, 
						LocaleContextHolder.getLocale());
		htmlFormBuilder.append("<input type='text' value='" + parameterValue + "' name='_simpleSearchForm_searchParameter' class='form-control' placeholder='" + keywordPlaceholder + "' " + searchFieldAutofocus + " />");
		
		htmlFormBuilder.append("<span class='input-group-btn'>");
		htmlFormBuilder.append("<button class=' " + searchButtonCssClass + " ' type='submit'><span class='glyphicon glyphicon-search'></span></button>");
		htmlFormBuilder.append("</span>");
		htmlFormBuilder.append("</div>");
		htmlFormBuilder.append("</form>");
		
		if(StringUtils.hasText(htmlVar)) {
			getJspContext().setAttribute(htmlVar, htmlFormBuilder.toString(), PageContext.PAGE_SCOPE);
		}
		else {
			getJspContext().getOut().write(htmlFormBuilder.toString());
		}
		
		logger.debug("Build form script.");
		StringBuilder scriptFormBuilder = new StringBuilder();
		scriptFormBuilder.append("<script type=\"text/javascript\">");
		scriptFormBuilder.append("$(function() {");
		scriptFormBuilder.append("$(\"div.search-type-selection ul li a\").click(function(e) {");
		scriptFormBuilder.append("e.preventDefault();");
		
		/**
		 *  Look at data-parameter-name of each <a> element in drop-down list above.
		 */
		scriptFormBuilder.append("$(\"form#search-form input#parameter-name\").val($(this).data(\"parameter-name\"));");
		scriptFormBuilder.append("$(\"div.search-type-selection button span.search-type-selected\").html($(this).html());");
		scriptFormBuilder.append("});");
		scriptFormBuilder.append("});");
		scriptFormBuilder.append("</script>");
		
		if(StringUtils.hasText(scriptVar)) {
			getJspContext().setAttribute(scriptVar, scriptFormBuilder.toString(), PageContext.PAGE_SCOPE);
		}
		else {
			getJspContext().getOut().write(scriptFormBuilder.toString());
		}
	}
	private List<SearchableFieldNameToLabelMap> extractFieldNameToLabelMaps(SearchableMetamodel searchableMetamodel, String parentPath) {
		List<SearchableFieldNameToLabelMap> fieldNameToLabelMaps = new ArrayList<SearchableFieldNameToLabelMap>();
		for(SearchableFieldMetamodel searchableFieldMetamodel : searchableMetamodel.getSearchableFields()) {
			StringBuilder fieldNameBuilder = new StringBuilder();
			if(parentPath != null) {
				fieldNameBuilder.append(parentPath).append(".");
			}
			
			if(searchableFieldMetamodel instanceof CompositeSearchableFieldMetamodel) {
				CompositeSearchableFieldMetamodel compositeSearchableFieldMetamodel = (CompositeSearchableFieldMetamodel) searchableFieldMetamodel;
				String relationPath = fieldNameBuilder.append(compositeSearchableFieldMetamodel.getName()).toString();
				
				// Recursively call this method to extract composite type.
				List<SearchableFieldNameToLabelMap> relationFieldNameToLabelMaps = extractFieldNameToLabelMaps(compositeSearchableFieldMetamodel, relationPath);
				for(SearchableFieldNameToLabelMap relationFieldNameToLabelMap : relationFieldNameToLabelMaps) {
					fieldNameToLabelMaps.add(relationFieldNameToLabelMap);
				}
			}
			else {
				SearchableFieldNameToLabelMap fieldNameToLabelMap = new SearchableFieldNameToLabelMap(
						fieldNameBuilder.append(searchableFieldMetamodel.getName()).toString(), 
						searchableFieldMetamodel.getLabel());
				fieldNameToLabelMaps.add(fieldNameToLabelMap);
			}
		}
		return fieldNameToLabelMaps;
	}

	public SimpleSearchForm getFormObject() {
		return formObject;
	}
	public void setFormObject(SimpleSearchForm formObject) {
		this.formObject = formObject;
	}

	public String getFormUrl() {
		return formUrl;
	}
	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	public String getHtmlVar() {
		return htmlVar;
	}
	public void setHtmlVar(String htmlVar) {
		this.htmlVar = htmlVar;
	}

	public String getScriptVar() {
		return scriptVar;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	
	public String getHintText() {
		return hintText;
	}
	public void setHintText(String hintText) {
		this.hintText = hintText;
	}
	
	public String getPlaceholderText() {
		return placeholderText;
	}
	public void setPlaceholderText(String placeholderText) {
		this.placeholderText = placeholderText;
	}

	private ApplicationContext getApplicationContext() {
		PageContext pageContext = (PageContext) getJspContext();
		return WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
	}
	
	/**
	 * Simple helper class to map field name to label
	 * 
	 * @author zakyalvan
	 */
	@SuppressWarnings("serial")
	public static class SearchableFieldNameToLabelMap implements Serializable {
		private final String name;
		private final String label;
		public SearchableFieldNameToLabelMap(String name, String label) {
			Assert.hasLength(name);
			Assert.hasLength(label);
			
			this.name = name;
			this.label = label;
		}
		public String getName() {
			return name;
		}
		public String getLabel() {
			return label;
		}
	}
}
