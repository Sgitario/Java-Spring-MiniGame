package org.jcarvajal.webapp.server.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jcarvajal.webapp.utils.StringUtils;
import org.jcarvajal.webapp.server.RequestContext;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

/**
 * RequestContext implementation using com.sun.httpExchange instance.
 * @author JoseCH
 *
 */
public class HttpRequestContext implements RequestContext {

	private static final String COOKIE_FIELD_SEP = "=";
	private static final String COOKIE_SEP = ";";
	
	private final HttpExchange exchange;
	private String username;
	private String role;
	private Map<String, String> cookies = new HashMap<String, String>();
	
	public HttpRequestContext(HttpExchange exchange) {
		this.exchange = exchange;
		
		parseCookies();
	}

	public URI getRequestURI() {
		return exchange.getRequestURI();
	}

	public String getRequestMethod() {
		return exchange.getRequestMethod();
	}

	public InputStream getRequestBody() {
		return exchange.getRequestBody();
	}
	
	public String getPrincipal() {
		return username;
	}
	
	public void setPrincipal(String username, String role) {
		this.username = username;
		this.role = role;
	}
	
	public String getUserRole() {
		return role;
	}
	
	public String getCookie(String key) {		
		return cookies.get(key);
	}
	
	public String clearCookie(String key) {
		String value = getCookie(key);
		setCookie(key, "");
		updateCookies();
		
		return value;
	}
	
	public void setCookie(String key, String value) {
		cookies.put(key, value);
		
		updateCookies();
	}
	
	private void updateCookies() {
		List<String> internalCookies = new LinkedList<String>();
		for(Entry<String, String> cookie : cookies.entrySet()) {
			internalCookies.add(cookie.getKey() + COOKIE_FIELD_SEP + cookie.getValue());
		}
		
		exchange.getResponseHeaders().put("Set-Cookie", internalCookies);
	}

	private void parseCookies() {
		Headers rmap = exchange.getRequestHeaders();
		if (rmap != null) {
			String cookieString = rmap.getFirst("Cookie");
	        
	        cookies = StringUtils.map(cookieString, COOKIE_SEP, COOKIE_FIELD_SEP);
		}
	}
}
