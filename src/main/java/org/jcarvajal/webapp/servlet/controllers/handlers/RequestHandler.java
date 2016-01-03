package org.jcarvajal.webapp.servlet.controllers.handlers;

import java.lang.reflect.Method;

import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.RequestMethod;

/**
 * This class will invoke the method to handle a request
 * and will marshall the response.
 * @author JoseCH
 *
 */
public abstract class RequestHandler {
	/**
	 * Controller that will handle the request.
	 */
	private final Object controller;
	
	/**
	 * Method to invoke.
	 */
	private final Method method;
	/**
	 * Regular expression that should match 
	 */
	private String regExpUrl;
	
	/**
	 * Initializes a new instance of the RequestHandler class.
	 * @param controller
	 * @param method
	 */
	public RequestHandler(Object controller, Method method) {
		this.controller = controller;
		this.method = method;
	}
	
	/**
	 * @return get mapping url linked to the handler.
	 */
	public abstract String getMappingUrl();
	
	/**
	 * @return request method.
	 */
	public abstract RequestMethod getMethod();
	
	public abstract boolean requiresRole();
	
	public abstract String getRequiredRole();
	
	/**
	 * Initialize the regular expression that should match against the 
	 * incoming url.
	 * @param url
	 * @return
	 */
	protected abstract String initRegExpUrl(String url);

	/**
	 * Initialize the params and reg expression url.
	 */
	public final void init() {
		this.regExpUrl = initRegExpUrl(getMappingUrl());
	}
	
	/**
	 * Check whether the incoming url match to this handler.
	 * The incoming url will match if and only if:
	 * - The regular expression url matches the incoming request url.
	 * - The declared method type matches the incoming declared method type.
	 * 
	 * @param url
	 * @param method
	 * @return
	 */
	public final boolean satisfy(String url, String method) {
		return url != null 
				&& url.matches(regExpUrl)
				&& getMethod().equals(method);
	}
	
	/**
	 * Resolve the params from the request url, then invoke the handler method 
	 * and finally marshall the response.
	 * 
	 * @return
	 * @throws OnRequestException
	 */
	public Object invoke(RequestContext request) {		
		try {
			return this.method.invoke(controller, request);
			
		} catch (Exception e) {
			throw new RuntimeException("Error handling request in " + request.getRequestURI(), e);
		}
	}
}
