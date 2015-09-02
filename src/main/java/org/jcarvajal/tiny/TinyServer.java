package org.jcarvajal.tiny;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.jcarvajal.tiny.config.Servlet;
import org.jcarvajal.tiny.config.WebConfiguration;
import org.jcarvajal.tiny.config.XmlWebConfiguration;
import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.jcarvajal.tiny.server.ServerFacade;
import org.jcarvajal.tiny.server.ServerFactory;

/**
 * Start tiny server by reading the servlet configuration
 * in web.xml.
 * 
 * @author JoseCH
 */
public class TinyServer {
	
	private static final int DEFAULT_PORT = 8081;
	private static final Logger LOG = Logger.getLogger(
			TinyServer.class.getName());
	
	private final int port;
	private ServerFacade server;
	private ServerFactory serverFactory = new ServerFactory();
	private WebConfiguration config = new XmlWebConfiguration();
	
	/**
	 * Initializes a new instace of the TinyServer class using the default port.
	 * @throws OnInitConfigurationException 
	 */
	public TinyServer() throws OnInitConfigurationException {
		this(DEFAULT_PORT);
	}
	
	/**
	 * Initializes a new instance of the TinyServer class by specifying a port.
	 * @param port
	 * @throws OnInitConfigurationException 
	 */
	public TinyServer(int port) throws OnInitConfigurationException {
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
	 */
	public boolean start() {
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
	
	private void bindContexts() {
		Map<String, Servlet> servlets = this.config.getServlets();
		if (servlets != null) {
			for (Entry<String, Servlet> entry : servlets.entrySet()) {
				serverFactory.addContext(entry.getKey(), entry.getValue());
			}
		}
	}
}
