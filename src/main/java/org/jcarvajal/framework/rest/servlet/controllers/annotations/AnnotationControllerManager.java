package org.jcarvajal.framework.rest.servlet.controllers.annotations;

import java.lang.reflect.Method;

import org.jcarvajal.framework.rest.servlet.controllers.ControllerManager;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;

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
	public void register(Object controller) throws OnRequestMappingInitializationException {
		if (controller != null) {
			// Loop over the methods with @RequestMapping
			Method[] methods = controller.getClass().getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(RequestMapping.class)) {
					initMapping(
							new AnnotationRequestHandler(
									controller, 
									method,
									method.getAnnotation(RequestMapping.class)));
				}
			}
		}
		
	}
}
