package com.innovez.data.search.support.taglib;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
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
	private Logger logger = Logger.getLogger(SimpleSearchFormRendererTag.class);
	
	/**
	 * 
	 */
	private SimpleSearchForm formObject;
	
	/**
	 * Action url for form.
	 */
	private String formUrl;
	
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
		
		StringBuilder htmlForm = new StringBuilder();
		htmlForm.append("<form action='" + formUrl + "' class='form form-inline' role='search' method='post'>");
		htmlForm.append("<div class='input-group'>");
		htmlForm.append("<div class='input-group-btn search-type-selection'>");
		htmlForm.append("<button type='button' class='btn btn-default dropdown-toggle' data-toggle='dropdown'><span class='search-type-selected'>Search By</span> <span class='caret'></span></button>");
		htmlForm.append("<ul class='dropdown-menu'>");
		
		/**
		 * Iterate for field selection.
		 */
		for(SearchableFieldMetamodel field : searchableModel.getSearchFields()) {
			String label = applicationContext.getMessage(field.getLabel(), new Object[] {}, field.getLabel(), Locale.ENGLISH);
			htmlForm.append("<li><a href='#'>" + label + "</a></li>");
		}
		
		htmlForm.append("</ul>");
		htmlForm.append("</div>");
		htmlForm.append("<input type='text' class='form-control' placeholder='Keyword' />");
		htmlForm.append("<span class='input-group-btn'>");
		htmlForm.append("<button class='btn btn-primary' type='submit'><span class='glyphicon glyphicon-search'></span></button>");
		htmlForm.append("</span>");
		htmlForm.append("</div>");
		htmlForm.append("</form>");
		
		getJspContext().getOut().write(htmlForm.toString());
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

	private ApplicationContext getApplicationContext() {
		PageContext pageContext = (PageContext) getJspContext();
		return WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
	}
}
