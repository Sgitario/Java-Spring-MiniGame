package org.jcarvajal.webapp.servlet;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.server.Response;
import org.jcarvajal.webapp.servlet.controllers.ControllerManager;
import org.jcarvajal.webapp.servlet.controllers.handlers.RequestHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DispatcherServletTest {
	private static final String LOGIN_REDIRECT = "toLogin";
	private static final String REQUIRED_ROLE = "role";
	
	private DispatcherServlet servlet;
	
	private RequestContext mockRequestContext;
	private ControllerManager mockControllerManager;
	private RequestHandler mockRequestHandler;
	
	private List<Object> controllers;
	private Response actual;
	private boolean handleResponseInvoked;
	
	@Before
	public void setup() {
		handleResponseInvoked = false;
		mockRequestContext = Mockito.mock(RequestContext.class);
		Mockito.when(mockRequestContext.getRequestURI()).thenReturn(URI.create("something"));
		mockControllerManager = Mockito.mock(ControllerManager.class);
		
		servlet = new DispatcherServlet() {

			@Override
			protected Response handleResponse(Object response) throws Exception {
				handleResponseInvoked = true;
				return null;
			}
			
		}.setControllerManager(mockControllerManager)
		 .setRedirectToLogin(LOGIN_REDIRECT);
	}
	
	@Test
	public void initController_whenInit_thenManagerInvoked() {
		givenControllers();
		whenInitControllers();
		thenManagerInvoked();
	}
	
	@Test
	public void handle_whenHandlerNotFound_thenResponseNull() throws Exception {
		whenHandle();
		thenResponseNull();
	}
	
	@Test
	public void handle_whenHandlerFoundNoAuth_thenResponse() throws Exception {
		givenHandlerWithNoAuthorization();
		whenHandle();
		thenHandleResponseInvoked();
	}
	
	@Test
	public void handle_whenHandlerFoundAuthUserNotLogIn_thenLoginResponse() throws Exception {
		givenHandlerWithAuthorization();
		whenHandle();
		thenResponseRedirectToLogin();
	}
	
	@Test
	public void handle_whenHandlerFoundAuthUserLogIn_thenNotAuthorized() throws Exception {
		givenHandlerWithAuthorization();
		givenUserLogIn();
		whenHandle();
		thenResponseCode(403);
	}
	
	@Test
	public void handle_whenHandlerFoundAuthUserLogIn_thenAuthorized() throws Exception {
		givenHandlerWithAuthorization();
		givenUserLogIn();
		givenUserRoleExpected();
		whenHandle();
		thenHandleResponseInvoked();
	}
	
	private void givenUserLogIn() {
		Mockito.when(mockRequestContext.getPrincipal()).thenReturn(UUID.randomUUID().toString());
	}
	
	private void givenUserRoleExpected() {
		Mockito.when(mockRequestContext.getUserRole()).thenReturn(REQUIRED_ROLE);
	}
	
	private void givenHandlerWithNoAuthorization() {
		mockRequestHandler = Mockito.mock(RequestHandler.class);
		Mockito.when(mockRequestHandler.requiresRole()).thenReturn(false);
		Mockito.when(mockControllerManager.getHandler(Mockito.anyString(), Mockito.anyString())).thenReturn(mockRequestHandler);
	}
	
	private void givenHandlerWithAuthorization() {
		mockRequestHandler = Mockito.mock(RequestHandler.class);
		Mockito.when(mockRequestHandler.requiresRole()).thenReturn(true);
		Mockito.when(mockRequestHandler.getRequiredRole()).thenReturn(REQUIRED_ROLE);
		Mockito.when(mockControllerManager.getHandler(Mockito.anyString(), Mockito.anyString())).thenReturn(mockRequestHandler);
	}
	
	private void givenControllers() {
		controllers = new ArrayList<Object>();
		controllers.add("XXX1");
		controllers.add("XXX2");
	}
	
	private void whenHandle() throws Exception {
		actual = servlet.handle(mockRequestContext);
	}
	
	private void whenInitControllers() {
		servlet.initControllers(controllers);
	}
	
	private void thenManagerInvoked() {
		Mockito.verify(mockControllerManager, Mockito.times(controllers.size())).register(Mockito.anyObject());
	}
	
	private void thenResponseNull() {
		assertNull(actual);
	}
	
	private void thenHandleResponseInvoked() {
		assertTrue(handleResponseInvoked);
	}
	
	private void thenResponseRedirectToLogin() {
		assertEquals(LOGIN_REDIRECT, actual.getRedirect());
	}
	
	private void thenResponseCode(int code) {
		assertEquals(code, actual.getCode());
	}
}
