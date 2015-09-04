package org.jcarvajal.framework.rest;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.jcarvajal.framework.rest.config.WebConfiguration;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.server.ServerFacade;
import org.jcarvajal.framework.rest.server.ServerFactory;
import org.jcarvajal.framework.rest.servlet.Servlet;
import org.junit.Before;
import org.junit.Test;

public class RestServerTest {
	
	private static final int PORT = 1000;
	
	private RestServer server;
	private ServerFacade mockServerFacade;
	private ServerFactory mockServerFactory;
	private WebConfiguration mockWebConfiguration;
	
	private Map<String, Servlet> expectedServlets;
	
	private boolean actualServerStarted;
	
	@Before
	public void setup() throws OnRestInitializationException {
		mockServerFacade = mock(ServerFacade.class);
		mockServerFactory = mock(ServerFactory.class);
		mockWebConfiguration = mock(WebConfiguration.class);
		when(mockServerFactory.startServer(anyInt())).thenReturn(mockServerFactory);
		when(mockServerFactory.addContext(any(Map.class))).thenReturn(mockServerFactory);
		when(mockServerFactory.get()).thenReturn(mockServerFacade);
		
		server = new RestServer(mockServerFactory, mockWebConfiguration, PORT);
	}
	
	@Test
	public void start_whenServerCannotStart_thenReturnFalse() throws OnRestInitializationException {
		givenServerCannotStart();
		whenServerStarts();
		thenServerNotStarted();
	}
	
	@Test
	public void start_thenServerStartedWithWebConfiguration() throws OnRestInitializationException {
		givenServletsInWebConfig();
		whenServerStarts();
		thenServerIsProperlyConfigured();
	}
	
	private void givenServerCannotStart() {
		when(mockServerFacade.start(anyInt())).thenReturn(false);
	}
	
	private void givenServletsInWebConfig() {
		expectedServlets = new LinkedHashMap<String, Servlet>();
		expectedServlets.put("key1", new Servlet(UUID.randomUUID().toString(), UUID.randomUUID().toString(), null));
		when(mockWebConfiguration.getServlets()).thenReturn(expectedServlets);
	}
	
	private void whenServerStarts() throws OnRestInitializationException {
		actualServerStarted = server.start();
	}
	
	private void thenServerNotStarted() {
		assertFalse(actualServerStarted);
	}
	
	private void thenServerIsProperlyConfigured() throws OnRestInitializationException {
		verify(mockServerFactory, times(1)).addContext(
				eq(expectedServlets));
	}
}
