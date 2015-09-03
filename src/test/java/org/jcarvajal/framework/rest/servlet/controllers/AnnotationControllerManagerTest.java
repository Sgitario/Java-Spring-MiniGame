package org.jcarvajal.framework.rest.servlet.controllers;

import static org.junit.Assert.assertEquals;

import org.jcarvajal.framework.rest.annotations.PathVariable;
import org.jcarvajal.framework.rest.annotations.RequestMapping;
import org.jcarvajal.framework.rest.annotations.RequestParam;
import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.junit.Before;
import org.junit.Test;

public class AnnotationControllerManagerTest {
	
	private AnnotationControllerManager manager;
	
	private Object controller;
	
	private String expectedResponse;
	private Object actualResponse;
	
	@Before
	public void setup() {
		manager = new AnnotationControllerManager();
	}
	
	@Test
	public void register_whenRequestWithoutParams_thenUrlIsMatched() 
			throws OnRequestMappingInitializationException, OnRequestException {
		givenRegisterPingRequest();
		whenUrlIsForPing();
		thenResponsesMatch();
	}
	
	@Test
	public void register_whenRequestWithRequestParam_thenUrlIsMatched() 
			throws OnRequestMappingInitializationException, OnRequestException {
		givenRegisterEchoRequest();
		whenUrlIsForEcho();
		thenResponsesMatch();
	}
	
	@Test
	public void register_whenRequestWithAllTypeParams_thenUrlIsMatched() 
			throws OnRequestMappingInitializationException, OnRequestException {
		givenRegisterSumRequest();
		whenUrlIsForSum();
		thenResponsesMatch();
	}
	
	private void givenRegisterPingRequest() throws OnRequestMappingInitializationException {
		controller = new EchoController();
		expectedResponse = "pong";
		manager.register(controller);
	}
	
	private void givenRegisterEchoRequest() throws OnRequestMappingInitializationException {
		controller = new EchoController();
		expectedResponse = "test";
		manager.register(controller);
	}
	
	private void givenRegisterSumRequest() throws OnRequestMappingInitializationException {
		controller = new EchoController();
		expectedResponse = "Sum is 5";
		manager.register(controller);
	}
	
	private void whenUrlIsForPing() throws OnRequestException {
		actualResponse = manager.handle("/ping", RequestMethod.GET.toString());
	}
	
	private void whenUrlIsForEcho() throws OnRequestException {
		actualResponse = manager.handle("/echo?value=" + expectedResponse, RequestMethod.GET.toString());
	}
	
	private void whenUrlIsForSum() throws OnRequestException {
		actualResponse = manager.handle("/sum/2/3?label=Sum", RequestMethod.GET.toString());
	}
	
	private void thenResponsesMatch() {
		assertEquals(expectedResponse, actualResponse);
	}
	
	public class EchoController {
		private int value;
		
		public int getValue() {
			return value;
		}
		
		@RequestMapping(url="/ping", method = RequestMethod.GET)
		public String ping() {
			return "pong";
		}
		
		@RequestMapping(url="/echo", method = RequestMethod.GET)
		public String echo(@RequestParam(attr = "value") String value) {
			return value;
		}
		
		@RequestMapping(url="/sum/{value1}/{value2}", method = RequestMethod.GET)
		public String sum(@RequestParam(attr = "label") String value, 
				@PathVariable(name="value1") int value1, @PathVariable(name="value2") int value2) {
			return String.format("%s is %s", value, value1 + value2);
		}
		
		@RequestMapping(url="/echoController/{value1}", method = RequestMethod.GET)
		public EchoController sum(@PathVariable(name="value1") int value1) {
			
			value = value1 * 2;
			
			return this;
		}
	}
}
