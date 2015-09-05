package org.jcarvajal.framework.rest.server;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.server.impl.HttpServerFacade;
import org.jcarvajal.framework.rest.servlet.Servlet;
import org.junit.Before;
import org.junit.Test;

public class ServerFactoryTest {
	
	private static int PORT = 100;
	
	private Map<String, Servlet> servlets;
	private ServerFactory serverFactory;
	private ServerFacade mockServerFacade;
	
	private ServerFacade actualServerFacade;
	
	@Before
	public void setup() {
		servlets = new HashMap<String, Servlet>();
		
		serverFactory = new ServerFactory();
	}
	
	@Test
	public void get_thenReturnHttpServer() {
		whenGet();
		thenServerFacadeIsHttpStatus();
	}
	
	@Test
	public void startServer_thenServerStartIsInvoked() {
		givenMockServerFacade();
		whenStartServer();
		thenServerFacadeCalledStart();
	}
	
	@Test
	public void addContext_thenServerStartIsInvoked() 
			throws OnRestInitializationException {
		givenMockServerFacade();
		givenContexts("/*", MockDispatcherServletProxy.class);
		whenAddContexts();
		thenServerFacadeCreateContextWithExpected();
	}
	
	private void givenMockServerFacade() {
		mockServerFacade = mock(ServerFacade.class);
		serverFactory.setFacade(mockServerFacade);
	}
	
	private void givenContexts(String mapping, Class<?> servletClazz) {
		Servlet servlet = new Servlet(UUID.randomUUID().toString(),
				servletClazz.getName(), null);
		servlets.put(mapping, servlet);
	}
	
	private void whenGet() {
		actualServerFacade = serverFactory.get();
	}
	
	private void whenAddContexts() throws OnRestInitializationException {
		serverFactory.addContext(servlets);
	}
	
	private void whenStartServer() {
		serverFactory.startServer(PORT);
	}
	
	private void thenServerFacadeCalledStart() {
		verify(mockServerFacade, times(1)).start(PORT);
	}
	
	private void thenServerFacadeIsHttpStatus() {
		assertTrue(actualServerFacade instanceof HttpServerFacade);
	}
	
	private void thenServerFacadeCreateContextWithExpected() {
		for (Entry<String, Servlet> servlet : servlets.entrySet()) {
			verify(mockServerFacade, times(1)).createContext(eq(servlet.getKey()), 
					argThat(new BaseMatcher<MockDispatcherServletProxy>() {

						public void describeTo(Description arg0) {
							
						}

						public boolean matches(Object arg) {
							if (arg instanceof MockDispatcherServletProxy) {
								MockDispatcherServletProxy proxy = (MockDispatcherServletProxy) arg;
								assertTrue(proxy.getInitHasBeenInvoked());
								
								return true;
							}
							
							return false;
						}
						
					}));
		}
	}
}
