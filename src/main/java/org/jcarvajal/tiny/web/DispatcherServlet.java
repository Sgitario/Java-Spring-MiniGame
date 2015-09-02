package org.jcarvajal.tiny.web;

import java.io.OutputStream;
import java.net.URI;

import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.jcarvajal.tiny.injector.DependencyInjector;

/**
 * Dispatch an incoming HTTP request.
 * @author JoseCH
 *
 */
public abstract class DispatcherServlet {

	private DependencyInjector injector;
	
	/**
	 * Initialize the dispatcher.
	 * @throws OnInitConfigurationException
	 */
	public abstract void init() throws OnInitConfigurationException;
	
	/**
	 * Handle an incoming HTTP request and returns a response.
	 * @param requestURI
	 * @param requestMethod
	 * @param responseBody
	 * @return
	 */
	public abstract byte[] handle(URI requestURI, String requestMethod,
			OutputStream responseBody);
	
	public DependencyInjector getInjector() {
		return injector;
	}
	
	protected void initializeInjector(DependencyInjector injector) {
		this.injector = injector;
		this.injector.init();
	}

}
