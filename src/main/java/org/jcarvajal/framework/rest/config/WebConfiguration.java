package org.jcarvajal.framework.rest.config;

import java.util.Map;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;

/**
 * This class will read the servlet configuration. 
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
