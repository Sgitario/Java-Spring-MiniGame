package org.jcarvajal.framework.rest.servlet.controllers;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.RequestHandler;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.ResponseMarshaller;

public abstract class ControllerManager {

	private static final Logger LOG = Logger.getLogger(
			ControllerManager.class.getName());
	
	private List<RequestHandler> handlers = new ArrayList<RequestHandler>();
	
	public abstract void register(Object controller) throws OnRequestMappingInitializationException;
	
	public byte[] handle(String url, String method, OutputStream responseBody) throws OnRequestException {
		for (RequestHandler handler : handlers) {
			if (handler.satisfy(url, method)) {
				return handler.invoke(url, responseBody);
			}
		}
		
		LOG.warning(String.format("No handler found for %s:%s", method, url));
		
		return null;
	}
	
	protected void registerMapping(RequestHandler handler) 
			throws OnRequestMappingInitializationException {
		handler.init();
		handlers.add(handler);
	}
	
	protected void registerMapping(RequestHandler handler, ResponseMarshaller response) {
		handlers.add(handler);
	}
}
