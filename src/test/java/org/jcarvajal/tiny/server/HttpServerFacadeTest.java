package org.jcarvajal.tiny.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class HttpServerFacadeTest {
	private static int PORT = 100;
	private HttpServerFacade serverFacade;
	
	private boolean actualStarted;
	private boolean actualStopped;
	
	@Before
	public void setup() throws IOException {
		HttpServerFacade realServerFacade = new HttpServerFacade();
		serverFacade = spy(realServerFacade);
		doNothing().when(serverFacade).startInternal(anyInt());
		doNothing().when(serverFacade).stopInternal();
	}
	
	@Test
	public void start_whenNotStarted_thenServerStarts() throws IOException {
		whenStart();
		thenIsStarted();
		thenStartInternalInvoked();
		thenStopInternalNotInvoked();
	}
	
	@Test
	public void start_whenExceptionAtStart_thenServerDidNotStart() throws IOException {
		givenExceptionAtStartServer();
		whenStart();
		thenIsNotStarted();
	}
	
	@Test
	public void stop_whenStarted_thenServerStops() throws IOException {
		whenStart();
		whenStop();
		thenIsStopped();
		thenStopInternalInvoked();
	}
	
	private void givenExceptionAtStartServer() throws IOException {
		doThrow(new IOException()).when(serverFacade).startInternal(anyInt());
	}
	
	private void whenStart() {
		actualStarted = serverFacade.start(PORT);
	}
	
	private void whenStop() {
		actualStopped = serverFacade.stop();
	}
	
	private void thenIsStarted() {
		assertTrue(actualStarted);
	}
	
	private void thenIsNotStarted() {
		assertFalse(actualStarted);
	}
	
	private void thenIsStopped() {
		assertTrue(actualStopped);
	}
	
	private void thenStartInternalInvoked() throws IOException {
		verify(serverFacade, times(1)).startInternal(PORT);
	}
	
	private void thenStopInternalInvoked() throws IOException {
		thenStopInternalInvoked(1);
	}
	
	private void thenStopInternalNotInvoked() throws IOException {
		thenStopInternalInvoked(0);
	}
	
	private void thenStopInternalInvoked(int n) throws IOException {
		verify(serverFacade, times(n)).stopInternal();
	}
}
