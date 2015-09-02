package org.jcarvajal.tiny.web;

import java.io.OutputStream;
import java.net.URI;

public interface DispatcherServlet {

	public void init();
	
	byte[] handle(URI requestURI, String requestMethod,
			OutputStream responseBody);

}
