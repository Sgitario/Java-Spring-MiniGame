package org.jcarvajal.framework.rest.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.jcarvajal.framework.rest.server.HttpServerFacade;
import org.junit.Before;
import org.junit.Test;

public class HttpServerFacadeTest {
	private static int PORT = 100;
	private HttpServerFacade serverFacade;
	
	private boolean actualStarted;
	
	@Before
	public void setup() throws IOException {
		HttpServerFacade realServerFacade = new HttpServerFacade();
		serverFacade = spy(realServerFacade);
		doNothing().when(serverFacade).startInternal(anyInt());
	}
	
	@Test
	public void start_whenNotStarted_thenServerStarts() throws IOException {
		whenStart();
		thenIsStarted();
		thenStartInternalInvoked();
	}
	
	@Test
	public void start_whenExceptionAtStart_thenServerDidNotStart() throws IOException {
		givenExceptionAtStartServer();
		whenStart();
		thenIsNotStarted();
	}
	
	private void givenExceptionAtStartServer() throws IOException {
		doThrow(new IOException()).when(serverFacade).startInternal(anyInt());
	}
	
	private void whenStart() {
		actualStarted = serverFacade.start(PORT);
	}
	
	private void thenIsStarted() {
		assertTrue(actualStarted);
	}
	
	private void thenIsNotStarted() {
		assertFalse(actualStarted);
	}
	
	private void thenStartInternalInvoked() throws IOException {
		verify(serverFacade, times(1)).startInternal(PORT);
	}

}
