package org.jcarvajal.framework.rest.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jcarvajal.framework.rest.controllers.annotations.AnnotationControllerManager;
import org.jcarvajal.framework.rest.controllers.annotations.PathVariable;
import org.jcarvajal.framework.rest.controllers.annotations.RequestBody;
import org.jcarvajal.framework.rest.controllers.annotations.RequestMapping;
import org.jcarvajal.framework.rest.controllers.annotations.RequestParam;
import org.jcarvajal.framework.rest.controllers.annotations.ResponseMapping;
import org.jcarvajal.framework.rest.exceptions.OnRequestException;
import org.jcarvajal.framework.rest.exceptions.OnRequestMappingInitializationException;
import org.jcarvajal.framework.rest.models.RequestMethod;
import org.junit.Before;
import org.junit.Test;

public class AnnotationControllerManagerTest {
	
	private AnnotationControllerManager manager;
	
	private Object controller;
	
	private String expectedResponse;
	private byte[] actualResponse;
	
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
	
	@Test
	public void register_whenRequestWithResponseMapping_thenUrlIsMatched() 
			throws OnRequestMappingInitializationException, OnRequestException {
		givenRegisterControllerRequest();
		whenUrlIsForController();
		thenResponsesMatch();
	}
	
	@Test
	public void register_whenRequestPostWithBody_thenUrlIsMatched() 
			throws OnRequestMappingInitializationException, OnRequestException, IOException {
		givenRegisterPostRequest();
		whenUrlIsForPost();
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
	
	private void givenRegisterControllerRequest() throws OnRequestMappingInitializationException {
		controller = new EchoController();
		expectedResponse = "10";
		manager.register(controller);
	}
	
	private void givenRegisterPostRequest() throws OnRequestMappingInitializationException {
		controller = new EchoController();
		expectedResponse = "10 and 6";
		manager.register(controller);
	}
	
	private void whenUrlIsForPing() throws OnRequestException {
		actualResponse = manager.handle("/ping", RequestMethod.GET.toString(), null);
	}
	
	private void whenUrlIsForEcho() throws OnRequestException {
		actualResponse = manager.handle("/echo?value=" + expectedResponse, RequestMethod.GET.toString(), null);
	}
	
	private void whenUrlIsForSum() throws OnRequestException {
		actualResponse = manager.handle("/sum/2/3?label=Sum", RequestMethod.GET.toString(), null);
	}
	
	private void whenUrlIsForController() throws OnRequestException {
		actualResponse = manager.handle("/controller/5", RequestMethod.GET.toString(), null);
	}
	
	private void whenUrlIsForPost() throws OnRequestException, IOException {
		String text = "10";
		byte[] stringByte = text.getBytes();
	    ByteArrayOutputStream bos = new ByteArrayOutputStream(text.length());
	    bos.write(stringByte);
		
		actualResponse = manager.handle("/post/5", RequestMethod.POST.toString(), bos);
	}
	
	private void thenResponsesMatch() {
		assertNotNull(actualResponse);
		String actualResponseStr = new String(actualResponse);
		assertEquals(expectedResponse, actualResponseStr);
	}
	
	public class EchoController {
		private int property;
		
		public int getProperty() {
			return property;
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
		
		@RequestMapping(url="/controller/{value1}", method = RequestMethod.GET)
		@ResponseMapping(map="{property}")
		public EchoController controller(@PathVariable(name="value1") int value1) {
			
			property = value1 * 2;
			
			return this;
		}
		
		@RequestMapping(url="/post/{value1}", method = RequestMethod.POST)
		public String post(@PathVariable(name="value1") int value1, @RequestBody int body) {
			return String.format("%s and %s", body, value1 + 1);
		}
	}
}
