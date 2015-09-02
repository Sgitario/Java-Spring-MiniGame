package org.jcarvajal.tiny.web;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.jcarvajal.marti.adapter.MartiDependencyInjector;
import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.jcarvajal.tiny.injector.DependencyInjector;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationDispatcherServletTest {
	private final String CONFIG_FILE = "/tiny-dispatcher-config.xml";
	
	private ConfigurationDispatcherServlet dispatcher;
	
	@Before
	public void setup() throws OnInitConfigurationException {
		dispatcher = spy(new ConfigurationDispatcherServlet());
		when(dispatcher.getFileStream(anyString())).thenReturn(this.getClass().getResourceAsStream(CONFIG_FILE));
	}
	
	@Test
	public void init_thenInjectorShouldBeInitialized() throws OnInitConfigurationException {
		whenInit();
		thenInjectorIsExpected();
	}
	
	private void whenInit() throws OnInitConfigurationException {
		dispatcher.init();
	}
	
	private void thenInjectorIsExpected() {
		DependencyInjector injector = dispatcher.getInjector();
		assertNotNull(injector);
		assertTrue(injector instanceof MartiDependencyInjector);
		assertEquals("WEB-INF/components.xml", ((MartiDependencyInjector) injector).getConfigFile());
	}
}
