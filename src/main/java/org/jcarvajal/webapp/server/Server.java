package org.jcarvajal.webapp.server;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import org.jcarvajal.webapp.filters.Filter;
import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.ServerFacade;
import org.jcarvajal.webapp.server.impl.HttpServerFacade;
import org.jcarvajal.webapp.servlet.DispatcherServlet;

/**
 * Manage a server by:
 * - Creating a new instance.
 * - Registering the controllers.
 * - Registering servlets.
 * 
 * @author JoseCH
 */
public class Server implements Observer {
	
	private static final int DEFAULT_PORT = 8080;
	private static final Logger LOG = Logger.getLogger(
			Server.class.getName());
	
	private final int port;
	private ServerFacade serverInternal;
	private DispatcherServlet servlet;
	private List<Object> controllers = new LinkedList<Object>();
	private List<Filter> filters = new LinkedList<Filter>();
	
	/**
	 * Initializes a new instace of the Server class using the default impl.
	 */
	public Server() {
		this(new HttpServerFacade(), DEFAULT_PORT);
	}
	
	/**
	 * Initializes a new instance of the Server class by specifying a port.
	 * @param port
	 */
	public Server(ServerFacade server, int port) {
		this.serverInternal = server;
		this.port = port;
	}
	
	/**
	 * Register a new controller into the server.
	 * @param controller
	 */
	public void addController(Object controller) {
		this.controllers.add(controller);
	}
	
	/**
	 * Add filter to be run before a request.
	 * @param filter
	 */
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}
	
	/**
	 * Register a new context.
	 * @param context
	 * @param servlet
	 */
	public void setServlet(DispatcherServlet servlet) {
		this.servlet = servlet;
	}
	
	/**
	 * Start the server.
	 */
	public boolean start() {
		serverInternal.start(port);
		
		if (serverInternal.isStarted()) {
			initServlet();
			
			LOG.info(String.format("Server started at %s", new Date()));
			LOG.info(String.format("Server listening on %s", this.port));
		} else {
			LOG.warning("Server not started");
		}
		
		return serverInternal.isStarted();
	}

	/**
	 * Stop the server.
	 */
	public void stop() {
		if (serverInternal != null) {
			serverInternal.stop();
			serverInternal = null;
		}
	}
	
	/**
	 * Initialize servlet and register the controllers.
	 */
	private void initServlet() {
		if (this.servlet == null) {
			throw new RuntimeException("Servlet is null.");
		}
		
		this.servlet.addObserver(this);
		
		this.serverInternal.createContext("/", this.servlet);
		this.servlet.initControllers(controllers);
	}
	
	/**
	 * Dispatcher that will handle the request.
	 * arg param contain the RequestContextProxy object.
	 */
	public void update(Observable o, Object arg) {
		if (arg instanceof RequestContext) {
			RequestContext request = (RequestContext) arg;
			for (Filter filter : filters) {
				filter.doFilter(request);
			}
		}
	}
}
