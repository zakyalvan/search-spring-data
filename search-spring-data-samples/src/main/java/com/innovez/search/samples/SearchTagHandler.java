package com.innovez.search.samples;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

public class SearchTagHandler extends SimpleTagSupport {
	private Logger logger = Logger.getLogger(SearchTagHandler.class);
	
	@Override
	protected JspContext getJspContext() {
		return super.getJspContext();
	}

	@Override
	protected JspFragment getJspBody() {
		return super.getJspBody();
	}
	
}
