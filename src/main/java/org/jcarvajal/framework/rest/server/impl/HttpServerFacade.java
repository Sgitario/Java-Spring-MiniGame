package org.jcarvajal.framework.rest.server.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.server.ServerFacade;
import org.jcarvajal.framework.rest.servlet.DispatcherServlet;
import org.jcarvajal.framework.utils.StringUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

/**
 * Implementation of ServerFacade that uses com.sun.net.httpserver.HttpServer internally. 
 * @author jhilario
 *
 */
@SuppressWarnings("restriction")
public class HttpServerFacade implements ServerFacade {

	private static final Logger LOG = Logger.getLogger(
			HttpServerFacade.class.getName());
	
	private HttpServer server;
	private boolean started = false;
	
	/**
	 * Start the server.
	 */
	public boolean start(int port) {		
		// Start.
		try {
			startInternal(port);
			
			this.started = true;
		} catch (IOException e) {
			LOG.severe(
					String.format("Error starting server. Cause: %s", e.getMessage()));
		}
		
		return this.started;
	}

	/**
	 * "true" if the server is running.
	 */
	public boolean isStarted() {
		return this.started;
	}
	
	/**
	 * Register a context.
	 */
	public void createContext(String context, final DispatcherServlet handler) {
		this.server.createContext(context, new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				try {				
					byte[] response = handler.handle(exchange.getRequestURI(), 
							exchange.getRequestMethod(), exchange.getRequestBody());
					writeResponse(exchange, 200, response);
		            
				} catch (Exception ex) {
					writeResponse(exchange, 500, ex.getMessage());
				}
			}
			
		});
	}
	
	protected void startInternal(int port) throws IOException {
		this.server = HttpServer.create(new InetSocketAddress(port), 0);
		this.server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
		this.server.start();
	}
	
	private void writeResponse(HttpExchange exchange, int code, String message) {
		String response = StringUtils.EMPTY;
		if (StringUtils.isNotEmpty(message)) {
			response = message;
		}
		
		writeResponse(exchange, code, response.getBytes());
	}
	
	private void writeResponse(HttpExchange exchange, int code, byte[] response) {
		try {
			if (response != null) {
				exchange.sendResponseHeaders(code, response.length);
				OutputStream os = exchange.getResponseBody();
		        os.write(response);
		        os.close();
			} else {
				exchange.sendResponseHeaders(code, 0);
			}			
			
		} catch (IOException e) {
			LOG.severe("Error writing http response. Cause: " + e.getMessage());
		} finally {
			exchange.close();
		}
	}

}
