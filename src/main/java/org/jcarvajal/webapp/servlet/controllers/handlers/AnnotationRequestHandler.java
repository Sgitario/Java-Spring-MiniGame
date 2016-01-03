package org.jcarvajal.webapp.servlet.controllers.handlers;

import java.lang.reflect.Method;

import org.jcarvajal.webapp.server.RequestMethod;
import org.jcarvajal.webapp.servlet.controllers.impl.RequestMapping;
import org.jcarvajal.webapp.servlet.controllers.impl.Role;

/**
 * Implementation of RequestHandler to define a handler using annotations.
 * Example:
 * @RequestMapping(url="/test"")
 * public String test() {
 * 	...
 * }
 * 
 * The handler method "test" will satisfy an incoming request "/test" 
 * with method "GET".
 * 
 * @author JoseCH
 *
 */
public class AnnotationRequestHandler extends RequestHandler {

	private final RequestMapping requestMapping;
	private final Role role;
	
	/**
	 * Initializes a new instance of the AnnotationRequestHandler class.
	 * @param controller
	 * @param method
	 * @param requestMapping
	 * @throws OnRequestMappingInitializationException
	 */
	public AnnotationRequestHandler(Object controller, Method method) {
		super(controller, method);
		
		this.requestMapping = method.getAnnotation(RequestMapping.class);
		this.role = method.getAnnotation(Role.class);
	}
	
	@Override
	public String getMappingUrl() {
		return requestMapping.url();
	}
	
	@Override
	public RequestMethod getMethod() {
		return requestMapping.method();
	}
	
	@Override
	public boolean requiresRole() {
		return role != null;
	}
	
	@Override
	public String getRequiredRole() {
		return role.name();
	}
	
	protected String initRegExpUrl(String url) {		
		return "^" + url + "(\\?.*)?";
	}

}
