package org.jcarvajal.framework.rest.servlet;

import java.io.OutputStream;
import java.net.URI;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;

/**
 * Dispatch an incoming HTTP request.
 * @author JoseCH
 *
 */
public abstract class DispatcherServlet {

	private DependencyInjector injector;
	
	/**
	 * Initialize the dispatcher.
	 * @throws OnRestInitializationException
	 */
	public abstract void init() throws OnRestInitializationException;
	
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
		if (this.injector != null) {
			this.injector.init();
		}
	}

}
