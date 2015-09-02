package org.jcarvajal.tiny.server;

import java.util.logging.Logger;

import org.jcarvajal.tiny.config.Servlet;
import org.jcarvajal.tiny.web.DispatcherServlet;
import org.jcarvajal.utils.ReflectionUtils;

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
	public ServerFactory bindServer(int port) {
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
	 * Configure the context to be handled by the specified class name.
	 * 
	 * The class name is initialized using Reflection.
	 * @param key
	 * @param className
	 */
	public ServerFactory addContext(String context, Servlet servlet) {
		
		try {
			DispatcherServlet handler = createDispatcher(servlet);
			handler.init();
			serverFacade.createContext(context, handler);
		} catch (Exception ex) {
			LOG.severe("Error creating handler. Cause: " + ex.getMessage());
		}
		
		return this;
	}

	private DispatcherServlet createDispatcher(Servlet servlet) {
		DispatcherServlet dispatcher = ReflectionUtils.createInstance(
				servlet.getClassName(), DispatcherServlet.class,
				servlet.getParams());
		if (dispatcher == null) {
			LOG.severe("Servlet is not a Tiny dispatcher! Ignoring...");
		}
		
		return dispatcher;
	}
}
