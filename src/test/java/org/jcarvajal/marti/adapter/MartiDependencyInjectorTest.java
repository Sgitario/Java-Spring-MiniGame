package org.jcarvajal.marti.adapter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.jcarvaja.minigame.infrastructure.ScoreRepository;
import org.jcarvaja.minigame.infrastructure.SessionRepository;
import org.jcarvaja.minigame.infrastructure.impl.MemoryScoreRepositoryImpl;
import org.jcarvaja.minigame.infrastructure.impl.MemorySessionRepositoryImpl;
import org.jcarvaja.minigame.service.ScoreService;
import org.jcarvaja.minigame.service.SessionService;
import org.jcarvaja.minigame.service.impl.ScoreServiceImpl;
import org.jcarvaja.minigame.service.impl.SessionServiceImpl;
import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.junit.Before;
import org.junit.Test;

public class MartiDependencyInjectorTest {
private final String CONFIG_FILE = "/components.xml";
	
	private MartiDependencyInjector injector;
	
	@Before
	public void setup() throws OnInitConfigurationException {
		injector = spy(new MartiDependencyInjector());
		when(injector.getFileStream(anyString())).thenReturn(this.getClass().getResourceAsStream(CONFIG_FILE));
	}
	
	@Test
	public void init_thenComponentsAreSetUp() throws OnInitConfigurationException {
		whenInit();
		thenComponentsAreExpected();
	}
	
	private void whenInit() throws OnInitConfigurationException {
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
		Object instance = injector.get(bind);
		assertNotNull(instance);
		assertTrue(impl.isAssignableFrom(instance.getClass()));
	}
}
