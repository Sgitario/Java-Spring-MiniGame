package org.jcarvajal.framework.rest.servlet;

import java.util.Map;

/**
 * Model interface to register servlets via ServerFactory.
 * 
 * @author jhilario
 *
 */
public class Servlet {
	private final String name;
	private final String className;
	private final Map<String, String> params;
	
	/**
	 * Initializes a new instance of the Servlet class.
	 * @param servletName
	 * @param servletClass
	 * @param params
	 */
	public Servlet(String servletName, String servletClass,
			Map<String, String> params) {
		this.name = servletName;
		this.className = servletClass;
		this.params = params;
	}

	public String getName() {
		return name;
	}
	
	public String getClassName() {
		return className;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
}
