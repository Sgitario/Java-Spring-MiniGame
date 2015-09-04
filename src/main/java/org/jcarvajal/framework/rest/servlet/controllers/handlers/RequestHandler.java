package org.jcarvajal.framework.rest.servlet.controllers.handlers;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.RequestMethod;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.params.ParamResolver;

public abstract class RequestHandler {
	
	private final Object controller;
	private final Method method;
	private String regExpUrl;
	private List<ParamResolver> params;
	private ResponseMarshaller marshaller = new DefaultResponseMarshaller();
	
	public RequestHandler(Object controller, Method method) 
			throws OnRequestMappingInitializationException {
		if (controller == null || method == null) {
			throw new OnRequestMappingInitializationException("Controller cannot be null!");
		}
		
		this.controller = controller;
		this.method = method;
	}
	
	protected abstract String getMappingUrl();
	protected abstract RequestMethod getRequestMethod();
	protected abstract String initRegExpUrl(String url);
	protected abstract List<ParamResolver> initParams(String url, Method method) 
			throws OnRequestMappingInitializationException;

	public void init() throws OnRequestMappingInitializationException {
		this.params = initParams(getMappingUrl(), method);
		this.regExpUrl = initRegExpUrl(getMappingUrl());
	}
	
	public boolean satisfy(String url, String method) {
		return url != null 
				&& url.matches(regExpUrl) 
				&& getRequestMethod().equals(method);
	}
	
	public byte[] invoke(String url, OutputStream responseBody) throws OnRequestException {
		byte[] response = null;
		try {
			Object[] params = matchParams(url, responseBody);
			
			Object result = this.method.invoke(controller, params);
			response = this.marshaller.marshall(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new OnRequestException("Error handling request. ", e);
		}
		
		return response;
	}
	
	protected void setMarshaller(ResponseMarshaller marshaller) {
		this.marshaller = marshaller;
	}
	
	private Object[] matchParams(String url, OutputStream responseBody) {
		Object[] paramValues = new Object[params.size()];
		for (ParamResolver paramResolver : params) {
			paramValues[paramResolver.getPosition()] = paramResolver.resolve(url, responseBody);
		}
		
		return paramValues;
	}
}
