package org.jcarvajal.webapp.server;

/**
 * Request method type.
 * Only supports GET and POST.
 * @author JoseCH
 *
 */
public enum RequestMethod {
	GET("get"), POST("post");
	
	private final String method;
	
	RequestMethod(String method) {
		this.method = method;
	}
	
	public boolean equals(String method) {
		return this.method.equalsIgnoreCase(method);
	}
}
