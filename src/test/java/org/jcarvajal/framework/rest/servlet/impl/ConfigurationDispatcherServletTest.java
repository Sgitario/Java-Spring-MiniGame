package org.jcarvajal.framework.rest.servlet.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.jcarvajal.framework.di.impl.ConfigDependencyInjectorImpl;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.impl.ConfigurationDispatcherServlet;
import org.jcarvajal.framework.rest.servlet.injector.DependencyInjector;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationDispatcherServletTest {
	private final String CONFIG_FILE = "/servlet-dispatcher-config.xml";
	
	private ConfigurationDispatcherServlet dispatcher;
	
	@Before
	public void setup() throws OnRestInitializationException {
		dispatcher = spy(new ConfigurationDispatcherServlet());
		when(dispatcher.getFileStream(anyString())).thenReturn(this.getClass().getResourceAsStream(CONFIG_FILE));
	}
	
	@Test
	public void init_thenInjectorShouldBeInitialized() throws OnRestInitializationException {
		whenInit();
		thenInjectorIsExpected();
	}
	
	private void whenInit() throws OnRestInitializationException {
		dispatcher.init();
	}
	
	private void thenInjectorIsExpected() {
		DependencyInjector injector = dispatcher.getInjector();
		assertNotNull(injector);
		assertTrue(injector instanceof ConfigDependencyInjectorImpl);
		assertEquals("/components.xml", ((ConfigDependencyInjectorImpl) injector).getConfigFile());
	}
}
