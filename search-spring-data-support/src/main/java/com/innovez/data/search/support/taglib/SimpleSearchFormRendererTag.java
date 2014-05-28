package com.innovez.data.search.support.taglib;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.innovez.core.entity.support.search.SearchManager;
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
	 * 
	 */
	private String htmlVar;
	
	/**
	 * 
	 */
	private String scriptVar;
	
	/**
	 * 
	 */
	private String hintText = DEFAULT_SEARCH_HINT_TEXT;
	
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
		
		String selectedSearchField = applicationContext.getMessage(hintText, new Object[] {}, hintText, Locale.ENGLISH);
		if(formObject.isEnabled()) {
			String searchField = formObject.getParameterName();
			if(StringUtils.hasText(searchField)) {
				selectedSearchField = applicationContext.getMessage(
						searchableModel.getSearchField(searchField).getLabel(), 
						new Object[] {},
						searchableModel.getSearchField(searchField).getLabel(), 
						Locale.ENGLISH);
			}
		}
		
		htmlFormBuilder.append("<button type='button' class='btn btn-default dropdown-toggle' data-toggle='dropdown'><span class='search-type-selected'>" + selectedSearchField + "</span> <span class='caret'></span></button>\n");
		htmlFormBuilder.append("<ul class='dropdown-menu'>\n");
		
		/**
		 * Iterate and print searchable field label.
		 */
		for(SearchableFieldMetamodel field : searchableModel.getSearchFields()) {
			String name = field.getName();
			String label = applicationContext.getMessage(field.getLabel(), new Object[] {}, field.getLabel(), Locale.ENGLISH);
			htmlFormBuilder.append("<li><a href='#' data-parameter-name='" + name + "'>" + label + "</a></li>");
		}
		
		htmlFormBuilder.append("</ul>");
		htmlFormBuilder.append("</div>");
		
		/**
		 * Parameter value on the form.
		 */
		String parameterValue = StringUtils.hasText((String) formObject.getParameterValue()) ? (String) formObject.getParameterValue() : "";
		htmlFormBuilder.append("<input type='text' value='" + parameterValue + "' name='_simpleSearchForm_searchParameter' class='form-control' placeholder='Keyword' />");
		
		htmlFormBuilder.append("<span class='input-group-btn'>");
		htmlFormBuilder.append("<button class='btn btn-primary' type='submit'><span class='glyphicon glyphicon-search'></span></button>");
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

	private ApplicationContext getApplicationContext() {
		PageContext pageContext = (PageContext) getJspContext();
		return WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
	}
}
