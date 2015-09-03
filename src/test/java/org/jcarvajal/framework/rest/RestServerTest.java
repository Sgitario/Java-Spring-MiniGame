package org.jcarvajal.framework.rest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jcarvajal.framework.rest.RestServer;
import org.jcarvajal.framework.rest.config.Servlet;
import org.jcarvajal.framework.rest.config.WebConfiguration;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.server.ServerFacade;
import org.jcarvajal.framework.rest.server.ServerFactory;
import org.junit.Before;
import org.junit.Test;

public class RestServerTest {
	
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
		when(mockServerFactory.bindServer(anyInt())).thenReturn(mockServerFactory);
		when(mockServerFactory.get()).thenReturn(mockServerFacade);
		
		server = new RestServer();
		server.setServerFactory(mockServerFactory);
		server.setConfig(mockWebConfiguration);
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
		expectedServlets.put("key1", new Servlet());
		when(mockWebConfiguration.getServlets()).thenReturn(expectedServlets);
	}
	
	private void whenServerStarts() throws OnRestInitializationException {
		actualServerStarted = server.start();
	}
	
	private void thenServerNotStarted() {
		assertFalse(actualServerStarted);
	}
	
	private void thenServerIsProperlyConfigured() throws OnRestInitializationException {
		if (expectedServlets != null) {
			for (Entry<String, Servlet> entry : expectedServlets.entrySet()) {
				verify(mockServerFactory, times(1)).addContext(
						entry.getKey(), entry.getValue());
			}
		}	
	}
}
