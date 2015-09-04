package org.jcarvajal.framework.rest.config.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.jcarvajal.framework.rest.config.impl.XmlWebConfiguration;
import org.jcarvajal.framework.rest.exceptions.OnRestInitializationException;
import org.jcarvajal.framework.rest.servlet.Servlet;
import org.junit.Before;
import org.junit.Test;

public class XmlWebConfigurationTest {
	private final String WEB_FILE = "/web.xml";
	
	private XmlWebConfiguration webConfiguration;
	
	private Map<String, Servlet> actualMappings;
	
	@Before
	public void setup() throws OnRestInitializationException {
		webConfiguration = spy(new XmlWebConfiguration());
		when(webConfiguration.getFileStream(anyString())).thenReturn(this.getClass().getResourceAsStream(WEB_FILE));
	}
	
	@Test
	public void getServlets_whenInit_thenExpectedServlet() throws OnRestInitializationException {
		whenInit();
		whenGetServlets();
		thenActualMappingsAreTheExpectedInXml();
	}
	
	private void whenInit() throws OnRestInitializationException {
		webConfiguration.init();
	}
	
	private void whenGetServlets() {
		actualMappings = webConfiguration.getServlets();
	}
	
	private void thenActualMappingsAreTheExpectedInXml() {
		assertEquals(1, actualMappings.size());
		Servlet servlet = actualMappings.get("/");
		assertNotNull(servlet);
		assertEquals("servlet-dispatcher", servlet.getName());
		assertEquals("org.jcarvajal.framework.rest.servlet.ConfigurationDispatcherServlet", servlet.getClassName());
		assertEquals(1, servlet.getParams().size());
		String paramValue = servlet.getParams().get("contextConfigLocation");
		assertEquals("/servlet-dispatcher-config.xml", paramValue);
	}
}
