package org.jcarvajal.webapp.controllers;

import static org.junit.Assert.assertEquals;

import org.jcarvajal.webapp.server.RequestContext;
import org.jcarvajal.webapp.servlet.impl.ViewAndModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PageControllerTest {
	private PageController controller;
	
	private RequestContext mockRequestContext;
	
	private ViewAndModel actual;
	
	@Before
	public void setup() {
		mockRequestContext = Mockito.mock(RequestContext.class);
		
		controller = new PageController();
	}
	
	@Test
	public void page1_thenGoToPage1() {
		whenPage1();
		thenViewName("page1");
	}
	
	@Test
	public void page2_thenGoToPage2() {
		whenPage2();
		thenViewName("page2");
	}
	
	@Test
	public void page3_thenGoToPage3() {
		whenPage3();
		thenViewName("page3");
	}
	
	private void whenPage1() {
		actual = controller.getPage1(mockRequestContext);
	}
	
	private void whenPage2() {
		actual = controller.getPage2(mockRequestContext);
	}
	
	private void whenPage3() {
		actual = controller.getPage3(mockRequestContext);
	}
	
	private void thenViewName(String expected) {
		assertEquals(expected, actual.getViewName());
	}
}
