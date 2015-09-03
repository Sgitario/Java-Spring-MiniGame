package org.jcarvajal.framework.rest;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.config.Servlet;
import org.jcarvajal.framework.rest.config.WebConfiguration;
import org.jcarvajal.framework.rest.config.XmlWebConfiguration;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.server.ServerFacade;
import org.jcarvajal.framework.rest.server.ServerFactory;

/**
 * Start rest server by reading the servlet configuration
 * in web.xml.
 * 
 * @author JoseCH
 */
public class RestServer {
	
	private static final int DEFAULT_PORT = 8081;
	private static final Logger LOG = Logger.getLogger(
			RestServer.class.getName());
	
	private final int port;
	private ServerFacade server;
	private ServerFactory serverFactory = new ServerFactory();
	private WebConfiguration config = new XmlWebConfiguration();
	
	/**
	 * Initializes a new instace of the RestServer class using the default port.
	 * @throws OnRestInitializationException 
	 */
	public RestServer() throws OnRestInitializationException {
		this(DEFAULT_PORT);
	}
	
	/**
	 * Initializes a new instance of the RestServer class by specifying a port.
	 * @param port
	 * @throws OnRestInitializationException 
	 */
	public RestServer(int port) throws OnRestInitializationException {
		this.port = port;
		this.config.init();
	}
	
	public void setServerFactory(ServerFactory serverFactory) {
		this.serverFactory = serverFactory;
	}
	
	public void setConfig(WebConfiguration config) {
		this.config = config;
	}
	
	/**
	 * Start the server.
	 * @throws OnRestInitializationException 
	 */
	public boolean start() throws OnRestInitializationException {
		bindServer();
		bindContexts();
				
		this.server = serverFactory.get();
		
		if (this.server.isStarted()) {
			LOG.info(String.format("Server started at %s", new Date()));
			LOG.info(String.format("Server listening on %s", this.port));
		} else {
			LOG.warning("Server not started");
		}
		
		return this.server.isStarted();
	}
	
	/**
	 * Stop the server.
	 * @return 'true' if the server is stopped.
	 */
	public boolean stop() {
		boolean stopped = true;
		if (this.server != null) {
			stopped = this.server.stop();
		}
		
		return stopped;
	}
	
	private void bindServer() {
		serverFactory.bindServer(this.port);
	}
	
	private void bindContexts() throws OnRestInitializationException {
		Map<String, Servlet> servlets = this.config.getServlets();
		if (servlets != null) {
			for (Entry<String, Servlet> entry : servlets.entrySet()) {
				serverFactory.addContext(entry.getKey(), entry.getValue());
			}
		}
	}
}
