package org.jcarvajal.webapp.server;

import java.io.InputStream;
import java.net.URI;

/**
 * Context with request scope.
 * 
 * @author JoseCH
 */
public interface RequestContext {
	public URI getRequestURI();
	public String getRequestMethod();
	public InputStream getRequestBody();
	
	public String getPrincipal();
	public void setPrincipal(String username, String role);
	public String getUserRole();
	
	public void setCookie(String key, String value);
	public String getCookie(String string);
	public String clearCookie(String redirect);
}
