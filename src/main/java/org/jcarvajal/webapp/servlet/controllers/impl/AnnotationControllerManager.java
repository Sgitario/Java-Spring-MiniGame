package org.jcarvajal.webapp.servlet.controllers.impl;

import java.lang.reflect.Method;

import org.jcarvajal.webapp.servlet.controllers.ControllerManager;
import org.jcarvajal.webapp.servlet.controllers.handlers.AnnotationRequestHandler;
import org.jcarvajal.webapp.servlet.controllers.handlers.RequestHandler;

/**
 * Register all mappings of the controller based on their annotations.
 * @author jhilario
 *
 */
public class AnnotationControllerManager extends ControllerManager {
	
	/**
	 * Register the mapping handler into the controller manager base.
	 * @param method
	 * @throws OnRequestMappingInitializationException 
	 */
	@Override
	public RequestHandler register(Object controller) {
		RequestHandler handler = null;
		
		if (controller != null) {
			// Loop over the methods with @RequestMapping
			Method[] methods = controller.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					
					handler = new AnnotationRequestHandler(
							controller, 
							method);
					
					initMapping(handler);
				}
			}
		}
		
		return handler;
		
	}
}
