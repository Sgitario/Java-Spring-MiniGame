package org.jcarvajal.framework.rest.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.jcarvajal.framework.rest.servlet.DispatcherServlet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

public class HttpServerFacade implements ServerFacade {

	private static final Logger LOG = Logger.getLogger(
			HttpServerFacade.class.getName());
	private static final int DELAY_TO_STOP = 100;
	
	private HttpServer server;
	private boolean started = false;
	
	public synchronized boolean start(int port) {
		// If started, stop it.
		stop();
		
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

	public boolean stop() {
		// If started stop it.
		if (isStarted()) {
			stopInternal();
			this.server = null;
		}
		
		this.started = false;
		
		return !this.started;
	}

	public boolean isStarted() {
		return this.started;
	}
	
	public void createContext(String context, final DispatcherServlet handler) {
		this.server.createContext(context, new HttpHandler() {

			public void handle(HttpExchange exchange) throws IOException {
				try {					
					byte[] response = handler.handle(exchange.getRequestURI(), 
							exchange.getRequestMethod(), exchange.getResponseBody());
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
	
	protected void stopInternal() {
		if (this.server != null) {
			this.server.stop(DELAY_TO_STOP);
		}
	}
	
	private void writeResponse(HttpExchange exchange, int code, String message) {
		writeResponse(exchange, code, message.getBytes());
	}
	
	private void writeResponse(HttpExchange exchange, int code, byte[] response) {
		try {
			exchange.sendResponseHeaders(code, response.length);
			OutputStream os = exchange.getResponseBody();
	        os.write(response);
	        os.close();
		} catch (IOException e) {
			LOG.severe("Error writing http response. Cause: " + e.getMessage());
		} finally {
			exchange.close();
		}
	}

}
