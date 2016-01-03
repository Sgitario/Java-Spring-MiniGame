package org.jcarvajal.webapp.server.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.Response;
import org.jcarvajal.webapp.server.ServerFacade;
import org.jcarvajal.webapp.servlet.DispatcherServlet;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Implementation of ServerFacade that uses com.sun.net.httpserver.HttpServer internally. 
 * @author jhilario
 *
 */
@SuppressWarnings("restriction")
public class HttpServerFacade implements ServerFacade {

	private static final int DELAY_TO_STOP = 1;
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
	 * Stop the server.
	 */
	public void stop() {
		if (this.started) {
			this.server.stop(DELAY_TO_STOP);
			this.started = false;
		}
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
					RequestContext context = new HttpRequestContext(exchange);
					
					Response response = handler.handle(context);
					if (response.getRedirect() != null) {
						redirectTo(exchange, response.getRedirect());
					} else {
						writeResponse(exchange, response.getCode(), response.getResponse());
					}
		            
				} catch (Exception ex) {
					writeResponse(exchange, 500, ex.toString());
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
		writeResponse(exchange, code, message.getBytes());
	}
	
	private void writeResponse(HttpExchange exchange, int code, byte[] response) {
		try {
			prepareHeaders(exchange, code, response);
			if (response != null) {
				OutputStream os = exchange.getResponseBody();
		        os.write(response);
		        os.close();
			}
			
		} catch (IOException e) {
			LOG.severe("Error writing http response. Cause: " + e.getMessage());
		} finally {
			exchange.close();
		}
	}

	private void prepareHeaders(HttpExchange exchange, int code, byte[] response) 
			throws IOException {
		Headers headers = exchange.getResponseHeaders();
		headers.add("Content-Type", "text/html");
		int size = 0;
		if (response != null) {
			size = response.length;
		}
		
		exchange.sendResponseHeaders(code, size);
	}
	
	private void redirectTo(HttpExchange exchange, String redirect) throws IOException {
		Headers headers = exchange.getResponseHeaders();
		headers.add("Content-Type", "text/html");
		exchange.getResponseHeaders().set("Location", redirect); 
		exchange.sendResponseHeaders(302, 1);
	}

}
