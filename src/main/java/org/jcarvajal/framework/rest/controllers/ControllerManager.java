package org.jcarvajal.framework.rest.controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;

/**
 * List of mappings for all controllers.
 * The manager will register the controller and when the request is coming, it will loop over the mappings to handle it.
 * @author jhilario
 *
 */
public abstract class ControllerManager {

	private static final Logger LOG = Logger.getLogger(
			ControllerManager.class.getName());
	
	/**
	 * List of registered handlers.
	 */
	private List<RequestHandler> handlers = new ArrayList<RequestHandler>();
	
	/**
	 * Register a controller instance.
	 * This method will analyze the instance to resolve the handler properties
	 * depending to the implementation of this manager. 
	 * @param controller
	 * @throws OnRequestMappingInitializationException
	 */
	public abstract void register(Object controller) throws OnRequestMappingInitializationException;
	
	/**
	 * Check whether the request can be handled by the controller manager and if so, invoke the handler.
	 * @param url
	 * @param method
	 * @param responseBody
	 * @return
	 * @throws OnRequestException
	 */
	public final byte[] handle(String url, String method, InputStream requestBody) throws OnRequestException {
		for (RequestHandler handler : handlers) {
			if (handler.satisfy(url, method)) {
				return handler.invoke(url, requestBody);
			}
		}
		
		LOG.warning(String.format("No handler found for %s:%s", method, url));
		
		return null;
	}
	
	/**
	 * Finally add the mapping to the handler list.
	 * @param handler
	 * @throws OnRequestMappingInitializationException
	 */
	protected final void initMapping(RequestHandler handler) 
			throws OnRequestMappingInitializationException {
		handler.init();
		handlers.add(handler);
	}
}
