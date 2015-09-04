package org.jcarvajal.framework.rest.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jcarvajal.framework.rest.server.impl.HttpServerFacade;
import org.jcarvajal.framework.rest.server.ServerFacade;
import org.jcarvajal.framework.rest.server.ServerFactory;
import org.junit.Before;
import org.junit.Test;

public class ServerFactoryTest {
	
	private static int PORT = 100;
	
	private ServerFactory serverFactory;
	private ServerFacade mockServerFacade;
	
	private ServerFacade actualServerFacade;
	
	@Before
	public void setup() {
		serverFactory = new ServerFactory();
	}
	
	@Test
	public void get_thenReturnHttpServer() {
		whenGet();
		thenServerFacadeIsHttpStatus();
	}
	
	@Test
	public void bindServer_thenServerStartIsInvoked() {
		givenMockServerFacade();
		whenBindServer();
		thenServerFacadeCalledStart();
	}
	
	private void givenMockServerFacade() {
		mockServerFacade = mock(ServerFacade.class);
		serverFactory.setFacade(mockServerFacade);
	}
	
	private void whenGet() {
		actualServerFacade = serverFactory.get();
	}
	
	private void whenBindServer() {
		serverFactory.startServer(PORT);
	}
	
	private void thenServerFacadeCalledStart() {
		verify(mockServerFacade, times(1)).start(PORT);
	}
	
	private void thenServerFacadeIsHttpStatus() {
		assertTrue(actualServerFacade instanceof HttpServerFacade);
	}
}
