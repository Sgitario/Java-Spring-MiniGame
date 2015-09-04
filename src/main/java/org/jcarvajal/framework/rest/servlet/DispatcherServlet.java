package org.jcarvajal.framework.rest.servlet;

import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.ControllerManager;
import org.jcarvajal.framework.rest.servlet.controllers.annotations.AnnotationControllerManager;
import org.jcarvajal.framework.rest.servlet.injector.DependencyInjector;
import org.jcarvajal.framework.rest.servlet.injector.InjectorComponent;
import org.jcarvajal.framework.utils.ReflectionUtils;
import org.jcarvajal.framework.utils.StringUtils;

/**
 * Dispatch an incoming HTTP request.
 * @author JoseCH
 *
 */
public abstract class DispatcherServlet {

	private static final Logger LOG = Logger.getLogger(
			DispatcherServlet.class.getName());
	
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
	protected void initInjector(InjectorComponent injectorInterface) {
		if (injectorInterface != null 
				&& StringUtils.isNotEmpty(injectorInterface.getClassName())) {
			
			this.injector = ReflectionUtils.createInstanceSafely(injectorInterface.getClassName(), 
					injectorInterface.getParams(), DependencyInjector.class);
			if (this.injector != null) {
				this.injector.init();
			}
		}
	}
	
	/**
	 * Register the controller instance.
	 * @param controller
	 * @throws OnRestInitializationException 
	 * @throws OnRequestMappingInitializationException 
	 */
	protected void initControllers(List<String> controllers) {
		if (controllers != null) {
			for (String controllerClassName : controllers) {
				Object controller = getInjector().get(controllerClassName);
				if (controller != null) {
					try {
						if (controllerManager != null) {
							controllerManager.register(controller);
						} else {
							LOG.severe(String.format("Manager of controllers %s cannot be created. ",
									controllerClassName));
						}
					} catch (OnRequestMappingInitializationException e) {
						LOG.severe(String.format("Error registering request handler %s. Cause: %s",
								controllerClassName, e.getMessage()));
					}
				}
			}
		}
		
	}

}
