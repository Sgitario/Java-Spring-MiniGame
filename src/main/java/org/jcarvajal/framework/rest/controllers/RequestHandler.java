package org.jcarvajal.framework.rest.controllers;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.jcarvajal.framework.rest.controllers.marshallers.DefaultResponseMarshaller;
import org.jcarvajal.framework.rest.controllers.params.ParamResolver;
import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.models.RequestMethod;

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
	private List<ParamResolver> params;
	private ResponseMarshaller marshaller = new DefaultResponseMarshaller();
	
	/**
	 * Initializes a new instance of the RequestHandler class.
	 * @param controller
	 * @param method
	 * @throws OnRequestMappingInitializationException
	 */
	public RequestHandler(Object controller, Method method) 
			throws OnRequestMappingInitializationException {
		if (controller == null || method == null) {
			throw new OnRequestMappingInitializationException("Controller cannot be null!");
		}
		
		this.controller = controller;
		this.method = method;
	}
	
	/**
	 * @return get mapping url linked to the handler.
	 */
	protected abstract String getMappingUrl();
	
	/**
	 * @return get method type linked to the handler.
	 */
	protected abstract RequestMethod getRequestMethod();
	
	/**
	 * Initialize the regular expression that should match against the 
	 * incoming url.
	 * @param url
	 * @return
	 */
	protected abstract String initRegExpUrl(String url);
	
	/**
	 * Resolve the list of param that should apply to the handler.
	 * @param url
	 * @param method
	 * @return
	 * @throws OnRequestMappingInitializationException
	 */
	protected abstract List<ParamResolver> initParams(String url, Method method) 
			throws OnRequestMappingInitializationException;
	
	/**
	 * Set the marshaller to parse the response.
	 * @param marshaller
	 */
	protected void setMarshaller(ResponseMarshaller marshaller) {
		this.marshaller = marshaller;
	}

	/**
	 * Initialize the params and reg expression url.
	 * @throws OnRequestMappingInitializationException
	 */
	public final void init() throws OnRequestMappingInitializationException {
		this.params = initParams(getMappingUrl(), method);
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
				&& getRequestMethod().equals(method);
	}
	
	/**
	 * Resolve the params from the request url, then invoke the handler method 
	 * and finally marshall the response.
	 * 
	 * @param url
	 * @param responseBody
	 * @return
	 * @throws OnRequestException
	 */
	public final byte[] invoke(String url, OutputStream responseBody) throws OnRequestException {
		byte[] response = null;
		try {
			Object[] params = resolveParams(url, responseBody);
			
			Object result = this.method.invoke(controller, params);
			response = this.marshaller.marshall(result);
		} catch (Exception e) {
			throw new OnRequestException(e, "Error handling request in %s ", url);
		}
		
		return response;
	}	
	
	/**
	 * Resolve the params into the parameters of the request method.
	 * @param url
	 * @param responseBody
	 * @return
	 */
	private Object[] resolveParams(String url, OutputStream responseBody) {
		Object[] paramValues = new Object[params.size()];
		for (ParamResolver paramResolver : params) {
			paramValues[paramResolver.getPosition()] = paramResolver.resolve(url, responseBody);
		}
		
		return paramValues;
	}
}
