package org.jcarvajal.tiny.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.jcarvajal.tiny.exceptions.OnInitConfigurationException;
import org.junit.Before;
import org.junit.Test;

public class XmlWebConfigurationTest {
	private final String WEB_FILE = "/web.xml";
	
	private XmlWebConfiguration webConfiguration;
	
	private Map<String, Servlet> actualMappings;
	
	@Before
	public void setup() {
		webConfiguration = spy(new XmlWebConfiguration());
		when(webConfiguration.getFileStream(anyString())).thenReturn(this.getClass().getResourceAsStream(WEB_FILE));
	}
	
	@Test
	public void getServlets_whenInit_thenExpectedServlet() throws OnInitConfigurationException {
		whenInit();
		whenGetServlets();
		thenActualMappingsAreTheExpectedInXml();
	}
	
	private void whenInit() throws OnInitConfigurationException {
		webConfiguration.init();
	}
	
	private void whenGetServlets() {
		actualMappings = webConfiguration.getServlets();
	}
	
	private void thenActualMappingsAreTheExpectedInXml() {
		assertEquals(1, actualMappings.size());
		Servlet servlet = actualMappings.get("/");
		assertNotNull(servlet);
		assertEquals("tiny-web-dispatcher", servlet.getName());
		assertEquals("org.jcarvajal.tiny.web.DispatcherServlet", servlet.getClassName());
		assertEquals(1, servlet.getParams().size());
		String paramValue = servlet.getParams().get("contextConfigLocation");
		assertEquals("/WEB-INF/tiny-dispatcher-config.xml", paramValue);
	}
}
