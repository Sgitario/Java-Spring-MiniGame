package org.jcarvajal.webapp.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jcarvajal.webapp.server.ServerFacade;
import org.jcarvajal.webapp.servlet.DispatcherServlet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ServerTest {
	
	private static final int PORT = 1000;
	
	private Server server;
	private DispatcherServlet mockDispatcherServlet;
	private ServerFacade mockServerFacade;
	
	private boolean actualServerStarted;
	
	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		mockServerFacade = mock(ServerFacade.class);		
		server = new Server(mockServerFacade, PORT);
	}
	
	@Test
	public void start_whenServerCannotStart_thenResultIsFalse() {
		givenServerCannotStart();
		whenServerStarts();
		thenServerNotStarted();
	}
	
	@Test(expected = RuntimeException.class)
	public void start_whenServerCanStartAndServletNull_thenException() {
		givenServerCanStart();
		whenServerStarts();
	}
	
	@Test
	public void start_whenServerCanStart_thenServletInit() {
		givenServerCanStart();
		givenServlet();
		whenServerStarts();
		thenServerStarted();
		thenServletIsInit();
	}
	
	private void givenServerCannotStart() {
		when(mockServerFacade.isStarted()).thenReturn(false);
	}
	
	private void givenServerCanStart() {
		when(mockServerFacade.isStarted()).thenReturn(true);
	}
	
	private void givenServlet() {
		mockDispatcherServlet = mock(DispatcherServlet.class);
		server.setServlet(mockDispatcherServlet);
	}
	
	private void whenServerStarts() {
		actualServerStarted = server.start();
	}
	
	private void thenServerNotStarted() {
		assertFalse(actualServerStarted);
	}
	
	private void thenServerStarted() {
		assertTrue(actualServerStarted);
	}
	
	private void thenServletIsInit() {
		Mockito.verify(mockDispatcherServlet, Mockito.times(1)).addObserver(server);
		Mockito.verify(mockServerFacade, Mockito.times(1)).createContext("/", mockDispatcherServlet);
	}
}
