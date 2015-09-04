package org.jcarvajal.framework.rest.server;

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.server.impl.HttpServerFacade;
import org.jcarvajal.framework.rest.servlet.DispatcherServlet;
import org.jcarvajal.framework.rest.servlet.Servlet;
import org.jcarvajal.framework.utils.ReflectionUtils;

/**
 * Factory to isolate the build of Server instances.
 * 
 * @author JoseCH
 */
public class ServerFactory {
	
	private static final Logger LOG = Logger.getLogger(
			ServerFactory.class.getName());
	
	/**
	 * By default, a Http Server instance.
	 */
	private ServerFacade serverFacade = new HttpServerFacade();
	
	/**
	 * Set another instance of server facade.
	 * @param serverFacade
	 */
	public ServerFactory setFacade(ServerFacade serverFacade) {
		this.serverFacade = serverFacade;
		
		return this;
	}
	
	/**
	 * Bind the server to a port number.
	 * @param port
	 * @return
	 */
	public ServerFactory startServer(int port) {
		serverFacade.start(port);
		
		return this;
	}

	/**
	 * @return the server instance.
	 */
	public ServerFacade get() {
		return serverFacade;
	}
	
	/**
	 * Configure a list of servelts.
	 * @param servlets
	 * @return
	 * @throws OnRestInitializationException
	 */
	public ServerFactory addContext(Map<String, Servlet> servlets) 
			throws OnRestInitializationException {
		if (servlets != null) {
			for (Entry<String, Servlet> entry : servlets.entrySet()) {
				addContext(entry.getKey(), entry.getValue());
			}
		}
		
		return this;
	}

	/**
	 * Configure the context to be handled by the specified class name.
	 * 
	 * The class name is initialized using Reflection.
	 * @param key
	 * @param className
	 * @throws OnRestInitializationException 
	 */
	public ServerFactory addContext(String context, Servlet servlet) throws OnRestInitializationException {
		
		DispatcherServlet handler = createDispatcher(servlet);
		handler.init();
		serverFacade.createContext(context, handler);
		
		return this;
	}

	private DispatcherServlet createDispatcher(Servlet servlet) {
		DispatcherServlet dispatcher = ReflectionUtils.createInstanceSafely(
				servlet.getClassName(), servlet.getParams(), DispatcherServlet.class);
		if (dispatcher == null) {
			LOG.severe("Servlet is not a dispatcher! Ignoring...");
		}
		
		return dispatcher;
	}
}
