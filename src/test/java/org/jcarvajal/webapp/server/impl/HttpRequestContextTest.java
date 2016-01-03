package org.jcarvajal.webapp.server.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class HttpRequestContextTest {
	private HttpRequestContext context;
	
	private HttpExchange mockHttpExchange;
	
	private String actualPrincipal;
	private String actualCookie;
	
	@Before
	public void setup() {
		mockHttpExchange = Mockito.mock(HttpExchange.class);
		
		context = new HttpRequestContext(mockHttpExchange);
	}
	
	@Test
	public void getRequestURI_whenHttpExchange_thenGetExpected() {		
		whenGetRequestURI();
		thenGetRequestURIExchangeInvoked();
	}
	
	@Test
	public void getRequestMethod_whenHttpExchange_thenGetExpected() {		
		whenGetRequestMethod();
		thenGetRequestMethodExchangeInvoked();
	}
	
	@Test
	public void getRequestBody_whenHttpExchange_thenGetExpected() {		
		whenGetRequestBody();
		thenGetRequestBodyExchangeInvoked();
	}
	
	@Test
	public void getPrincipal_whenNotSet_thenNull() {
		whenGetPrincipal();
		thenPrincipalIsNull();
	}
	
	@Test
	public void getPrincipal_whenNotSet_thenExpected() {
		whenSetPrincipal("test1", "role1");
		whenGetPrincipal();
		thenPrincipalIs("test1");
		thenUserRoleIs("role1");
	}
	
	@Test
	public void contextWithCookies_thenCookieFound() {
		givenContextWithCookies("cookie1=val1");
		whenGetCookie("cookie1");
		thenCookieIs("val1");
	}
	
	@Test
	public void clearCookie_thenCookieDeleted() {
		givenContextWithCookies("cookie1=val1");
		whenClearCookie("cookie1");
		whenGetCookie("cookie1");
		thenCookieIs("");
	}
	
	private void givenContextWithCookies(String cookies) {
		Headers headers = Mockito.mock(Headers.class);
		Mockito.when(headers.getFirst("Cookie")).thenReturn(cookies);
		Mockito.when(mockHttpExchange.getRequestHeaders()).thenReturn(headers);
		Mockito.when(mockHttpExchange.getResponseHeaders()).thenReturn(headers);
		
		context = new HttpRequestContext(mockHttpExchange);
	}
	
	private void whenGetCookie(String key) {
		actualCookie = context.getCookie(key);
	}
	
	private void whenClearCookie(String key) {
		context.clearCookie(key);
	}
	
	private void whenGetPrincipal() {
		actualPrincipal = context.getPrincipal();
	}
	
	private void whenSetPrincipal(String username, String role) {
		context.setPrincipal(username, role);
	}
	
	private void whenGetRequestURI() {
		context.getRequestURI();
	}
	
	private void whenGetRequestMethod() {
		context.getRequestMethod();
	}
	
	private void whenGetRequestBody() {
		context.getRequestBody();
	}
	
	private void thenGetRequestURIExchangeInvoked() {
		Mockito.verify(mockHttpExchange, Mockito.times(1)).getRequestURI();
	}
	
	private void thenGetRequestMethodExchangeInvoked() {
		Mockito.verify(mockHttpExchange, Mockito.times(1)).getRequestMethod();
	}
	
	private void thenGetRequestBodyExchangeInvoked() {
		Mockito.verify(mockHttpExchange, Mockito.times(1)).getRequestBody();
	}
	
	private void thenPrincipalIsNull() {
		assertNull(actualPrincipal);
	}
	
	private void thenPrincipalIs(String expected) {
		assertEquals(expected, actualPrincipal);
	}
	
	private void thenUserRoleIs(String userRole) {
		assertEquals(userRole, context.getUserRole());
	}
	
	private void thenCookieIs(String expected) {
		assertEquals(expected, actualCookie);
	}
}
