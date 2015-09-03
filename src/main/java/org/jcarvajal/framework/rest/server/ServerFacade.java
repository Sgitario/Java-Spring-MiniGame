package org.jcarvajal.framework.rest.server;

import org.jcarvajal.framework.rest.servlet.DispatcherServlet;

/**
 * Server interface to work with RestServer.
 * @author JoseCH
 *
 */
public interface ServerFacade {
	/**
	 * Start the server on the port number specified.
	 * @param port
	 * @return
	 */
	public boolean start(int port);
	
	/**
	 * Stop the server.
	 * @return
	 */
	public boolean stop();
	
	/**
	 * @return 'true' if the server is listening. Otherwise, 'false'.
	 */
	public boolean isStarted();

	/**
	 * Register a new handler within the specified context.
	 * @param context
	 * @param handler
	 */
	public void createContext(String context, DispatcherServlet handler);
}
