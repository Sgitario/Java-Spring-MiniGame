package org.jcarvajal.framework.rest.servlet.controllers;

import java.lang.reflect.Method;

import org.jcarvajal.framework.rest.annotations.RequestMapping;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.AnnotationRequestHandler;

public class AnnotationControllerManager extends ControllerManager {
	
	/**
	 * Register the mapping handler into the controller manager base.
	 * @param method
	 * @throws OnRequestMappingInitializationException 
	 */
	@Override
	public void register(Object controller) throws OnRequestMappingInitializationException {
		if (controller != null) {
			// Loop over the methods with @RequestMapping
			Method[] methods = controller.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					registerMapping(
							new AnnotationRequestHandler(
									controller, 
									method,
									method.getAnnotation(RequestMapping.class)));
				}
			}
		}
		
	}
}
