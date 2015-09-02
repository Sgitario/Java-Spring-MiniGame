package org.jcarvajal.tiny.config;

import java.util.Map;

import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;

/**
 * This class will read the servlet configuration. 
 * @author JoseCH
 *
 */
public interface WebConfiguration {
	
	/**
	 * Initialize the web configuration.
	 */
	public void init() throws OnInitConfigurationException;

	/**
	 * @return The list of context and handlers.
	 */
	public Map<String, Servlet> getServlets();
}
