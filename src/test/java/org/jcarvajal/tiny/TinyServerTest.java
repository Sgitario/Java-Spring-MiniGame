package org.jcarvajal.tiny;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jcarvajal.tiny.config.Servlet;
import org.jcarvajal.tiny.config.WebConfiguration;
import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.jcarvajal.tiny.server.ServerFacade;
import org.jcarvajal.tiny.server.ServerFactory;
import org.junit.Before;
import org.junit.Test;

public class TinyServerTest {
	
	private TinyServer server;
	private ServerFacade mockServerFacade;
	private ServerFactory mockServerFactory;
	private WebConfiguration mockWebConfiguration;
	
	private Map<String, Servlet> expectedServlets;
	
	private boolean actualServerStarted;
	
	@Before
	public void setup() throws OnInitConfigurationException {
		mockServerFacade = mock(ServerFacade.class);
		mockServerFactory = mock(ServerFactory.class);
		mockWebConfiguration = mock(WebConfiguration.class);
		when(mockServerFactory.bindServer(anyInt())).thenReturn(mockServerFactory);
		when(mockServerFactory.get()).thenReturn(mockServerFacade);
		
		server = new TinyServer();
		server.setServerFactory(mockServerFactory);
		server.setConfig(mockWebConfiguration);
	}
	
	@Test
	public void start_whenServerCannotStart_thenReturnFalse() {
		givenServerCannotStart();
		whenServerStarts();
		thenServerNotStarted();
	}
	
	@Test
	public void start_thenServerStartedWithWebConfiguration() {
		givenServletsInWebConfig();
		whenServerStarts();
		thenServerIsProperlyConfigured();
	}
	
	@Test
	public void stop_thenServerStopsIsCalled() {
		givenServerRunning();
		whenServerStarts();
		whenServerStops();
		thenServerIsStopped();
	}
	
	private void givenServerRunning() {
		when(mockServerFacade.isStarted()).thenReturn(true);
		when(mockServerFacade.stop()).thenReturn(true);
	}
	
	private void givenServerCannotStart() {
		when(mockServerFacade.start(anyInt())).thenReturn(false);
	}
	
	private void givenServletsInWebConfig() {
		expectedServlets = new LinkedHashMap<String, Servlet>();
		expectedServlets.put("key1", new Servlet());
		when(mockWebConfiguration.getServlets()).thenReturn(expectedServlets);
	}
	
	private void whenServerStarts() {
		actualServerStarted = server.start();
	}
	
	private void whenServerStops() {
		server.stop();
	}
	
	private void thenServerNotStarted() {
		assertFalse(actualServerStarted);
	}
	
	private void thenServerIsStopped() {
		verify(mockServerFacade, times(1)).stop();
	}
	
	private void thenServerIsProperlyConfigured() {
		if (expectedServlets != null) {
			for (Entry<String, Servlet> entry : expectedServlets.entrySet()) {
				verify(mockServerFactory, times(1)).addContext(
						entry.getKey(), entry.getValue());
			}
		}	
	}
}
