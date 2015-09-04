package org.jcarvajal.framework.di.builders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.jcarvajal.framework.di.DependencyInjectorBase;
import org.jcarvajal.framework.di.exceptions.InstantiationException;
import org.jcarvajal.framework.di.instances.InitializationInstance;
import org.jcarvajal.minigame.infrastructure.ScoreRepository;
import org.jcarvajal.minigame.service.ScoreService;
import org.jcarvajal.minigame.service.impl.ScoreServiceImpl;
import org.junit.Before;
import org.junit.Test;

public class InitializationInstanceTest {
	
	private DependencyInjectorBase mockInjector;
	
	private InitializationInstance instance;
	
	private Class<?> bind;
	private Class<?> implementedBy;
	private Map<String, String> params;
	
	@Before
	public void setup() {
		mockInjector = mock(DependencyInjectorBase.class);
	}
	
	@Test
	public void bind_whenClassHasAutowired_thenAutowiredAreCreated() 
			throws InstantiationException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		givenRegisteredInInjector(ScoreRepository.class);
		givenClassesToBind(ScoreService.class, ScoreServiceImpl.class);
		whenCreateInstance();
		thenInjectorReturnsImplInstance();
	}
	
	private <T> void givenRegisteredInInjector(Class<T> bind) {
		T instance = mock(bind);
		when(mockInjector.get(eq(bind.getName()))).thenReturn(instance);
	}
	
	private void givenClassesToBind(Class<?> bind, Class<?> implementedBy) {
		this.bind = bind;
		this.implementedBy = implementedBy;
		this.params = new HashMap<String, String>();
	}
	
	private void whenCreateInstance() throws InstantiationException {
		instance = new InitializationInstance(bind.getName(), implementedBy.getName(), params, mockInjector);
	}
	
	private void thenInjectorReturnsImplInstance() {
		Object actual = instance.instance();
		assertNotNull(actual);
		assertTrue(implementedBy.isAssignableFrom(actual.getClass()));
	}
}
