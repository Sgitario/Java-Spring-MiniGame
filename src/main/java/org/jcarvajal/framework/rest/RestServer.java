package org.jcarvajal.framework.rest;

import java.util.Date;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.config.WebConfiguration;
import org.jcarvajal.framework.rest.config.impl.XmlWebConfiguration;
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
	private final ServerFactory serverFactory;
	private final WebConfiguration config;
	private ServerFacade currentInstance;
	
	/**
	 * Initializes a new instace of the RestServer class using the default impl.
	 * @throws OnRestInitializationException 
	 */
	public RestServer() throws OnRestInitializationException {
		this(new ServerFactory(), new XmlWebConfiguration(), DEFAULT_PORT);
	}
	
	/**
	 * Initializes a new instance of the RestServer class by specifying a port.
	 * @param port
	 * @throws OnRestInitializationException 
	 */
	public RestServer(ServerFactory serverFactory, WebConfiguration config, int port) 
			throws OnRestInitializationException {
		this.serverFactory = serverFactory;
		this.port = port;
		this.config = config;
		this.config.init();
	}
	
	/**
	 * Start the server.
	 * @throws OnRestInitializationException 
	 */
	public boolean start() throws OnRestInitializationException {
				
		currentInstance = serverFactory
				.startServer(port)
				.addContext(config.getServlets())
				.get();
		
		if (currentInstance.isStarted()) {
			LOG.info(String.format("Server started at %s", new Date()));
			LOG.info(String.format("Server listening on %s", this.port));
		} else {
			LOG.warning("Server not started");
		}
		
		return currentInstance.isStarted();
	}

	/**
	 * Stop the server.
	 */
	public void stop() {
		if (currentInstance != null) {
			currentInstance.stop();
			currentInstance = null;
		}
	}
}
