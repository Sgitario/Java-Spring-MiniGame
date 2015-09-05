package org.jcarvajal.framework.di.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.jcarvajal.framework.di.exceptions.OnDependencyInjectionInitializationException;
import org.jcarvajal.framework.di.impl.ConfigDependencyInjectorImpl;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.minigame.infrastructure.ScoreRepository;
import org.jcarvajal.minigame.infrastructure.SessionRepository;
import org.jcarvajal.minigame.infrastructure.impl.MemoryScoreRepositoryImpl;
import org.jcarvajal.minigame.infrastructure.impl.MemorySessionRepositoryImpl;
import org.jcarvajal.minigame.service.ScoreService;
import org.jcarvajal.minigame.service.SessionService;
import org.jcarvajal.minigame.service.impl.ScoreServiceImpl;
import org.jcarvajal.minigame.service.impl.SessionServiceImpl;
import org.junit.Before;
import org.junit.Test;

public class ConfigDependencyInjectorImplTest {
	private final String CONFIG_FILE = "/components.xml";
	
	private ConfigDependencyInjectorImpl injector;
	
	@Before
	public void setup() {
		injector = spy(new ConfigDependencyInjectorImpl());
	}
	
	/**
	 * When components file not found.
	 * Then exception should be raised.
	 * @throws OnRestInitializationException
	 */
	@Test(expected = OnRestInitializationException.class)
	public void init_whenFileNotFound_thenExceptionIsRaised() throws OnRestInitializationException {
		whenInit();
	}
	
	/**
	 * When components file not exist.
	 * Then exception should be raised.
	 * @throws OnRestInitializationException
	 */
	@Test(expected = OnRestInitializationException.class)
	public void init_whenFileNotExist_thenExceptionIsRaised() throws OnRestInitializationException {
		givenComponentsFileDoesNotExist();
		whenInit();
	}
	
	/**
	 * When init the xml in test/resources.
	 * Then all their components should have been properly parsed and registered.
	 * @throws OnDependencyInjectionInitializationException 
	 * @throws OnRestInitializationException
	 */
	@Test
	public void init_thenComponentsAreSetUp() 
			throws OnDependencyInjectionInitializationException, OnRestInitializationException {
		givenComponentsFileInTestResources();
		whenInit();
		thenComponentsAreExpected();
	}
	
	private void givenComponentsFileDoesNotExist() {
		injector.setConfigFile("DOES NOT EXIST");
	}
	
	private void givenComponentsFileInTestResources() throws OnDependencyInjectionInitializationException {
		when(injector.getFileStream(anyString())).thenReturn(this.getClass().getResourceAsStream(CONFIG_FILE));
	}
	
	private void whenInit() throws OnRestInitializationException {
		injector.init();
	}
	
	private void thenComponentsAreExpected() {
		thenInstanceImplFound(ScoreRepository.class, MemoryScoreRepositoryImpl.class);
		thenInstanceImplFound(SessionRepository.class, MemorySessionRepositoryImpl.class);
		thenInstanceImplFound(SessionService.class, SessionServiceImpl.class);
		thenInstanceImplFound(ScoreService.class, ScoreServiceImpl.class);
	}

	private void thenInstanceImplFound(Class<?> bind,
			Class<?> impl) {
		Object instance = injector.get(bind.getName());
		assertNotNull(instance);
		assertTrue(impl.isAssignableFrom(instance.getClass()));
	}
}
