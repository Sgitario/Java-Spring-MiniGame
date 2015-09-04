package org.jcarvajal.framework.rest.servlet.injector;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;

/**
 * Dependency injector interface with the rest api.
 * 
 * @author JoseCH
 *
 */
public interface DependencyInjector {
	public void init() throws OnRestInitializationException;
	
	public <T> T get(String className);
}
