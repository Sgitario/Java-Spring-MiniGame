package org.jcarvajal.webapp.servlet.controllers.impl;

import static org.junit.Assert.*;

import org.jcarvajal.webapp.server.RequestMethod;
import org.jcarvajal.webapp.servlet.controllers.handlers.RequestHandler;
import org.junit.Before;
import org.junit.Test;

public class AnnotationControllerManagerIntegrationTest {
	private AnnotationControllerManager manager;
	
	private Object controller;
	private String expectedPath;
	private RequestMethod expectedMethod;
	private String expectedRole;
	private boolean expectedRequiresRole;
	private RequestHandler actual;
	
	@Before
	public void setup() {
		manager = new AnnotationControllerManager();
	}
	
	@Test
	public void register_whenRequestPing_thenUrlIsMatched() {
		givenRegisterPingRequest();
		whenUrlIsForPing();
		thenHandlerIsExpected();
	}
	
	@Test
	public void register_whenRequestPing1_thenUrlIsMatched() {
		givenRegisterPing1Request();
		whenUrlIsForPing1();
		thenHandlerIsExpected();
	}
	
	private void givenRegisterPingRequest() {
		controller = new EchoController();
		expectedPath = "/ping";
		expectedMethod = RequestMethod.GET;
		expectedRole = null;
		expectedRequiresRole = false;
		manager.register(controller);
	}
	
	private void givenRegisterPing1Request() {
		controller = new EchoController();
		expectedPath = "/ping1";
		expectedMethod = RequestMethod.POST;
		expectedRole = "role1";
		expectedRequiresRole = true;
		manager.register(controller);
	}
	
	private void whenUrlIsForPing() {
		actual = manager.getHandler("/ping", RequestMethod.GET.toString());
	}
	
	private void whenUrlIsForPing1() {
		actual = manager.getHandler("/ping1", RequestMethod.POST.toString());
	}
	
	private void thenHandlerIsExpected() {
		assertEquals(expectedPath, actual.getMappingUrl());
		assertEquals(expectedMethod, actual.getMethod());
		
		assertEquals(expectedRequiresRole, actual.requiresRole());
		if (actual.requiresRole()) {
			assertEquals(expectedRole, actual.getRequiredRole());
		}
	}
	
	public class EchoController {		
		@RequestMapping(url="/ping", method = RequestMethod.GET)
		public String ping() {
			return "pong";
		}
		
		@RequestMapping(url="/ping1", method = RequestMethod.POST)
		@Role(name = "role1")
		public String pingSecured() {
			return "pong";
		}
	}
}
