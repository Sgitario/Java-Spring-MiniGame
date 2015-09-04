package org.jcarvajal.framework.rest.config;

import java.util.Map;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.Servlet;

/**
 * This class will get the servlets. 
 * @author JoseCH
 *
 */
public interface WebConfiguration {
	
	/**
	 * Initialize the web configuration.
	 */
	public void init() throws OnRestInitializationException;

	/**
	 * @return The list of context and handlers.
	 */
	public Map<String, Servlet> getServlets();
}
