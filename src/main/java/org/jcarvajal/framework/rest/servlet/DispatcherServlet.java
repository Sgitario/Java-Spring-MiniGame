package org.jcarvajal.framework.rest.servlet;

import java.io.OutputStream;
import java.net.URI;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.injector.DependencyInjector;
import org.jcarvajal.framework.rest.servlet.controllers.AnnotationControllerManager;
import org.jcarvajal.framework.rest.servlet.controllers.ControllerManager;

/**
 * Dispatch an incoming HTTP request.
 * @author JoseCH
 *
 */
public abstract class DispatcherServlet {

	private DependencyInjector injector;
	
	/**
	 * Annotation based by default.
	 */
	private ControllerManager controllerManager = new AnnotationControllerManager();
	
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
	 * @throws OnRequestException 
	 */
	public byte[] handle(URI requestURI, String requestMethod,
			OutputStream responseBody) throws OnRequestException {
		return controllerManager.handle(requestURI.toString(), 
				requestMethod, responseBody);
	}
	
	protected DependencyInjector getInjector() {
		return injector;
	}
	
	/**
	 * Initialize the injector.
	 * @param injector
	 * @throws Exception 
	 */
	protected void initializeInjector(DependencyInjector injector) {
		this.injector = injector;
		if (this.injector != null) {
			this.injector.init();
		}
	}
	
	/**
	 * Register the controller instance.
	 * @param controller
	 * @throws OnRestInitializationException 
	 * @throws OnRequestMappingInitializationException 
	 */
	protected void registerController(Object controller) 
			throws OnRestInitializationException, OnRequestMappingInitializationException {
		if (controllerManager == null) {
			throw new OnRestInitializationException("Manager of controllers is null");
		}
		
		controllerManager.register(controller);
	}

}
