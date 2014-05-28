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
			throw new JspException("Form parameter should not be null");
		}
		
		ApplicationContext applicationContext = getApplicationContext();
		
		Class<?> targetType = formObject.getTarget();
		SearchManager searchManager = applicationContext.getBean(SearchManager.class);
		SearchableMetamodel searchableModel = searchManager.getSearchMetamodel(targetType);
		
		if(formUrl == null) {
			formUrl = "#";
		}
		
		logger.debug("Build html form.");
		StringBuilder htmlFormBuilder = new StringBuilder();
		htmlFormBuilder.append("<form action='" + formUrl + "' class='form form-inline' role='search' method='post'>");
		htmlFormBuilder.append("<input type='hidden' name='_simpleSearchForm_target' id='_simpleSearchForm_target'>");
		htmlFormBuilder.append("<input type='hidden' name='_simpleSearchForm_searchBy' id='_simpleSearchForm_searchBy'>");
		htmlFormBuilder.append("<input type='hidden' name='_simpleSearchForm_searchParameter' id='_simpleSearchForm_searchParameter'>");
		htmlFormBuilder.append("<div class='input-group'>\n");
		htmlFormBuilder.append("<div class='input-group-btn search-type-selection'>\n");
		
		String selectedSearchField = applicationContext.getMessage(hintText, new Object[] {}, hintText, Locale.ENGLISH);
		if(formObject.isEnabled()) {
			String searchField = formObject.getSearchField();
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
		
		for(SearchableFieldMetamodel field : searchableModel.getSearchFields()) {
			String label = applicationContext.getMessage(field.getLabel(), new Object[] {}, field.getLabel(), Locale.ENGLISH);
			htmlFormBuilder.append("<li><a href='#'>" + label + "</a></li>\n");
		}
		
		htmlFormBuilder.append("</ul>\n");
		htmlFormBuilder.append("</div>\n");
		htmlFormBuilder.append("<input type='text' class='form-control' placeholder='Keyword' />\n");
		htmlFormBuilder.append("<span class='input-group-btn'>\n");
		htmlFormBuilder.append("<button class='btn btn-primary' type='submit'><span class='glyphicon glyphicon-search'></span></button>\n");
		htmlFormBuilder.append("</span>\n");
		htmlFormBuilder.append("</div>\n");
		htmlFormBuilder.append("</form>\n");
		
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
