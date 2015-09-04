package org.jcarvajal.framework.rest.servlet.controllers;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.servlet.controllers.handlers.RequestHandler;

/**
 * List of mappings for all controllers.
 * The manager will register the controller and when the request is coming, it will loop over the mappings to handle it.
 * @author jhilario
 *
 */
public abstract class ControllerManager {

	private static final Logger LOG = Logger.getLogger(
			ControllerManager.class.getName());
	
	private List<RequestHandler> handlers = new ArrayList<RequestHandler>();
	
	public abstract void register(Object controller) throws OnRequestMappingInitializationException;
	
	/**
	 * Check whether the request can be handled by the controller manager and if so, invoke the handler.
	 * @param url
	 * @param method
	 * @param responseBody
	 * @return
	 * @throws OnRequestException
	 */
	public byte[] handle(String url, String method, OutputStream responseBody) throws OnRequestException {
		for (RequestHandler handler : handlers) {
			if (handler.satisfy(url, method)) {
				return handler.invoke(url, responseBody);
			}
		}
		
		LOG.warning(String.format("No handler found for %s:%s", method, url));
		
		return null;
	}
	
	protected void initMapping(RequestHandler handler) 
			throws OnRequestMappingInitializationException {
		handler.init();
		handlers.add(handler);
	}
}
